package com.xue.siu.module.news.presenter;


import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnLoadMoreListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnRefreshListener;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.image.imagepreview.activity.MultiItemImagesPreviewActivity;
import com.xue.siu.module.image.imagepreview.activity.SingleItemImagePreviewActivity;
import com.xue.siu.module.news.activity.ActionFragment;
import com.xue.siu.module.news.model.ActionVO;
import com.xue.siu.module.news.model.CommentVO;
import com.xue.siu.module.news.viewholder.NewsActionViewHolder;
import com.xue.siu.module.news.viewholder.NewsDecorationViewHolder;
import com.xue.siu.module.news.viewholder.item.NewsActionViewHolderItem;
import com.xue.siu.module.news.viewholder.item.NewsDecorationViewHolderItem;
import com.xue.siu.module.news.viewholder.item.NewsItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XUE on 2016/2/15.
 */
public class ActionPresenter extends BaseFragmentPresenter<ActionFragment> implements
        OnRefreshListener, OnLoadMoreListener, ItemEventListener, HTSwipeRecyclerView.OnScrollListener {
    private List<TAdapterItem<ActionVO>> mAdapterItems = new ArrayList<>();
    private HashMap<String, ActionVO> actionMap = new HashMap<>();
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(NewsItemType.ITEM_COMMON_ACTION, NewsActionViewHolder.class);
                    put(NewsItemType.ITEM_COMMON_DECORATION, NewsDecorationViewHolder.class);
                }
            };
    private TRecycleViewAdapter mAdapter;

    public ActionPresenter(ActionFragment target) {
        super(target);
    }

    @Override
    public void initFragment() {
        mAdapter = new TRecycleViewAdapter(mTarget.getContext(), mViewHolders, mAdapterItems);
        mAdapter.setItemEventListener(this);
        mTarget.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        DialogUtil.showProgressDialog(mTarget.getActivity(), true);
        AVFriendshipQuery query = AVUser.friendshipQuery(AVUser.getCurrentUser().getObjectId());
        query.getInBackground(new AVFriendshipCallback() {
            @Override
            public void done(AVFriendship avFriendship, AVException e) {
                if (e == null) {
                    List<AVUser> list = avFriendship.getFollowees();
                    list.add(AVUser.getCurrentUser());
                    AVQuery<AVObject> postQuery = new AVQuery<>(LeanConstants.POST_TABLE_NAME);
                    postQuery.whereContainedIn(LeanConstants.CREATOR_FILED_NAME, list);
                    postQuery.orderByDescending(LeanConstants.CREATE_TIME);
                    postQuery.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            DialogUtil.hideProgressDialog(mTarget.getActivity());
                            if (e == null) {
                                bindData(list);
                            } else {
                                ToastUtil.makeShortToast(R.string.na_hint_error);
                            }
                        }
                    });
                } else {
                    DialogUtil.hideProgressDialog(mTarget.getActivity());
                    ToastUtil.makeShortToast(R.string.na_hint_error);
                }
            }
        });
    }

    private void bindData(List<AVObject> list) {
        mTarget.setRefreshComplete();
        new FetchDataTask(list).execute();
    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (view.getId()) {
            case R.id.sdv_pic_news:
                if (values != null) {
                    AVFile avFile = (AVFile) values[0];
                    String currentUrl = avFile.getUrl();
                    List<AVFile> files = (List<AVFile>) values[1];
                    ArrayList<String> urls = new ArrayList<>();
                    for (AVFile file : files) {
                        urls.add(file.getUrl());
                    }
                    startPreviewMulImages(currentUrl, urls);
                }
                break;
            case R.id.btn_comment_news_fragment:
                ActionVO actionVO = mAdapterItems.get(position).getDataModel();
                mTarget.showInput(actionVO, null);
                break;
        }
        return true;
    }

    private void startPreviewMulImages(String currentUrl, ArrayList<String> urls) {
        MultiItemImagesPreviewActivity.start(mTarget.getActivity(), currentUrl, urls);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            mTarget.hideInput();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {
        private List<AVObject> avList;

        public FetchDataTask(List<AVObject> avList) {
            this.avList = avList;
        }

        @Override
        protected Void doInBackground(Void... params) {
            int i = 0;
            mAdapterItems.clear();
            for (AVObject object : avList) {
                ActionVO actionVO = ActionVO.parse(object);
                try {
                    actionVO.setCreator((AVUser) object.getAVObject("creator").fetch());
                    AVRelation<AVObject> relation = object.getRelation("picList");

                    List<AVFile> files = new ArrayList<>();
                    List<AVObject> obList = relation.getQuery().find();
                    for (AVObject object1 : obList) {
                        files.add(AVFile.withAVObject(object1));
                    }
                    List<CommentVO> commentList = fetchComment(object);
                    LogUtil.i("xxj", "评论数" + commentList.size());
                    actionVO.setPicList(files);
                    actionVO.setCommentList(commentList);
                    mAdapterItems.add(new NewsActionViewHolderItem(actionVO));
                    actionMap.put(actionVO.getObjectId(), actionVO);
                    i++;
                    if (i < avList.size()) {
                        mAdapterItems.add(new NewsDecorationViewHolderItem());
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            mAdapter.notifyDataSetChanged();
            DialogUtil.hideProgressDialog(mTarget.getActivity());
        }
    }

    public void fetchLatestComment(String postId) {
        AVObject object = AVObject.createWithoutData("Post", postId);
        AVRelation<AVObject> commentRelation = object.getRelation("commentList");
        List<CommentVO> commentList = new ArrayList<>();
    }

    public List<CommentVO> fetchComment(AVObject post) throws AVException {
        AVQuery<AVObject> postQuery = new AVQuery<>(LeanConstants.COMMENT_TABLE_NAME);
        postQuery.include("from");
        postQuery.include("to");
        postQuery.orderByDescending(LeanConstants.CREATE_TIME);
        postQuery.whereEqualTo(LeanConstants.COMMENT_POST, post);
        List<AVObject> avos = postQuery.find();
        List<CommentVO> commentVOs = new ArrayList<>();
        for (AVObject object : avos) {
            commentVOs.add(CommentVO.parse(object));
        }
        return commentVOs;
    }
}

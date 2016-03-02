package com.xue.siu.module.news.presenter;

import android.util.SparseArray;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnLoadMoreListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnRefreshListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.news.activity.ActionFragment;
import com.xue.siu.module.news.model.ActionVO;
import com.xue.siu.module.news.viewholder.NewsActionViewHolder;
import com.xue.siu.module.news.viewholder.item.NewsItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/2/15.
 */
public class ActionPresenter extends BaseFragmentPresenter<ActionFragment> implements
        OnRefreshListener, OnLoadMoreListener {
    private List<TAdapterItem<ActionVO>> mAdapterItems = new ArrayList<>();
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(NewsItemType.ITEM_COMMON_ACTION, NewsActionViewHolder.class);
                }
            };
    private TRecycleViewAdapter mAdapter;

    public ActionPresenter(ActionFragment target) {
        super(target);
    }

    @Override
    public void initFragment() {
        mAdapter = new TRecycleViewAdapter(mTarget.getContext(), mViewHolders, mAdapterItems);
        mTarget.setAdapter(mAdapter);
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
                    AVQuery<AVObject> postQuery = new AVQuery<>("XPost");
                    postQuery.whereContainedIn("creator", list);
                    postQuery.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {

                        }
                    });
                } else {
                    DialogUtil.hideProgressDialog(mTarget.getActivity());
                    ToastUtil.makeShortToast("网络错误");
                }
            }
        });
    }

    @Override
    public void onLoadMore() {

    }
}

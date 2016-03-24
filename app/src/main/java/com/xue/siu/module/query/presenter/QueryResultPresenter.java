package com.xue.siu.module.query.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.netease.hearttouch.htrecycleview.util.LogUtil;
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.follow.viewholder.FollowItemViewHolder;
import com.xue.siu.module.follow.viewholder.item.FollowViewHolderItem;
import com.xue.siu.module.follow.viewholder.item.ItemType;
import com.xue.siu.module.query.activity.QueryResultActivity;
import com.xue.siu.module.userpage.activity.UserDataActivity;
import com.xue.siu.module.userpage.usertype.FriendshipType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/21.
 */
public class QueryResultPresenter extends BaseActivityPresenter<QueryResultActivity> implements ItemEventListener {

    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();
    private List<TAdapterItem<AVUser>> mTAdapterItems = new ArrayList<>();
    private TRecycleViewAdapter mAdapter;
    private String mKeyWord;

    public QueryResultPresenter(QueryResultActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        mViewHolders.put(ItemType.ITEM_COMMON, FollowItemViewHolder.class);
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mTAdapterItems);
        mAdapter.setItemEventListener(this);
        mTarget.setAdapter(mAdapter);
        getKeyWord();
    }

    private void getKeyWord() {
        Intent intent = mTarget.getIntent();
        mKeyWord = intent.getStringExtra(QueryResultActivity.KEY_SEARCH);
        if (mKeyWord != null) {
            queryUserInBackground();
        }
    }

    private void queryUserInBackground() {
        DialogUtil.showProgressDialog(mTarget, false);
        LogUtil.d("xxj", "keyword " + mKeyWord);
        AVQuery<AVUser> query = AVQuery.getQuery("_User");
        query.whereEqualTo("username", mKeyWord);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (list != null)
                    transformDataToItems(list);
                DialogUtil.hideProgressDialog(mTarget);
            }
        });
    }

    private void transformDataToItems(List<AVUser> list) {
        for (AVUser user : list) {
            mTAdapterItems.add(new FollowViewHolderItem(user));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        if (TextUtils.equals(eventName, ItemEventListener.clickEventName)) {
            AVUser user = mTAdapterItems.get(position).getDataModel();
            UserDataActivity.start(mTarget, user);
        }
        return true;
    }
}

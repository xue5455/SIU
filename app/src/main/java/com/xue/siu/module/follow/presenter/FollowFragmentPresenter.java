package com.xue.siu.module.follow.presenter;

import android.util.SparseArray;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnRefreshListener;
import com.xue.siu.avim.LeanFriendshipCache;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowFragment;
import com.xue.siu.module.follow.callback.FriendshipCallback;
import com.xue.siu.module.follow.viewholder.FollowItemViewHolder;
import com.xue.siu.module.follow.viewholder.item.FollowViewHolderItem;
import com.xue.siu.module.follow.viewholder.item.ItemType;
import com.xue.siu.module.userpage.activity.UserDataActivity;
import com.xue.siu.module.userpage.usertype.FriendshipType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/18.
 */
public class FollowFragmentPresenter extends BaseFragmentPresenter<FollowFragment> implements
        OnRefreshListener,
        ItemEventListener,
        AVIMResultListener {
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();
    private List<TAdapterItem<AVUser>> mTAdapterItems = new ArrayList<>();
    private TRecycleViewAdapter mAdapter;
    private AVFriendshipCallback mFriendshipCallback = new AVFriendshipCallback() {
        @Override
        public void done(AVFriendship avFriendship, AVException e) {
            if (e == null) {
                onQuerySuccess(avFriendship);
            } else {
                onQueryError(e);
            }
        }
    };
    private FriendshipCallback friendshipCallback;

    public FollowFragmentPresenter(FollowFragment target) {
        super(target);
        mViewHolders.put(ItemType.ITEM_COMMON, FollowItemViewHolder.class);
    }

    @Override
    public void initFragment() {
        friendshipCallback = new FriendshipCallback(this);
        mAdapter = new TRecycleViewAdapter(getContext(), mViewHolders, mTAdapterItems);
        mAdapter.setItemEventListener(this);
        mTarget.setAdapter(mAdapter);
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }


    public void refreshData() {
        AVFriendshipQuery query = AVUser.friendshipQuery(AVUser.getCurrentUser().getObjectId());
        FragmentType type = mTarget.getType();
        switch (type) {
            case FolloweeFragment:
                query.include("followee");
                break;
            case FollowerFragment:
                query.include("follower");
                break;
        }
        query.getInBackground(friendshipCallback.getCallback());
    }

    private void onQuerySuccess(AVFriendship friendship) {
        List<AVUser> list = null;
        switch (mTarget.getType()) {
            case FolloweeFragment:
                list = friendship.getFollowees();
                LeanFriendshipCache.getInstance().setFolloweeCache(list);
                break;
            case FollowerFragment:
                list = friendship.getFollowers();
                LeanFriendshipCache.getInstance().setFollowerCache(list);
                break;
        }
        if (list != null && list.size() > 0) {
            mTAdapterItems.clear();
            for (AVUser user : list) {
                mTAdapterItems.add(new FollowViewHolderItem(user));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onQueryError(AVException e) {
        ToastUtil.makeShortToast(e.getMessage());
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (eventName) {
            case ItemEventListener.clickEventName:
                UserDataActivity.start(mTarget.getActivity(),
                        mTAdapterItems.get(position).getDataModel());
                break;
        }
        return true;
    }

    @Override
    public void onLeanError(String cbName, AVException e) {
        if (cbName.equals(FriendshipCallback.class.getName()))
            onQueryError(e);
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        if (cbName.equals(FriendshipCallback.class.getName()))
            onQuerySuccess((AVFriendship) values[0]);
    }
}

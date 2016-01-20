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
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowFragment;
import com.xue.siu.module.follow.viewholder.FollowItemViewHolder;
import com.xue.siu.module.follow.viewholder.item.FollowViewHolderItem;
import com.xue.siu.module.follow.viewholder.item.ItemType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/18.
 */
public class FollowFragmentPresenter extends BaseFragmentPresenter<FollowFragment> implements OnRefreshListener,
        ItemEventListener {
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();
    private List<TAdapterItem> mTAdapterItems = new ArrayList<>();
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

    public FollowFragmentPresenter(FollowFragment target) {
        super(target);
        mViewHolders.put(ItemType.ITEM_COMMON, FollowItemViewHolder.class);
    }

    @Override
    public void initFragment() {
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
        query.getInBackground(mFriendshipCallback);
    }

    private void onQuerySuccess(AVFriendship friendship) {
        List<LeanUser> list = null;
        switch (mTarget.getType()) {
            case FolloweeFragment:
                list = friendship.getFollowees();
                break;
            case FollowerFragment:
                list = friendship.getFollowers();
                break;
        }
        if (list != null) {
            mTAdapterItems.clear();
            for (LeanUser user : list) {
                mTAdapterItems.add(new FollowViewHolderItem(user));
            }
            if (mAdapter == null) {
                mAdapter = new TRecycleViewAdapter(getContext(), mViewHolders, mTAdapterItems);
                mAdapter.setItemEventListener(this);
                mTarget.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void onQueryError(AVException e) {
        ToastUtil.makeShortToast(e.getMessage());
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (eventName) {
            case ItemEventListener.clickEventName:
                //跳转到聊天界面
                break;
        }
        return true;
    }
}

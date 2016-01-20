package com.xue.siu.module.follow.presenter;

import android.util.SparseArray;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnLoadMoreListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnRefreshListener;
import com.xue.siu.common.util.HandleUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowFragment;
import com.xue.siu.module.follow.model.UserVO;
import com.xue.siu.module.follow.viewholder.FollowItemViewHolder;
import com.xue.siu.module.follow.viewholder.item.FollowViewHolderItem;
import com.xue.siu.module.follow.viewholder.item.ItemType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/18.
 */
public class FollowFragmentPresenter extends BaseFragmentPresenter<FollowFragment> implements OnRefreshListener,
        OnLoadMoreListener {
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
        for (int i = 0; i < 10; i++) {
            UserVO userVO = new UserVO();
            userVO.setName(mTarget.getType().toString() + i);
            mTAdapterItems.add(new FollowViewHolderItem(userVO));
        }
        mAdapter = new TRecycleViewAdapter(mTarget.getActivity(), mViewHolders, mTAdapterItems);
        mTarget.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        HandleUtil.doDelay(new Runnable() {
            @Override
            public void run() {
                mTarget.refreshFinish();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {

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
        List<AVUser> list = null;
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
            for (AVUser user : list) {

            }
        }
    }

    private void onQueryError(AVException e) {

    }
}

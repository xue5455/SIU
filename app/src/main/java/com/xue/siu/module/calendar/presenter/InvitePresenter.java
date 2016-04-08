package com.xue.siu.module.calendar.presenter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htswiperefreshrecyclerview.OnRefreshListener;
import com.xue.siu.R;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.calendar.activity.InviteActivity;
import com.xue.siu.module.calendar.viewholder.InviteAttenderViewHolder;
import com.xue.siu.module.calendar.viewholder.item.InviteAttenderViewHolderItem;
import com.xue.siu.module.calendar.viewholder.item.InviteItemType;
import com.xue.siu.module.follow.callback.FriendshipCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by XUE on 2016/4/6.
 */
public class InvitePresenter extends BaseActivityPresenter<InviteActivity> implements
        View.OnClickListener, AVIMResultListener, OnRefreshListener {
    private HashSet<String> selectedUsers = new HashSet<>();
    private TRecycleViewAdapter adapter;
    private final SparseArray<Class<? extends TRecycleViewHolder>> viewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(InviteItemType.ITEM_TYPE_USER, InviteAttenderViewHolder.class);
                }
            };
    private List<TAdapterItem<InviteAttenderViewHolder.UserWrapper>> adapterItems = new ArrayList<>();

    public InvitePresenter(InviteActivity target) {
        super(target);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        List<AVUser> selected = (List<AVUser>) mTarget.getIntent().getSerializableExtra(mTarget.KEY_USER);
        for (AVUser user : selected) {
            selectedUsers.add(user.getUsername());
        }
    }

    @Override
    protected void initActivity() {
        adapter = new TRecycleViewAdapter(mTarget, viewHolders, adapterItems);
        mTarget.setAdapter(adapter);
        DialogUtil.showProgressDialog(mTarget, false);
        onRefresh();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLeanError(String cbName, AVException e) {
        DialogUtil.hideProgressDialog(mTarget);
        if (TextUtils.equals(cbName, FriendshipCallback.class.getName())) {
            ToastUtil.makeShortToast(e.getMessage());
        }
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        DialogUtil.hideProgressDialog(mTarget);
        if (TextUtils.equals(cbName, FriendshipCallback.class.getName())) {
            transformData((AVFriendship) values[0]);
        }
    }

    private void transformData(AVFriendship friendship) {
        mTarget.refreshDone();
        List<AVUser> users = friendship.getFollowees();
        adapterItems.clear();
        for (AVUser user : users) {
            boolean selected = selectedUsers.contains(user.getUsername());
            InviteAttenderViewHolder.UserWrapper wrapper =
                    new InviteAttenderViewHolder.UserWrapper(selected, user);
            adapterItems.add(new InviteAttenderViewHolderItem(wrapper));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        AVFriendshipQuery query = AVUser.friendshipQuery(AVUser.getCurrentUser().getObjectId());
        query.include("followee");
        query.getInBackground(new FriendshipCallback(this).getCallback());
    }
}

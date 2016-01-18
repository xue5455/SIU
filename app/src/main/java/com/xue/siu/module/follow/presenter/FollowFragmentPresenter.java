package com.xue.siu.module.follow.presenter;

import android.content.Context;
import android.util.SparseArray;

import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.activity.FollowFragment;
import com.xue.siu.module.follow.adapter.FollowListAdapter;
import com.xue.siu.module.follow.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/18.
 */
public class FollowFragmentPresenter extends BaseFragmentPresenter<FollowFragment> {
    private FollowListAdapter mAdapter;
    private SparseArray<User> mArray;

    public FollowFragmentPresenter(FollowFragment target) {
        super(target);
    }

    @Override
    public void initFragment() {
        mArray = new SparseArray<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName(mTarget.getType().toString() + i);
            mArray.put(i, user);
        }
        mAdapter = new FollowListAdapter(mTarget.getActivity(), mArray);
        mTarget.setAdapter(mAdapter);
    }

}

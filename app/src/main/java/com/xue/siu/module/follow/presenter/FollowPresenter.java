package com.xue.siu.module.follow.presenter;

import android.support.v4.app.Fragment;

import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.follow.activity.FollowActivity;
import com.xue.siu.module.follow.activity.FolloweeFragment;
import com.xue.siu.module.follow.activity.FollowerFragment;
import com.xue.siu.module.follow.adapter.FollowPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowPresenter extends BaseActivityPresenter<FollowActivity> {

    List<Fragment> mFragmentList = new ArrayList<>();


    public FollowPresenter(FollowActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        mFragmentList.add(new FolloweeFragment());
        mFragmentList.add(new FollowerFragment());
        FollowPageAdapter adapter = new FollowPageAdapter(mTarget.getSupportFragmentManager(), mFragmentList);
        mTarget.initAdapter(adapter);
    }
}

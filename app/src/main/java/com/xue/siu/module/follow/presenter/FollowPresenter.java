package com.xue.siu.module.follow.presenter;

import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.follow.Constants;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowActivity;
import com.xue.siu.module.follow.activity.FollowFragment;
import com.xue.siu.module.follow.adapter.FollowPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowPresenter extends BaseActivityPresenter<FollowActivity> implements View.OnClickListener {

    SparseArray<Fragment> mFragmentArray = new SparseArray<>();


    public FollowPresenter(FollowActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        Fragment fragment = new FollowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_TYPE_KEY, FragmentType.FolloweeFragment.toString());
        fragment.setArguments(bundle);
        mFragmentArray.put(0, fragment);
        fragment = new FollowFragment();
        bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_TYPE_KEY, FragmentType.FollowerFragment.toString());
        fragment.setArguments(bundle);
        mFragmentArray.put(1, fragment);
        FollowPageAdapter adapter = new FollowPageAdapter(mTarget.getSupportFragmentManager(), mFragmentArray);
        mTarget.initAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_img:
                mTarget.finish();
                break;
            case R.id.tv_follower:
                mTarget.setCurrentItem(1, true);
                break;
            case R.id.tv_followee:
                mTarget.setCurrentItem(0, true);
                break;
        }
    }
}

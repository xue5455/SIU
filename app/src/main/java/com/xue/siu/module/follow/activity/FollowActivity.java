package com.xue.siu.module.follow.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xue.siu.R;
import com.xue.siu.common.view.viewpager.LineViewPagerIndicator;
import com.xue.siu.common.view.viewpager.ViewPagerWithIndicator;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.follow.presenter.FollowPresenter;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowActivity extends BaseActionBarActivity<FollowPresenter> {
    ViewPagerWithIndicator mFollowPager;
    LineViewPagerIndicator mLineIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_follow);
        initContentView();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new FollowPresenter(this);
    }


    private void initContentView() {
        mFollowPager = findView(R.id.follow_pager);
        mLineIndicator = findView(R.id.line_indicator);
        mFollowPager.setViewPagerIndicator(mLineIndicator);

        mFollowPager.setOffscreenPageLimit(1);
    }

    public void initAdapter(FragmentStatePagerAdapter adapter) {
        mFollowPager.setAdapter(adapter);
        mLineIndicator.setChildCount(adapter.getCount());
    }
}

package com.xue.siu.module.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.view.viewpager.LineViewPagerIndicator;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.news.adapter.NewsPagerAdapter;
import com.xue.siu.module.news.presenter.NewsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/2/15.
 */
public class NewsActivity extends BaseActionBarActivity<NewsPresenter> {
    private ViewPager mPager;
    private LineViewPagerIndicator mIndicator;
    private TextView mTvAction;
    private TextView mTvCalendar;
    private int[] mTextColors = new int[]{R.color.black, R.color.green_normal};

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NewsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_news);
        setNavigationBarBlack();
        setTitle(R.string.na_title);
        initViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        setStatueBarColor(R.color.action_bar_bg);
    }

    private void initViews() {
        mTvAction = findView(R.id.tv_action);
        mTvCalendar = findView(R.id.tv_calendar);
        mPager = findView(R.id.view_pager);
        mIndicator = findView(R.id.view_indicator);
        mPager.addOnPageChangeListener(mIndicator);
        mPager.addOnPageChangeListener(mPresenter);
        List<Fragment> list = new ArrayList<>();
        list.add(new ActionFragment());
        list.add(new CalendarFragment());
        mPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(), list));
        mIndicator.setChildCount(2);
        mIndicator.setLineWidth(ScreenUtil.getDisplayWidth() / 2);
        View view = LayoutInflater.from(this).inflate(R.layout.view_news_right_button, null, false);
        setRightView(view);
        view.setOnClickListener(mPresenter);
        navigationBarContainer.setPadding(navigationBarContainer.getLeft(), navigationBarContainer.getTop(), 0,
                navigationBarContainer.getBottom());
    }

    public void updateText(int position) {
        mTvAction.setTextColor(ResourcesUtil.getColor(mTextColors[1 - position]));
        mTvCalendar.setTextColor(ResourcesUtil.getColor(mTextColors[position]));
    }

    @Override
    protected void initPresenter() {
        mPresenter = new NewsPresenter(this);
    }

    public void setCurrentItem(int position) {
        mPager.setCurrentItem(position);
    }
}

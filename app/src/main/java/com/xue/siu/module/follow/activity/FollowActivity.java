package com.xue.siu.module.follow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.view.letterbar.LetterBar;
import com.xue.siu.common.view.viewpager.LineViewPagerIndicator;
import com.xue.siu.common.view.viewpager.ViewPagerWithIndicator;
import com.xue.siu.module.base.activity.BaseBlankActivity;
import com.xue.siu.module.follow.Constants;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.presenter.FollowPresenter;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowActivity extends BaseBlankActivity<FollowPresenter> implements ViewPager.OnPageChangeListener {
    private ViewPagerWithIndicator mFollowPager;
    private LineViewPagerIndicator mLineIndicator;
    private FragmentType mEnterType;
    private ImageView mBackImg;
    private TextView mFolloweeTv;
    private TextView mFollowerTv;
    private int[] mFollowTextColor = new int[]{R.color.white, R.color.green_normal};
    private LetterBar mLetterBar;
    private ImageView mIvSearch;
    public static void start(Activity activity, FragmentType type) {
        Intent intent = new Intent(activity, FollowActivity.class);
        intent.putExtra(Constants.FRAGMENT_TYPE_KEY, type.toString());
        activity.startActivity(intent);
    }

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
        mBackImg = findView(R.id.nav_left_img);
        mBackImg.setImageResource(R.drawable.selector_back_btn_navigationbar_white);
        mBackImg.setOnClickListener(mPresenter);
        mFollowPager = findView(R.id.follow_pager);
        mLineIndicator = findView(R.id.line_indicator);
        mLineIndicator.setLineWidth(ResourcesUtil.getDimenPxSize(R.dimen.fa_tv_width));
        mFollowPager.setViewPagerIndicator(mLineIndicator);
        mFollowPager.setOffscreenPageLimit(1);
        mFollowPager.addOnPageChangeListener(this);
        mFolloweeTv = findView(R.id.tv_followee);
        mFollowerTv = findView(R.id.tv_follower);
        mFolloweeTv.setOnClickListener(mPresenter);
        mFollowerTv.setOnClickListener(mPresenter);
        mLetterBar = findView(R.id.view_letter);
        mIvSearch = findView(R.id.iv_search);
        mIvSearch.setOnClickListener(mPresenter);
    }

    public void initAdapter(FragmentStatePagerAdapter adapter) {
        mFollowPager.setAdapter(adapter);
        mLineIndicator.setChildCount(adapter.getCount());
        router();
    }

    private void router() {
        initType();
        switch (mEnterType) {
            case FollowerFragment:
                setCurrentItem(1, false);
                break;
            case FolloweeFragment:
                setCurrentItem(0, false);
                mFolloweeTv.setTextColor(ResourcesUtil.getColor(mFollowTextColor[1]));
                break;
        }
    }

    private void initType() {
        String type = getIntent().getStringExtra(Constants.FRAGMENT_TYPE_KEY);
        if (type != null) {
            mEnterType = FragmentType.getType(type);
        }
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        mFollowPager.setCurrentItem(position, smoothScroll);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mFolloweeTv.setTextColor(ResourcesUtil.getColor(mFollowTextColor[1 - position]));
        mFollowerTv.setTextColor(ResourcesUtil.getColor(mFollowTextColor[position]));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            //显示SideBar
            mLetterBar.setVisibility(View.VISIBLE);
        } else {
            //隐藏SideBar
            mLetterBar.setVisibility(View.GONE);
        }
    }
}

package com.xue.siu.common.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.AbsListView;

/**
 * Created by XUE on 2016/1/16.
 */
public class ViewPagerWithIndicator extends ViewPager implements ViewPager.OnPageChangeListener {


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (mViewPagerIndicator != null)
            mViewPagerIndicator.onScrolled(position, positionOffset, positionOffsetPixels);
    }

    public interface ViewPagerIndicator {
        void onScrolled(int position, float positionOffset, int positionOffsetPixels);
    }

    private ViewPagerIndicator mViewPagerIndicator;

    public ViewPagerWithIndicator(Context context) {
        super(context);
    }

    public ViewPagerWithIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(this);
    }

    public void setViewPagerIndicator(ViewPagerIndicator viewPagerIndicator) {
        this.mViewPagerIndicator = viewPagerIndicator;
    }


}

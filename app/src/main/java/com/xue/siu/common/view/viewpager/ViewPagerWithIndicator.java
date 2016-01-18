package com.xue.siu.common.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.AbsListView;

/**
 * Created by XUE on 2016/1/16.
 */
public class ViewPagerWithIndicator extends ViewPager{

    OnPageChangeListener mListener;

    public interface ViewPagerIndicator {
        void onScrolled(int position, float positionOffset, int positionOffsetPixels);
    }

    private ViewPagerIndicator mViewPagerIndicator;

    public ViewPagerWithIndicator(Context context) {
        super(context);
    }

    public ViewPagerWithIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mViewPagerIndicator != null)
                    mViewPagerIndicator.onScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        addOnPageChangeListener(mListener);
    }

    public void setViewPagerIndicator(ViewPagerIndicator viewPagerIndicator) {
        this.mViewPagerIndicator = viewPagerIndicator;
    }


}

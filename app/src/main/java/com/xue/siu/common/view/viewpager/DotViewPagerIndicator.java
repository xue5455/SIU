package com.xue.siu.common.view.viewpager;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XUE on 2016/2/1.
 */
public class DotViewPagerIndicator extends View implements ViewPager.OnPageChangeListener {
    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private PagerAdapter mAdapter;
    private int mDotDividerWidth = 30;
    /**
     * item count of the view pager
     */
    private int mPageCount;
    /* the radius of the dots */
    private int mRadius = 10;
    /* current item of view pager */
    private int mCurrentItem;
    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mPageCount = mAdapter.getCount();
            invalidate();
        }
    };

    public DotViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float x = mViewWidth / 2 - (mPageCount * 2 * mRadius + (mPageCount - 1) * mDotDividerWidth) / 2;
        float y = mViewHeight / 2;
        for (int i = 0; i < mPageCount; i++) {
            if (i == mCurrentItem)
                mPaint.setColor(Color.BLACK);
            else
                mPaint.setColor(Color.GRAY);
            canvas.drawCircle(x, y, mRadius, mPaint);
            x += 2 * mRadius + mDotDividerWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }

    public void setAdapter(PagerAdapter adapter) {
        this.mAdapter = adapter;
        mPageCount = mAdapter.getCount();
        mAdapter.registerDataSetObserver(mObserver);
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

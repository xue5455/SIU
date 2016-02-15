package com.xue.siu.common.view.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;

/**
 * Created by XUE on 2016/1/16.
 */
public class LineViewPagerIndicator extends View implements ViewPager.OnPageChangeListener {
    private int mChildCount = 1;//page数量
    private int mLineWidth;
    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private int mOffset = 0;
    private int mCurrentItem = 0;
    private int mDividerWidth = 0;

    public LineViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentItem = position;
        mOffset = mCurrentItem * mDividerWidth + (int) (mDividerWidth * positionOffset);
        invalidate();
    }

    public void setChildCount(int count) {
        mChildCount = count;
        if (mViewWidth != 0) {
            mDividerWidth = (mViewWidth - mLineWidth) / (mChildCount - 1);
            invalidate();
        }
    }

    private void setup() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        if (isInEditMode())
            return;
        mPaint.setColor(ResourcesUtil.getColor(R.color.green_normal));
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        if (mChildCount - 1 > 0)
            mDividerWidth = (mViewWidth - mLineWidth) / (mChildCount - 1);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mOffset, 0, mOffset + mLineWidth, mViewHeight, mPaint);
    }


    public void setLineWidth(int width) {
        mLineWidth = width;
    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

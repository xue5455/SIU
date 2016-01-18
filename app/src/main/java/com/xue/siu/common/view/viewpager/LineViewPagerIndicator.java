package com.xue.siu.common.view.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;

/**
 * Created by XUE on 2016/1/16.
 */
public class LineViewPagerIndicator extends View implements ViewPagerWithIndicator.ViewPagerIndicator {
    private int mChildCount = 1;//page数量
    private int mLineWidth;
    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private int mOffset = 0;

    public LineViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    @Override
    public void onScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mOffset = position * mViewWidth + positionOffsetPixels;
        invalidate();
    }

    public void setChildCount(int count) {
        mChildCount = count;
        if (mViewWidth != 0) {
            mLineWidth = mViewWidth / mChildCount;
            invalidate();
        }
    }

    private void setup() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ResourcesUtil.getColor(R.color.green_normal));
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        mViewWidth = getMeasuredWidth();
        mLineWidth = mViewWidth / mChildCount;
        mViewHeight = getMeasuredHeight();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mOffset, 0, mOffset + mLineWidth, mViewHeight, mPaint);
    }
}

package com.xue.siu.module.chat.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.MsgType;

/**
 * Created by XUE on 2016/1/15.
 */
public class BubbleDrawable extends Drawable {
    private Paint mPaint;
    private Path mPath;
    private Rect mRect;
    private float mRadius = ResourcesUtil.getDimenPxSize(R.dimen.chat_bubble_radius);
    private int mLeftPadding = ResourcesUtil.getDimenPxSize(R.dimen.chat_bubble_left_padding);
    private int mOpenLength = ResourcesUtil.getDimenPxSize(R.dimen.chat_bubble_open_length);
    private int mPortraitSize = ResourcesUtil.getDimenPxSize(R.dimen.chat_portrait_size);
    private float x1, y1, x2, y2, x3, y3;
    private MsgDirection mDirection;

    public BubbleDrawable(MsgDirection direction) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mRect = new Rect();
        mDirection = direction;
    }
    public BubbleDrawable(MsgDirection direction,int color){
        this(direction);
        mPaint.setColor(color);
    }
    @Override
    public void setBounds(int l, int t, int r, int b) {
        super.setBounds(l, t, r, b);
        mRect.set(l, t, r, b);
        updatePath();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    private void updatePath() {
        switch (mDirection) {
            case IN:
                generateInPath();
                break;
            case OUT:
                generateOutPath();
                break;
        }

    }

    private void generateInPath() {
        x1 = 0;
        y1 = mPortraitSize / 2;
        x2 = mLeftPadding;
        y2 = y1 - mOpenLength / 2;
        x3 = mLeftPadding;
        y3 = y2 + mOpenLength;
        mPath.reset();
        mPath.addRoundRect(new RectF(mLeftPadding, 0, mRect.right, mRect.bottom), mRadius, mRadius, Path.Direction.CW);
        mPath.moveTo(x1, y1);
        mPath.lineTo(x2, y2);
        mPath.moveTo(x1, y1);
        mPath.lineTo(x3, y3);
        mPath.lineTo(x2, y2);
        mPath.close();
    }

    private void generateOutPath() {
        x1 = mRect.right;
        y1 = mPortraitSize / 2;
        x2 = x1 - mLeftPadding;
        y2 = y1 - mOpenLength / 2;
        x3 = x1 - mLeftPadding;
        y3 = y2 + mOpenLength;
        mPath.reset();
        mPath.addRoundRect(new RectF(0, 0, mRect.right - mLeftPadding, mRect.bottom), mRadius, mRadius, Path.Direction.CW);
        mPath.moveTo(x1, y1);
        mPath.lineTo(x2, y2);
        mPath.moveTo(x1, y1);
        mPath.lineTo(x3, y3);
        mPath.lineTo(x2, y2);
        mPath.close();
    }

    @Override
    public int getIntrinsicHeight() {
        return ResourcesUtil.getDimenPxSize(R.dimen.chat_text_min_height);
    }

    @Override
    public int getIntrinsicWidth() {
        return ResourcesUtil.getDimenPxSize(R.dimen.chat_text_min_width);
    }
}

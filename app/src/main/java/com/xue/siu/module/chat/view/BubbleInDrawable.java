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

/**
 * Created by XUE on 2016/1/15.
 */
public class BubbleInDrawable extends Drawable {
    private Paint mPaint;
    private Path mPath;
    private Rect mRect;
    private PorterDuffXfermode mXfermode;

    public BubbleInDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mRect = new Rect();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mPaint.setXfermode(mXfermode);
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

    int leftMargin = 25;
    int topMargin = 10;
    int yOffset = 5;

    private void updatePath() {
        mPath.reset();
        mPath.addRoundRect(new RectF(leftMargin, 0, mRect.right, mRect.bottom), 30, 30, Path.Direction.CW);
        mPath.moveTo(0, 15);
        mPath.quadTo(leftMargin / 2 - 3, 22, leftMargin, 30);
        mPath.moveTo(0, 15);
        mPath.quadTo(leftMargin / 2 - 3, 42, leftMargin, 55);
        mPath.lineTo(leftMargin, 30);
        mPath.close();
    }
}

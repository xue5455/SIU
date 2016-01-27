package com.xue.siu.module.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

/**
 * Created by XUE on 2016/1/27.
 */
public class BubbleLayout extends FrameLayout {

    private PorterDuffXfermode mXfermode;
    private Paint mPaint;

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(mXfermode);
        mPaint.setColor(Color.BLACK);
    }


    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, mPaint);
    }
}

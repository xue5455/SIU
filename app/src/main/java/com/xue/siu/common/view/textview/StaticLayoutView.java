package com.xue.siu.common.view.textview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xue.siu.R;

/**
 * Created by XUE on 2016/2/3.
 */
public class StaticLayoutView extends View {

    private Layout layout = null;

    private int width;
    private int height;
    private int minimumWidth;
    private int minimumHeight;
    private int paddingTop, paddingBottom, paddingLeft, paddingRight;

    public StaticLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public StaticLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(
                attrs,
                R.styleable.StaticLayoutView,
                0, 0);
        minimumHeight = a.getDimensionPixelSize(R.styleable.StaticLayoutView_minimum_height, 0);
        minimumWidth = a.getDimensionPixelSize(R.styleable.StaticLayoutView_minimum_width, 0);
        paddingTop = a.getDimensionPixelSize(R.styleable.StaticLayoutView_static_padding_top, 0);
        paddingBottom = a.getDimensionPixelSize(R.styleable.StaticLayoutView_static_padding_bottom, 0);
        paddingLeft = a.getDimensionPixelSize(R.styleable.StaticLayoutView_static_padding_left, 0);
        paddingRight = a.getDimensionPixelSize(R.styleable.StaticLayoutView_static_padding_right, 0);
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if (this.layout.getWidth() != width || this.layout.getHeight() != height) {
            width = this.layout.getWidth();
            height = this.layout.getHeight();
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (layout != null) {
            float x = paddingLeft;
            float y = layout.getLineCount() == 1 ? paddingTop + layout.getHeight() / 2 : paddingTop;
            canvas.translate(x, y);
            layout.draw(canvas, null, null, 0);
        }
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (layout != null) {
            width = Math.max(layout.getWidth() + paddingLeft + paddingRight, minimumWidth);
            height = Math.max(layout.getHeight() + paddingTop + paddingBottom, minimumHeight);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}


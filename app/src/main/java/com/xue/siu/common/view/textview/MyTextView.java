package com.xue.siu.common.view.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xue.siu.common.util.StaticLayoutManager;

/**
 * Created by XUE on 2016/2/4.
 */
public class MyTextView extends TextView {
    Layout layout;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (layout == null) {
            super.onDraw(canvas);
            layout = getLayout();
            StaticLayoutManager.getInstance().saveLayout(getText().toString(), layout);
        } else {
            canvas.save();
            layout.draw(canvas);
            canvas.restore();
        }
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}

package com.xue.siu.common.view.datepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/6/15.
 */
class MyLayout extends LinearLayout {
    private LayoutChangeListener listener;

    void addOnLayoutChangeListener(LayoutChangeListener on) {
        listener = on;
    }

    MyLayout(Context context) {
        this(context, null);
    }

    MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (listener != null) {
            listener.onLayoutChange();
        }
    }

    protected void updateChild(){
        for(int i=0;i<getChildCount();i++)
            getChildAt(i).invalidate();
    }
}

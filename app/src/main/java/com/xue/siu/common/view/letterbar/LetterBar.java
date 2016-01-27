package com.xue.siu.common.view.letterbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ToastUtil;

/**
 * Created by XUE on 2016/1/21.
 */
public class LetterBar extends LinearLayout implements View.OnClickListener {

    private int mViewWidth;
    private int mViewHeight;
    private int mLetterHeight;
    private String mLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int mLetterCount = mLetters.length();

    public LetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mViewHeight == 0) {
            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();
            mLetterHeight = mViewHeight / mLetterCount;
            fillBar();
        }
    }

    private void fillBar() {
        ColorStateList list = new ColorStateList(new int[][]{PRESSED_ENABLED_FOCUSED_STATE_SET, ENABLED_STATE_SET}, new int[]{Color.RED, Color.BLACK});
        for (int i = 0; i < mLetterCount; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(list);
            textView.setOnClickListener(this);
            LayoutParams params = new LayoutParams(mViewWidth, mLetterHeight);
            textView.setText(String.valueOf(mLetters.charAt(i)));
            addView(textView, params);
        }
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView)v;
        String c = textView.getText().toString();
        ToastUtil.makeShortToast(c);
    }
}

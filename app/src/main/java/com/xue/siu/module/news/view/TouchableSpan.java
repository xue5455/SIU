package com.xue.siu.module.news.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;

/**
 * Created by XUE on 2016/4/14.
 */
public abstract class TouchableSpan extends ClickableSpan {

    private boolean mIsPressed;
    private int mPressedBackgroundColor;
    private int mNormalTextColor;
    private int mPressedTextColor;

    public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = mIsPressed ? mPressedBackgroundColor : ResourcesUtil.getColor(R.color.grey_f4);
        ds.setUnderlineText(false);
    }

}

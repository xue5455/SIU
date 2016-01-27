package com.xue.siu.module.chat.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.xue.siu.R;
import com.xue.siu.common.util.KeyboardUtil;

/**
 * Created by XUE on 2016/1/26.
 */
public class EditContainer extends LinearLayout implements TextWatcher {

    EditText mEtMsg;

    Button mBtnSend;

    Button mBtnEmoji;

    Button mBtnPlus;

    ViewPager mVpEmoji;

    GridView mGvMenu;

    public EditContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mEtMsg = findView(R.id.et_msg);
        mBtnSend = findView(R.id.btn_send);
        mBtnEmoji = findView(R.id.btn_emoji);
        mBtnPlus = findView(R.id.btn_plus);
        mVpEmoji = findView(R.id.vp_emoji);
        mGvMenu = findView(R.id.gv_plus);
        mEtMsg.addTextChangedListener(this);
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = s.toString().length();
        if (length > 0) {
            showSendBtn();
        } else {
            hideSendBtn();
        }
    }


    public void hideKeyboard() {
        KeyboardUtil.hidekeyboard(mEtMsg);
    }

    public void showInputMethod() {
        mEtMsg.requestFocus();
        KeyboardUtil.showkeyboard(mEtMsg);
    }

    public void hideMenuAndEmoji() {
        mVpEmoji.setVisibility(View.GONE);
        mGvMenu.setVisibility(View.GONE);
    }

    public void showAdditionalMenu() {
        mGvMenu.setVisibility(View.VISIBLE);
        mVpEmoji.setVisibility(View.GONE);
    }

    public void hideMenu() {
        mGvMenu.setVisibility(View.GONE);
    }

    public void hideEmoji() {
        mVpEmoji.setVisibility(View.GONE);
    }

    public void showEmoji() {
        mVpEmoji.setVisibility(View.VISIBLE);
        mGvMenu.setVisibility(View.GONE);
    }

    public void showSendBtn() {
        mBtnSend.setVisibility(View.VISIBLE);
    }

    public void hideSendBtn() {
        mBtnSend.setVisibility(View.GONE);
    }

    public boolean isEmojiVisible() {
        return mVpEmoji.getVisibility() == View.VISIBLE;
    }

    public boolean isMenuVisible() {
        return mGvMenu.getVisibility() == View.VISIBLE;
    }

    public void updateMenuHeight(int height) {
        LayoutParams params = (LayoutParams) mGvMenu.getLayoutParams();
        params.height = height;
        mGvMenu.setLayoutParams(params);

        params = (LayoutParams) mVpEmoji.getLayoutParams();
        params.height = height;
        mVpEmoji.setLayoutParams(params);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mBtnSend.setOnClickListener(listener);
        mBtnEmoji.setOnClickListener(listener);
        mBtnPlus.setOnClickListener(listener);
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener listener) {
        mEtMsg.setOnTouchListener(listener);
    }


    public String getContent() {
        return mEtMsg.getText().toString();
    }
}

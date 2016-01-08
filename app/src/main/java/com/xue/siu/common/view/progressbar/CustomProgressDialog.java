package com.xue.siu.common.view.progressbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xue.siu.R;

/**
 * Created by DING on 15/11/27.
 */
public class CustomProgressDialog extends Dialog {
    private TextView tvLoding;
    private ArcProgressbar pgLoading;
    private boolean isCancelable = false;
    public CustomProgressDialog(Context context) {
        this(context, R.style.CustomProgressDialog);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.view_loading);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setWindowAnimations(R.style.popWindowAnimBottom);
        tvLoding = (TextView) findViewById(R.id.loading_text);
        pgLoading = (ArcProgressbar) findViewById(R.id.loading_image);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pgLoading.stopRotate();
            }
        });
    }

    public void setMessage(String message) {
        tvLoding.setVisibility(View.VISIBLE);
        tvLoding.setText(message);
    }

    public void setMessage(@StringRes int messageResId) {
        tvLoding.setVisibility(View.VISIBLE);
        tvLoding.setText(messageResId);
    }

    public void hideMessage() {
        tvLoding.setVisibility(View.GONE);
    }

    public void startRotate() {
        pgLoading.startRotate();
    }

    public void stopRotate() {
        pgLoading.stopRotate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
            stopRotate();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        isCancelable = flag;
    }

    public boolean isCancelable(){
        return isCancelable;
    }
}

package com.netease.hearttouch.htimagepicker.view.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;

/**
 * Created by zyl06 on 2/19/16.
 */
public class BaseActivity extends FragmentActivity {
    protected FrameLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blank);
        initContentView();
    }

    private void initContentView(){
        mContentView = (FrameLayout)findViewById(R.id.content_view);
    }

}
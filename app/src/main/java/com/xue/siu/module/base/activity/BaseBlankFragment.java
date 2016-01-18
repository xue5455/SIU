package com.xue.siu.module.base.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;

/**
 * Created by XUE on 2016/1/9.
 */
public abstract class BaseBlankFragment<T extends BaseFragmentPresenter> extends BaseFragment<T>{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initContentView();
        return mRootView;
    }

    @Override
    protected void inflateRootView(LayoutInflater inflater) {
        mRootView = (FrameLayout)inflater.inflate(R.layout.activity_blank, null);
    }
    private void initContentView(){
        mContentView = (FrameLayout) mRootView.findViewById(R.id.content_view);
    }
}

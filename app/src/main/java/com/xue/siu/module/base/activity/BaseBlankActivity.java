package com.xue.siu.module.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BasePresenter;

/**
 * Created by XUE on 2015/12/9.
 */
public abstract class BaseBlankActivity<T extends BasePresenter> extends BaseActivity<T> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        initContentView();
    }

    private void initContentView() {
        mRootView =  findView(R.id.root_view);
        mContentView = findView(R.id.content_view);
    }

    /**
     * Inflate a content view for the activity.
     *
     * @param resId ID for an XML layout resource as the content view
     */
    public void setRealContentView(@LayoutRes int resId) {
        getLayoutInflater().inflate(resId, mContentView);
    }
}

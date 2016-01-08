package com.xue.siu.module.base.presenter;

import com.xue.siu.module.base.activity.BaseActivity;

/**
 * Created by XUE on 2015/12/9.
 */
public abstract class BaseActivityPresenter<T extends BaseActivity>
        extends BasePresenter<T> {
    private boolean mIsInitialized = false;

    public BaseActivityPresenter(T target) {
        super(target);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsInitialized)
            try {
                initActivity();
            } finally {
                mIsInitialized = true;
            }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract void initActivity();
}

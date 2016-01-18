package com.xue.siu.module.base.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xue.siu.module.base.activity.BaseFragment;

/**
 * Created by XUE on 2015/12/9.
 */
public abstract class BaseFragmentPresenter<T extends BaseFragment>
        extends BasePresenter<T> {
    private boolean mIsInitialized = false;

    public BaseFragmentPresenter(T target) {
        super(target);
    }

    public Context getContext() {
        return mTarget.getActivity();
    }

    public void onAttach() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void onCreateView() {
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsInitialized) {
            initFragment();
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

    public void onDestroyView() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onDetach() {
    }

    public abstract void initFragment();
}

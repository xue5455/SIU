package com.xue.siu.module.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xue.siu.module.base.presenter.BaseFragmentPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2015/12/9.
 */
public abstract class BaseFragment<T extends BaseFragmentPresenter> extends Fragment {
    protected T mPresenter;
    protected FrameLayout mRootView;
    protected WeakReference<View> mRootViewRef;
    protected FrameLayout mContentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter != null) {
            mPresenter.onAttach();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateRootView(inflater);
        if (mPresenter != null) {
            mPresenter.onCreateView();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onActivityCreated(savedInstanceState);
        }
    }

    /**
     * Inflate a content view for the activity.
     *
     * @param resId ID for an XML layout resource as the content view
     */
    public void setRealContentView(@LayoutRes int resId) {
        getActivity().getLayoutInflater().inflate(resId, mContentView);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }

    abstract protected void initPresenter();


    protected void inflateRootView(LayoutInflater inflater) {
    }


}

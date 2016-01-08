package com.xue.siu.module.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.module.base.presenter.BasePresenter;

/**
 * Created by XUE on 2015/12/9.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected T mPresenter;
    SystemBarTintManager mTintManager;
    protected ViewGroup mRootView;
    protected ViewGroup mContentView;

    /**
     * 用于监听inflater view
     */
    protected LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
        setStatueBarColor(R.color.black);

        initPresenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    /**
     * Inflate a content view for the activity.
     *
     * @param resId ID for an XML layout resource as the content view
     */
    public void setRealContentView(@LayoutRes int resId) {
        getLayoutInflater().inflate(resId, mContentView);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {//MENU键
            //监控/拦截菜单键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    abstract protected void initPresenter();

    public void setStatueBarColor(@ColorRes int colorResId) {
        int color = ResourcesUtil.getColor(colorResId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(color);
        } else {
            // 创建状态栏的管理实例
            if (mTintManager == null) {
                mTintManager = new SystemBarTintManager(this);
                // 激活状态栏设置
                mTintManager.setStatusBarTintEnabled(true);
            }
            mTintManager.setTintColor(color);
        }
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }
}

package com.xue.siu.avim.base;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.xue.siu.module.base.presenter.BasePresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/3/7.
 */
public abstract class BaseSaveCallback<T extends BasePresenter> extends SaveCallback {
    private WeakReference<T> mPresenterRef;
    /*
     * callback是否依赖于activity
     */
    abstract protected boolean isRelatedToActivity();

    public BaseSaveCallback(T presenter) {
        initPresenter(presenter);
    }

    private void initPresenter(T presenter) {
        mPresenterRef = new WeakReference<T>(presenter);
    }

    abstract protected void onAVIMError(T presenter, AVException e);

    abstract protected void onAVIMSuccess(T presenter);

    @Override
    public void done(AVException e) {
        T presenter = mPresenterRef.get();
        if (presenter == null && isRelatedToActivity())
            return;
        if (e == null)
            onAVIMSuccess(presenter);
        else
            onAVIMError(presenter, e);
    }
}

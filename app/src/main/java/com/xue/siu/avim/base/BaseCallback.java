package com.xue.siu.avim.base;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.xue.siu.module.base.presenter.BasePresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/3/7.
 */
public abstract class BaseCallback<T,K>{
    protected T callback;
    private WeakReference<AVIMResultListener> mListenerRef;

    /*
     * callback是否依赖于activity
     */
    abstract protected boolean isRelatedToActivity();

    protected abstract void initCallback();
    abstract protected String getCbName();

    public BaseCallback(AVIMResultListener listener) {
        initPresenter(listener);
        initCallback();
    }

    private void initPresenter(AVIMResultListener listener) {
        mListenerRef = new WeakReference<>(listener);
    }

    protected  void result(K result, AVException e){
        AVIMResultListener listener = mListenerRef.get();
        if (listener == null && isRelatedToActivity())
            return;
        if (e == null)
            listener.onLeanSuccess(getCbName(),result);
        else
            listener.onLeanError(getCbName(),e);
    }

    public T getCallback(){
        return callback;
    }
}

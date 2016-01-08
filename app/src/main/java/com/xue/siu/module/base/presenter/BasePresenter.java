package com.xue.siu.module.base.presenter;

import com.android.volley.Request;
import com.xue.siu.application.ISubscriber;
import com.xue.siu.module.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2015/12/9.
 */
public class BasePresenter<T> implements ISubscriber {
    protected T mTarget;
    private List<Request> requests = new ArrayList<>();

    public BasePresenter(T target) {
        mTarget = target;
    }

    public void onCreate() {
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {

    }

    public void onDestroy() {
    }

    protected void putRequest(Request request) {
        requests.add(request);
    }

    protected void cancelRequests() {
        if(mTarget instanceof BaseActivity){
            //DialogUtil.hideProgressDialog((BaseActivity)mTarget);
        }
        for (Request request : requests) {
            if(request != null){
                request.cancel();
            }
        }
        requests.clear();
    }
}

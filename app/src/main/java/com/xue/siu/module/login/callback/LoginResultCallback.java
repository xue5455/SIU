package com.xue.siu.module.login.callback;

/**
 * Created by XUE on 2016/1/15.
 */
public interface LoginResultCallback {
    void loginSuccess();
    void loginFailed(String errorMsg);
}

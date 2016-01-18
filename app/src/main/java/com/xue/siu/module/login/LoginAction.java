package com.xue.siu.module.login;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.xue.siu.R;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.login.callback.LoginResultCallback;
import com.xue.siu.module.mainpage.activity.MainPageActivity;
import com.xue.siu.module.mainpage.model.TabType;

/**
 * Created by XUE on 2015/12/31.
 */
public class LoginAction {
    private String mUser;
    private String mPsw;
    private LoginResultCallback mLoginResultCallback;
    private LogInCallback<AVUser> logInCallback = new LogInCallback<AVUser>() {
        @Override
        public void done(AVUser avUser, AVException e) {
            if (e == null) {
                mLoginResultCallback.loginSuccess();
            } else {
                switch (e.getCode()) {
                    case AVException.USERNAME_PASSWORD_MISMATCH:
                        mLoginResultCallback.loginFailed(ResourcesUtil.getString(R.string.la_acc_psw_wrong));
                        break;
                    case AVException.TIMEOUT:
                        mLoginResultCallback.loginFailed(ResourcesUtil.getString(R.string.la_time_out));
                        break;
                }
            }
        }
    };

    public LoginAction() {

    }

    public LoginAction(LoginResultCallback loginResultCallback) {
        mLoginResultCallback = loginResultCallback;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        this.mUser = user;
    }

    public String getPsw() {
        return mPsw;
    }

    public void setPsw(String psw) {
        this.mPsw = psw;
    }

    public LoginResultCallback getLoginResultCallback() {
        return mLoginResultCallback;
    }

    public void setLoginResultCallback(LoginResultCallback loginResultCallback) {
        this.mLoginResultCallback = mLoginResultCallback;
    }

    public void login(String user, String psw) {
        if (mLoginResultCallback == null)
            throw new RuntimeException("You must set a callback for login");
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(psw)) {
            mLoginResultCallback.loginFailed(ResourcesUtil.getString(R.string.la_acc_psw_empty));
        }
        setUser(user);
        setPsw(psw);
        AVUser.logInInBackground(user, psw, logInCallback);
    }
}

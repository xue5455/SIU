package com.xue.siu.module.login;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.xue.siu.R;
import com.xue.siu.avim.AVIMClientManager;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.login.callback.LoginResultCallback;
import com.xue.siu.module.mainpage.activity.MainPageActivity;
import com.xue.siu.module.mainpage.model.TabType;
import com.xue.siu.service.LeanManager;

import java.util.Arrays;

/**
 * Created by XUE on 2015/12/31.
 */
public class LoginAction {
    private final String TAG = "LoginAction";
    private String mUser;
    private String mPsw;
    private LoginResultCallback mLoginResultCallback;
    private AVIMClientCallback mAVIMClientCallback = new AVIMClientCallback() {
        @Override
        public void done(AVIMClient avimClient, AVIMException e) {
            LogUtil.d(TAG,"[AVIMClient done]");
            if (e == null) {
                LogUtil.d(TAG, "[Open Client] success");
                mLoginResultCallback.loginSuccess();
            } else {
                LogUtil.e(TAG, "[Open Client] failed");
                mLoginResultCallback.loginFailed("网络错误");
            }
        }
    };
    private LogInCallback<AVUser> logInCallback = new LogInCallback<AVUser>() {
        @Override
        public void done(AVUser avUser, AVException e) {
            if (e == null) {
                LogUtil.d(TAG,"[Start Open Client]");
                AVIMClientManager.getInstance().open(mUser, mAVIMClientCallback);
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
        this.mLoginResultCallback = loginResultCallback;
    }

    public void login(String user, String psw) {
        if (mLoginResultCallback == null)
            throw new RuntimeException("You must set a callback for login");
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(psw)) {
            mLoginResultCallback.loginFailed(ResourcesUtil.getString(R.string.la_acc_psw_empty));
            return;
        }
        setUser(user);
        setPsw(psw);
        AVUser.logInInBackground(user, psw, logInCallback);
    }
}

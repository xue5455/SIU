package com.xue.siu.module.login.presenter;

import android.content.Intent;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.xue.siu.R;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.config.UserInfo;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.login.LoginAction;
import com.xue.siu.module.login.activity.LoginActivity;
import com.xue.siu.module.login.callback.LoginResultCallback;
import com.xue.siu.module.mainpage.activity.MainPageActivity;
import com.xue.siu.module.mainpage.model.TabType;
import com.xue.siu.module.register.activity.RegisterActivity;

/**
 * Created by XUE on 2015/12/11.
 */
public class LoginPresenter extends BaseActivityPresenter<LoginActivity> implements View.OnClickListener,
        LoginResultCallback {
    public static final int REQUEST_CODE_REGISTER = 1;
    private static final String TAG = "LoginPresenter";
    private LoginAction mLoginAction;

    public LoginPresenter(LoginActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
//        login();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    public void login() {
        DialogUtil.showProgressDialog(mTarget, false);
        if (mLoginAction == null)
            mLoginAction = new LoginAction(this);
        mLoginAction.login(mTarget.getAccount(), mTarget.getPassword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogUtil.hideProgressDialog(mTarget);
    }

    @Override
    public void loginSuccess() {
        saveInfo();
        Intent intent = new Intent(mTarget, MainPageActivity.class);
        mTarget.startActivity(intent);
        mTarget.finish();
    }

    private void saveInfo() {
        SharePreferenceHelper.putGlobalString(SharePreferenceC.ACCOUNT, mLoginAction.getUser());
        SharePreferenceHelper.putGlobalString(SharePreferenceC.PASSWORD, mLoginAction.getPsw());
        UserInfo.setIsLogged(true);
        UserInfo.setUserId(mLoginAction.getUser());
        UserInfo.setPassword(mLoginAction.getPsw());
    }

    @Override
    public void loginFailed(String errorMsg) {
        DialogUtil.hideProgressDialog(mTarget);
        ToastUtil.makeShortToast(errorMsg);
    }

    public void register() {
        RegisterActivity.start(mTarget, REQUEST_CODE_REGISTER);
    }
}

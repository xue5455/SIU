package com.xue.siu.module.mainpage.presenter;


import android.content.Intent;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.config.UserInfo;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.login.LoginAction;
import com.xue.siu.module.login.activity.LoginActivity;
import com.xue.siu.module.login.callback.LoginResultCallback;
import com.xue.siu.module.mainpage.activity.MainPageActivity;


/**
 * Created by XUE on 2015/12/9.
 */
public class MainPagePresenter extends BaseActivityPresenter<MainPageActivity> implements LoginResultCallback {


    public MainPagePresenter(MainPageActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        if (!UserInfo.isLogged()) {
            autoLogin();
        }
    }

    private void autoLogin() {
        String user = SharePreferenceHelper.getGlobalString(SharePreferenceC.ACCOUNT, "");
        String pass = SharePreferenceHelper.getGlobalString(SharePreferenceC.PASSWORD, "");
        LoginAction loginAction = new LoginAction(this);
        loginAction.login(user, pass);
    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailed(String errorMsg) {
        if (errorMsg.equals(ResourcesUtil.getString(R.string.la_acc_psw_wrong))) {
            LoginActivity.start(mTarget);
            mTarget.finish();
        } else {
            ToastUtil.makeShortToast(errorMsg);
        }
    }
}

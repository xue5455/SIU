package com.xue.siu.module.login.presenter;

import android.view.View;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.xue.siu.R;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.login.activity.LoginActivity;
import com.xue.siu.module.mainpage.activity.MainPageActivity;
import com.xue.siu.module.mainpage.model.TabType;

/**
 * Created by XUE on 2015/12/11.
 */
public class LoginPresenter extends BaseActivityPresenter<LoginActivity> implements View.OnClickListener {

    private static final String TAG = "LoginPresenter";

    public LoginPresenter(LoginActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        DialogUtil.showProgressDialog(mTarget, false);
        AVUser.logInInBackground(mTarget.getAccount(), mTarget.getPassword(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    MainPageActivity.start(mTarget, TabType.Schedule);
                    mTarget.finish();
                } else {
                    DialogUtil.hideProgressDialog(mTarget);
                    switch (e.getCode()) {
                        case AVException.USERNAME_PASSWORD_MISMATCH:
                            ToastUtil.makeShortToast(R.string.la_acc_psw_wrong);
                            break;
                        case AVException.TIMEOUT:
                            ToastUtil.makeShortToast(R.string.la_time_out);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogUtil.hideProgressDialog(mTarget);
    }

}

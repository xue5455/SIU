package com.xue.siu.module.register.presenter;

import android.text.TextUtils;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.xue.siu.R;
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.register.activity.RegisterActivity;

/**
 * Created by XUE on 2016/1/19.
 */
public class RegisterPresenter extends BaseActivityPresenter<RegisterActivity> implements View.OnClickListener {
    private SignUpCallback mSignUpCallback = new SignUpCallback() {
        @Override
        public void done(AVException e) {
            if (e == null) {
                mTarget.registerDone(true);
            } else {
                DialogUtil.hideProgressDialog(mTarget);
                ToastUtil.makeShortToast(e.getMessage());
            }
        }
    };

    public RegisterPresenter(RegisterActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                onConfirm();
                break;
            case R.id.nav_left_container:
                mTarget.registerDone(false);
                break;
        }
    }

    private void onConfirm() {
        if (TextUtils.isEmpty(mTarget.getAccount())) {
            ToastUtil.makeShortToast(R.string.ra_account_empty);
            return;
        }
        if (TextUtils.isEmpty(mTarget.getPassword())) {
            ToastUtil.makeShortToast(R.string.ra_password_empty);
            return;
        }
        DialogUtil.showProgressDialog(mTarget, false);
        LeanUser.register(mTarget.getAccount(),mTarget.getPassword(),mSignUpCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogUtil.hideProgressDialog(mTarget);
    }

}

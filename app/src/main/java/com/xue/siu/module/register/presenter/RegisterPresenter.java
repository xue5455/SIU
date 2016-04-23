package com.xue.siu.module.register.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

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
public class RegisterPresenter extends BaseActivityPresenter<RegisterActivity> implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static final String GENDER_MALE = "男";
    public static final String GENDER_FEMALE = "女";
    private String gender = GENDER_MALE;
    private SignUpCallback mSignUpCallback = new SignUpCallback() {
        @Override
        public void done(AVException e) {
            if (e == null) {
                mTarget.registerDone(true);
            } else {
                DialogUtil.hideProgressDialog(mTarget);
                onError(e.getCode());
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
            case R.id.register_confirm_btn:
                onConfirm();
                break;
            case R.id.nav_left_container:
                mTarget.registerDone(false);
                break;
        }
    }

    private void onConfirm() {
        if (TextUtils.isEmpty(mTarget.getNickname())) {
            ToastUtil.makeShortToast(R.string.ra_nickname_empty);
            return;
        }
        if (TextUtils.isEmpty(mTarget.getAccount())) {
            ToastUtil.makeShortToast(R.string.ra_account_empty);
            return;
        }
        if (TextUtils.isEmpty(mTarget.getPassword())) {
            ToastUtil.makeShortToast(R.string.ra_password_empty);
            return;
        }
        if (!TextUtils.equals(mTarget.getPasswordConfirm(), mTarget.getPassword())) {
            ToastUtil.makeShortToast(R.string.ra_password_not_same);
            return;
        }
        DialogUtil.showProgressDialog(mTarget, false);
        LeanUser.register(mTarget.getAccount(), mTarget.getPassword(),
                mTarget.getNickname(), gender, mSignUpCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogUtil.hideProgressDialog(mTarget);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.register_male_rb:
                gender = GENDER_MALE;
                break;
            case R.id.register_female_rb:
                gender = GENDER_FEMALE;
                break;
        }
    }

    public void onError(int errorCode) {
        switch (errorCode) {
            case AVException.USERNAME_TAKEN:
                ToastUtil.makeShortToast("帐号已被注册");
                break;
            case AVException.UNKNOWN:
                ToastUtil.makeShortToast("网络错误");
                break;
            default:
                ToastUtil.makeShortToast("网络错误");
        }
    }
}

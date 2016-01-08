package com.xue.siu.module.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseBlankActivity;
import com.xue.siu.module.login.presenter.LoginPresenter;

/**
 * Created by XUE on 2015/12/11.
 */
public class LoginActivity extends BaseBlankActivity<LoginPresenter> {

    /**
     * 帐号
     */
    private EditText editAcc;
    /**
     * 密码
     */
    private EditText editPsw;
    /**
     * 登录按钮
     */
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_login);
        initContentView();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    private void initContentView() {
        editAcc = findView(R.id.edit_acc);
        editPsw = findView(R.id.edit_psw);
        btnLogin = findView(R.id.btn_login);
        btnLogin.setOnClickListener(mPresenter);
    }


    public static void jumpToLoginActivity(Context context, Bundle bundle, String bundleKey) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (bundle != null && bundleKey != null)
            intent.putExtra(bundleKey, bundle);
        context.startActivity(intent);
    }

    public String getAccount() {
        return editAcc.getText().toString();
    }

    public String getPassword() {
        return editPsw.getText().toString();
    }
}

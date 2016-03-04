package com.xue.siu.module.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseBlankActivity;
import com.xue.siu.module.login.presenter.LoginPresenter;
import com.xue.siu.module.register.activity.RegisterActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2015/12/11.
 */
public class LoginActivity extends BaseBlankActivity<LoginPresenter> {

    @Bind(R.id.edit_acc)
    EditText editAcc;
    /**
     * 密码
     */
    @Bind(R.id.edit_psw)
    EditText editPsw;
    /**
     * 登录按钮
     */
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initContentView();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    private void initContentView() {
        btnLogin.setOnClickListener(mPresenter);
        mBtnRegister.setOnClickListener(mPresenter);
    }

    public static void start(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
    public static void start(Context context, Bundle bundle, String bundleKey) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (bundle != null && bundleKey != null)
            intent.putExtra(bundleKey, bundle);
        context.startActivity(intent);
    }

    public String getAccount() {
//        return "xue5455";
        return editAcc.getText().toString();
    }

    public String getPassword() {
//        return "123456";
        return editPsw.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginPresenter.REQUEST_CODE_REGISTER) {
            if (resultCode == RESULT_OK) {
                String account = data.getStringExtra(RegisterActivity.KEY_ACCOUNT);
                String password = data.getStringExtra(RegisterActivity.KEY_PASSWORD);
                editAcc.setText(account);
                editPsw.setText(password);
                mPresenter.login();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

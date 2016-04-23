package com.xue.siu.module.register.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.register.presenter.RegisterPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2016/1/19.
 */
public class RegisterActivity extends BaseActionBarActivity<RegisterPresenter> {
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PASSWORD = "password";

    private EditText etAccount;

    private EditText etPassword;

    private Button btnConfirm;

    private EditText etPasswordConfirm;

    private RadioGroup rgGender;

    private EditText etNickname;

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_register);
        initContentView();
    }

    private void initContentView() {
        etAccount = findView(R.id.register_account_et);
        etNickname = findView(R.id.register_nickname_et);
        rgGender = findView(R.id.register_rg_gender);
        etPassword = findView(R.id.register_password_et);
        etPasswordConfirm = findView(R.id.register_password_confirm_et);
        btnConfirm = findView(R.id.register_confirm_btn);
        btnConfirm.setOnClickListener(mPresenter);
        setNavigationBarBlack();
        navigationBar.setBackButtonClick(mPresenter);
        setTitle(R.string.ra_title);
        rgGender.setOnCheckedChangeListener(mPresenter);
    }

    public String getAccount() {
        return etAccount.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    public String getNickname() {
        return etNickname.getText().toString();
    }

    public String getPasswordConfirm() {
        return etPasswordConfirm.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void registerDone(boolean isSuccessful) {
        if (isSuccessful) {
            Intent intent = new Intent();
            intent.putExtra(KEY_ACCOUNT, getAccount());
            intent.putExtra(KEY_PASSWORD, getPassword());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        registerDone(false);
    }
}

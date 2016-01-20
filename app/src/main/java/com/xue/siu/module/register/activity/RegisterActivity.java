package com.xue.siu.module.register.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
    @Bind(R.id.et_account)
    EditText mEtAcc;
    @Bind(R.id.et_password)
    EditText mEtPsw;
    @Bind(R.id.btn_confirm)
    Button mBtnConfirm;

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
        ButterKnife.bind(this);
        initContentView();
    }

    private void initContentView() {
        mBtnConfirm.setOnClickListener(mPresenter);
        setNavigationBarBlack();
        navigationBar.setBackButtonClick(mPresenter);
        setTitle(R.string.la_title);
    }

    public String getAccount() {
        return mEtAcc.getText().toString();
    }

    public String getPassword() {
        return mEtPsw.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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

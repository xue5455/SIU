package com.xue.siu.module.userpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.util.TextUtil;
import com.xue.siu.common.util.media.FrescoUtil;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.userpage.presenter.MyUserDataPresenter;

/**
 * Created by XUE on 2016/4/4.
 */
public class MyUserDataActivity extends BaseActionBarActivity<MyUserDataPresenter> {
    private SimpleDraweeView sdvAvatar;
    private TextView tvNickname;
    private TextView tvUsername;
    private TextView tvGender;
    private AlertDialog nameDialog;
    private View nameDialogView;
    private EditText etName;
    private Button btnNegative;
    private Button btnPositive;

    public static void startForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), MyUserDataActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_my_user_data);
        initViews();
    }

    private void initViews() {
        setNavigationBarBlack();
        setStatueBarColor(R.color.action_bar_bg);
        setTitle(R.string.muda_title);
        sdvAvatar = findView(R.id.my_data_avatar_sdv);
        tvNickname = findView(R.id.my_data_name_tv);
        tvUsername = findView(R.id.my_data_user_tv);
        tvGender = findView(R.id.my_data_gender_tv);
        setPortrait((String) AVUser.getCurrentUser().get(LeanConstants.PORTRAIT));
        tvUsername.setText(AVUser.getCurrentUser().getUsername());
        setGender((String) AVUser.getCurrentUser().get(LeanConstants.GENDER));
        setNickName((String) AVUser.getCurrentUser().get(LeanConstants.NICK_NAME));
        sdvAvatar.setOnClickListener(mPresenter);
        findView(R.id.my_data_name_ll).setOnClickListener(mPresenter);
        findView(R.id.my_data_gender_ll).setOnClickListener(mPresenter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyUserDataPresenter(this);
    }

    public void setPortrait(String url) {
        if (!TextUtils.isEmpty(url)) {
            int size = ResourcesUtil.getDimenPxSize(R.dimen.upf_avatar_size);
            FrescoUtil.setImageUri(sdvAvatar, url, (float) size);
        }
    }

    public void setNickName(String nickName) {
        tvNickname.setText(nickName);
    }

    public void setGender(String gender) {
        tvGender.setText(gender);
    }

    public void showNameDialog(String name) {
        if (nameDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alert_dialog);
            nameDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_modify_nick_name, null);
            builder.setView(nameDialogView);
            nameDialog = builder.create();
            Window window = nameDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = ScreenUtil.getDisplayWidth() - 2 * ResourcesUtil.getDimenPxSize(R.dimen.alert_dialog_window_margin_left_right);
            window.setAttributes(params);
            nameDialog.setCancelable(true);
            nameDialog.setCanceledOnTouchOutside(false);
            //调用此句话,dialog才会自动弹出键盘
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            etName = (EditText) nameDialogView.findViewById(R.id.my_data_name_et);
            btnNegative = (Button) nameDialogView.findViewById(R.id.my_data_negative_btn);
            btnPositive = (Button) nameDialogView.findViewById(R.id.my_data_positive_btn);
            btnNegative.setOnClickListener(mPresenter);
            btnPositive.setOnClickListener(mPresenter);
            etName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    btnPositive.setEnabled(s.length() != 0);
                }
            });
        }
        etName.setText(name);
        etName.setSelection(name.length());
        nameDialog.show();
    }

    public String getNameEtContent() {
        if (nameDialog == null)
            return null;
        return etName.getText().toString();
    }

    public void closeNameDialog() {
        if (nameDialog == null || !nameDialog.isShowing())
            return;
        nameDialog.dismiss();
    }
}

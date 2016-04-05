package com.xue.siu.module.userpage.presenter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htimagepicker.HTImagePicker;
import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.follow.callback.FriendshipCallback;
import com.xue.siu.module.news.callback.UploadImageCallback;
import com.xue.siu.module.userpage.activity.MyUserDataActivity;
import com.xue.siu.module.userpage.callback.NameSaveCallback;
import com.xue.siu.module.userpage.callback.UserSaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/4/4.
 */
public class MyUserDataPresenter extends BaseActivityPresenter<MyUserDataActivity> implements
        View.OnClickListener,
        HTPickFinishedListener,
        AVIMResultListener {

    private UploadImageCallback uploadImageCallback;
    private UserSaveCallback userSaveCallback;
    private AVFile currentPortraitFile;

    public MyUserDataPresenter(MyUserDataActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_data_avatar_sdv:
                HTImagePicker.getDefault().start(mTarget,
                        null, new ArrayList<PhotoInfo>(),
                        true, 1, false, "全部图片",
                        this);
                break;
            case R.id.my_data_name_ll:
                mTarget.showNameDialog((String) AVUser.getCurrentUser().get(LeanConstants.NICK_NAME));
                break;
            case R.id.my_data_gender_ll:
                break;
            case R.id.my_data_negative_btn:
                mTarget.closeNameDialog();
                break;
            case R.id.my_data_positive_btn:
                saveNickName();
                break;
        }
    }

    @Override
    public void onImagePickFinished(@Nullable AlbumInfo albumInfo, List<PhotoInfo> photos) {
        uploadImage(photos.get(0));
    }

    @Override
    public void onImagePickCanceled() {

    }

    private void uploadImage(PhotoInfo photoInfo) {
        DialogUtil.showProgressDialog(mTarget, true);
        if (uploadImageCallback == null) {
            uploadImageCallback = new UploadImageCallback(this);
            userSaveCallback = new UserSaveCallback(this);
        }
        try {
            currentPortraitFile = AVFile.withAbsoluteLocalPath(AVUser.getCurrentUser().getObjectId(),
                    photoInfo.getAbsolutePath());
            currentPortraitFile.saveInBackground(uploadImageCallback.getCallback());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLeanError(String cbName, AVException e) {
        if (cbName.equals(UploadImageCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            ToastUtil.makeShortToast(R.string.upf_hint_upload_error);
        } else if (cbName.equals(UserSaveCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            ToastUtil.makeShortToast(R.string.upf_hint_upload_error);
        } else if (cbName.equals(NameSaveCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            ToastUtil.makeShortToast(R.string.muda_hint_name_error);
        }
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        if (cbName.equals(UploadImageCallback.class.getName())) {
            onUploadSuccess();
        } else if (cbName.equals(UserSaveCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            mTarget.setPortrait(currentPortraitFile.getUrl());
            mTarget.setResult(Activity.RESULT_OK);
        } else if (cbName.equals(NameSaveCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            mTarget.setNickName((String) AVUser.getCurrentUser().get(LeanConstants.NICK_NAME));
        }
    }

    private void onUploadSuccess() {
        final String url = currentPortraitFile.getUrl();
        AVUser user = AVUser.getCurrentUser();
        user.put(LeanConstants.PORTRAIT, url);
        user.saveInBackground(userSaveCallback.getCallback());
    }

    private void saveNickName() {
        mTarget.closeNameDialog();
        DialogUtil.showProgressDialog(mTarget, false);
        AVUser user = AVUser.getCurrentUser();
        user.put(LeanConstants.NICK_NAME, mTarget.getNameEtContent());
        user.saveInBackground(new NameSaveCallback(this).getCallback());
    }
}

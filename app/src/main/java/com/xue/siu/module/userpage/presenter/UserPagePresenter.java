package com.xue.siu.module.userpage.presenter;

import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowActivity;
import com.xue.siu.module.image.activity.ImageActivity;
import com.xue.siu.module.userpage.activity.UserPageFragment;

import java.io.IOException;
import java.util.List;

/**
 * Created by XUE on 2015/12/9.
 */
public class UserPagePresenter extends BaseFragmentPresenter<UserPageFragment> implements View.OnClickListener, HTPickFinishedListener {


    public UserPagePresenter(UserPageFragment target) {
        super(target);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.followee_view:
                FollowActivity.start(mTarget.getActivity(), FragmentType.FolloweeFragment);
                break;
            case R.id.follower_view:
                FollowActivity.start(mTarget.getActivity(), FragmentType.FollowerFragment);
                break;
            case R.id.sdv_portrait:
                mTarget.pickImage();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void initFragment() {
        String url = AVUser.getCurrentUser().get("portraitUrl").toString();
        mTarget.setPortrait(url);
    }

    @Override
    public void onImagePickFinished(@Nullable AlbumInfo albumInfo, List<PhotoInfo> photos) {
        uploadImage(photos.get(0));
    }

    @Override
    public void onImagePickCanceled() {

    }

    private void uploadImage(PhotoInfo photoInfo) {
        DialogUtil.showProgressDialog(mTarget.getActivity(), true);
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(AVUser.getCurrentUser().getObjectId(), photoInfo.getAbsolutePath());
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        final String url = file.getUrl();
                        AVUser user = AVUser.getCurrentUser();
                        user.put(LeanConstants.PORTRAIT, url);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                DialogUtil.hideProgressDialog(mTarget.getActivity());
                                if (e == null) {
                                    mTarget.setPortrait(url);
                                } else {
                                    ToastUtil.makeShortToast(R.string.upf_hint_upload_error);
                                }
                            }
                        });
                    } else {
                        DialogUtil.hideProgressDialog(mTarget.getActivity());
                        ToastUtil.makeShortToast(R.string.upf_hint_upload_error);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

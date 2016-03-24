package com.xue.siu.module.userpage.presenter;

import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.avim.LeanFriendshipCache;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowActivity;
import com.xue.siu.module.follow.callback.FriendshipCallback;
import com.xue.siu.module.news.callback.UploadImageCallback;
import com.xue.siu.module.userpage.activity.UserPageFragment;
import com.xue.siu.module.userpage.callback.UserSaveCallback;

import java.io.IOException;
import java.util.List;

/**
 * Created by XUE on 2015/12/9.
 */
public class UserPagePresenter extends BaseFragmentPresenter<UserPageFragment> implements
        View.OnClickListener,
        HTPickFinishedListener,
        AVIMResultListener {

    private FriendshipCallback friendshipCallback;
    private UploadImageCallback uploadImageCallback;
    private UserSaveCallback userSaveCallback;
    private AVFile currentPortraitFile;
    private boolean isQuerying = false;

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
    public void initFragment() {
        String url = AVUser.getCurrentUser().get(LeanConstants.PORTRAIT).toString();
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
        if (cbName.equals(FriendshipCallback.class.getName())) {
            isQuerying = false;
        } else if (cbName.equals(UploadImageCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget.getActivity());
            ToastUtil.makeShortToast(R.string.upf_hint_upload_error);
        } else if (cbName.equals(UserSaveCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget.getActivity());
            ToastUtil.makeShortToast(R.string.upf_hint_upload_error);
        }
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        if (cbName.equals(FriendshipCallback.class.getName())) {
            onQuerySuccess((AVFriendship) values[0]);
        } else if (cbName.equals(UploadImageCallback.class.getName())) {
            onUploadSuccess();
        } else if (cbName.equals(UserSaveCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget.getActivity());
            mTarget.setPortrait(currentPortraitFile.getUrl());
        }
    }

    private void onUploadSuccess() {
        final String url = currentPortraitFile.getUrl();
        AVUser user = AVUser.getCurrentUser();
        user.put(LeanConstants.PORTRAIT, url);
        user.saveInBackground(userSaveCallback.getCallback());
    }

    private void onQuerySuccess(AVFriendship avFriendship) {
        isQuerying = false;
        LeanFriendshipCache.getInstance().setFolloweeCache(avFriendship.getFollowees());
        LeanFriendshipCache.getInstance().setFollowerCache(avFriendship.getFollowers());
        mTarget.setFolloweeCount(LeanFriendshipCache.getInstance().getFolloweeCount());
        mTarget.setFollowerCount(LeanFriendshipCache.getInstance().getFollowerCount());
    }

    @Override
    public void onResume() {
        super.onResume();
        queryFollow();
    }

    private void queryFollow() {
        if (friendshipCallback == null)
            friendshipCallback = new FriendshipCallback(this);
        if (LeanFriendshipCache.getInstance().needQuery() && !isQuerying) {
            isQuerying = true;
            AVFriendshipQuery query = AVUser.friendshipQuery(AVUser.getCurrentUser().getObjectId());
            query.include("followee");
            query.include("follower");
            query.getInBackground(friendshipCallback.getCallback());
        } else {
            mTarget.setFolloweeCount(LeanFriendshipCache.getInstance().getFolloweeCount());
            mTarget.setFollowerCount(LeanFriendshipCache.getInstance().getFollowerCount());
        }
    }
}

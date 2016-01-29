package com.xue.siu.module.userpage.presenter;

import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.xue.siu.R;
import com.xue.siu.avim.AVIMClientManager;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.chat.activity.ChatActivity;
import com.xue.siu.module.userpage.activity.UserDataActivity;
import com.xue.siu.module.userpage.usertype.FriendshipType;

import java.util.List;

/**
 * Created by XUE on 2016/1/21.
 */
public class UserDataPresenter extends BaseActivityPresenter<UserDataActivity> implements View.OnClickListener {
    AVUser mUser;
    FriendshipType mType;
    /**
     * Is the user followed
     */
    private boolean mIsFollowed = false;

    private FollowCallback mFollowCallback = new FollowCallback() {
        @Override
        public void done(AVObject avObject, AVException e) {
            if (e == null) {
                mIsFollowed = !mIsFollowed;
                mTarget.updateButton(mIsFollowed);
            } else {
                ToastUtil.makeShortToast(e.getMessage());
            }
        }
    };

    public UserDataPresenter(UserDataActivity target) {
        super(target);
    }

    private void updateButton() {
        switch (mType) {
            case NONE:
            case FOLLOWER:
                mIsFollowed = false;
                break;
            case FOLLOWEE:
                mIsFollowed = true;
                break;
        }
        mTarget.updateButton(mIsFollowed);
    }

    @Override
    protected void initActivity() {
        mUser = mTarget.getIntent().getParcelableExtra(mTarget.KEY_USER);
        mType = (FriendshipType) mTarget.getIntent().getSerializableExtra(mTarget.KEY_FRIENDSHIP);
        updateButton();
        mTarget.setUserName(mUser.getUsername());
        mTarget.setPortraitUrl(mUser.get("portraitUrl").toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat:
                ChatActivity.start(mTarget, mUser);
                break;
            case R.id.btn_follow:
                onFollowClick();
                break;
        }
    }


    private void onFollowClick() {
        if (!mIsFollowed) {
            AVUser.getCurrentUser().followInBackground(mUser.getObjectId(), mFollowCallback);
        } else {
            AVUser.getCurrentUser().unfollowInBackground(mUser.getObjectId(), mFollowCallback);
        }
    }

    private void checkIfFollowed() {
        try {
            AVQuery<AVUser> followeeQuery = AVUser.getCurrentUser().followeeQuery(AVUser.class);
            followeeQuery.whereEqualTo("user", mUser);
            followeeQuery.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> list, AVException e) {

                }
            });
        } catch (AVException e) {
            e.printStackTrace();
        }

    }
}

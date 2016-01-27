package com.xue.siu.module.userpage.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.userpage.presenter.UserDataPresenter;
import com.xue.siu.module.userpage.usertype.FriendshipType;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2016/1/21.
 */
public class UserDataActivity extends BaseActionBarActivity<UserDataPresenter> {

    @Bind(R.id.btn_chat)
    Button mBtnChat;

    @Bind(R.id.btn_follow)
    Button mBtnFollow;

    @Bind(R.id.sdv_portrait)
    SimpleDraweeView mSdvPortrait;

    @Bind(R.id.tv_name)
    TextView mTvName;

    public static final String KEY_USER = "user";
    public static final String KEY_FRIENDSHIP = "friendship";

    public static void start(Activity activity, AVUser user, FriendshipType type) {
        Intent intent = new Intent(activity, UserDataActivity.class);
        intent.putExtra(KEY_USER, user);
        intent.putExtra(KEY_FRIENDSHIP, type);
        activity.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new UserDataPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_user_data);
        ButterKnife.bind(this);
        initContentView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initContentView() {
        mSdvPortrait.setOnClickListener(mPresenter);
        mBtnChat.setOnClickListener(mPresenter);
        mBtnFollow.setOnClickListener(mPresenter);
    }


    public void updateButton(boolean isFollowed) {
        if (isFollowed) {
            mBtnFollow.setText(R.string.uda_unfollow);
        } else {
            mBtnFollow.setText(R.string.uda_follow);
        }
    }

    public void setUserName(String userName) {
        mTvName.setText(userName);
    }

    public void setPortraitUrl(String url) {
        mSdvPortrait.setImageURI(Uri.parse(url));
    }
}

package com.xue.siu.module.userpage.presenter;

import android.view.View;

import com.avos.avoscloud.AVUser;
import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowActivity;
import com.xue.siu.module.image.activity.ImageActivity;
import com.xue.siu.module.userpage.activity.UserPageFragment;

/**
 * Created by XUE on 2015/12/9.
 */
public class UserPagePresenter extends BaseFragmentPresenter<UserPageFragment> implements View.OnClickListener {


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
                ImageActivity.start(mTarget.getActivity(),1);
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
        mTarget.setPortraitUrl(url);
    }
}

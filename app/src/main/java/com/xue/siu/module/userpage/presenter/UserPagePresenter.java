package com.xue.siu.module.userpage.presenter;

import android.view.View;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowActivity;
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

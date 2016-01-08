package com.xue.siu.module.userpage.activity;

import com.xue.siu.module.base.activity.BaseFragment;
import com.xue.siu.module.userpage.presenter.UserPagePresenter;

/**
 * Created by XUE on 2015/12/9.
 */
public class UserPageFragment extends BaseFragment<UserPagePresenter> {
    @Override
    protected void initPresenter() {
        mPresenter = new UserPagePresenter(this);
    }
}

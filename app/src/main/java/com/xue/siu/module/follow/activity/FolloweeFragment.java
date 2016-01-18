package com.xue.siu.module.follow.activity;

import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.follow.presenter.FolloweePresenter;

/**
 * Created by XUE on 2016/1/16.
 */
public class FolloweeFragment extends BaseBlankFragment<FolloweePresenter> {
    @Override
    protected void initPresenter() {
        mPresenter = new FolloweePresenter(this);
    }
}

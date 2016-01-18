package com.xue.siu.module.follow.activity;

import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.follow.presenter.FollowerPresenter;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowerFragment extends BaseBlankFragment<FollowerPresenter> {
    @Override
    protected void initPresenter() {
        mPresenter = new FollowerPresenter(this);
    }
}

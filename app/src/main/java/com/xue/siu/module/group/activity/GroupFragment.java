package com.xue.siu.module.group.activity;

import com.xue.siu.module.base.activity.BaseFragment;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.group.presenter.GroupPresenter;

/**
 * Created by XUE on 2015/12/9.
 */
public class GroupFragment extends BaseFragment<GroupPresenter> {
    @Override
    protected void initPresenter() {
        mPresenter = new GroupPresenter(this);
    }
}

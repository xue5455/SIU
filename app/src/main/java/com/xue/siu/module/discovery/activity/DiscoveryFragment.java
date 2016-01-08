package com.xue.siu.module.discovery.activity;

import com.xue.siu.module.base.activity.BaseFragment;
import com.xue.siu.module.discovery.presenter.DiscoveryPresenter;

/**
 * Created by XUE on 2015/12/9.
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> {
    @Override
    protected void initPresenter() {
        mPresenter = new DiscoveryPresenter(this);
    }
}

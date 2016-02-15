package com.xue.siu.module.discovery.presenter;

import android.util.Log;
import android.view.View;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.discovery.activity.DiscoveryFragment;
import com.xue.siu.module.news.activity.NewsActivity;

/**
 * Created by XUE on 2015/12/9.
 */
public class DiscoveryPresenter extends BaseFragmentPresenter<DiscoveryFragment> implements View.OnClickListener {
    public DiscoveryPresenter(DiscoveryFragment target) {
        super(target);
    }

    @Override
    public void initFragment() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_news:
                NewsActivity.start(mTarget.getActivity());
                break;
            case R.id.fl_hot_plans:
                break;
            case R.id.fl_city_users:
                break;
            case R.id.fl_city_plans:
                break;
            case R.id.fl_user_board:
                break;
        }
    }
}

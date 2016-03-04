package com.xue.siu.module.news.presenter;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.news.activity.NewsActivity;
import com.xue.siu.module.news.activity.PublishActivity;

/**
 * Created by XUE on 2016/2/15.
 */
public class NewsPresenter extends BaseActivityPresenter<NewsActivity> implements ViewPager.
        OnPageChangeListener, View.OnClickListener {
    public NewsPresenter(NewsActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTarget.updateText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_action:
                mTarget.setCurrentItem(0);
                break;
            case R.id.tv_calendar:
                mTarget.setCurrentItem(1);
                break;
            case R.id.btn_publish_news:
                PublishActivity.start(mTarget);
                break;
        }
    }
}

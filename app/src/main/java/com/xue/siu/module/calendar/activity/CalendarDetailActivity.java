package com.xue.siu.module.calendar.activity;

import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.calendar.presenter.CalendarDetailPresenter;

/**
 * Created by XUE on 2016/3/27.
 */
public class CalendarDetailActivity extends BaseActionBarActivity<CalendarDetailPresenter> {

    @Override
    protected void initPresenter() {
        mPresenter = new CalendarDetailPresenter(this);
    }
}

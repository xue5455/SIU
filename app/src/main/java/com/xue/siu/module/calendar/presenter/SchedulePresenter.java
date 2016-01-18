package com.xue.siu.module.calendar.presenter;

import android.view.View;

import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.calendar.activity.ScheduleFragment;
import com.xue.siu.module.calendar.adapter.CategoryAdapter;

/**
 * Created by XUE on 2015/12/9.
 */
public class SchedulePresenter extends BaseFragmentPresenter<ScheduleFragment> implements View.OnClickListener {
    public SchedulePresenter(ScheduleFragment target) {
        super(target);
    }

    @Override
    public void onClick(View v) {

    }

    public void initGvAdapter() {
        mTarget.setAdapter(new CategoryAdapter());
    }
}

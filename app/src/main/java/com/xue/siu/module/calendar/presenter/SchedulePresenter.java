package com.xue.siu.module.calendar.presenter;

import android.view.View;

import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.calendar.activity.NewCalendarActivity;
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
    public void initFragment() {
        initGvAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                NewCalendarActivity.start(mTarget.getActivity());
                break;
        }
    }

    public void initGvAdapter() {
        mTarget.setAdapter(new CategoryAdapter());
    }
}

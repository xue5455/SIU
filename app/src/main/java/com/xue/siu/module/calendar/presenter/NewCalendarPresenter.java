package com.xue.siu.module.calendar.presenter;

import android.view.View;
import android.widget.CompoundButton;

import com.xue.siu.common.util.TimeUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.calendar.activity.NewCalendarActivity;

import java.util.Date;

/**
 * Created by XUE on 2016/3/24.
 */
public class NewCalendarPresenter extends BaseActivityPresenter<NewCalendarActivity> implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private final String ZERO_START = "00:00";
    private final String FINAL_END = "23:59";
    private Date currentDate = new Date();

    public NewCalendarPresenter(NewCalendarActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        String today = TimeUtil.getTodayDate(TimeUtil.TIME_FORMAT_SLASH);
        StringBuilder sb = new StringBuilder();
        sb.append(today);
        sb.append(TimeUtil.getDayOfWeek());
        mTarget.setStartDate(sb.toString());
        mTarget.setEndDate(sb.toString());
        resetTime();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mTarget.setStartTime(ZERO_START);
            mTarget.setEndTime(FINAL_END);
        } else {
            resetTime();
        }
    }

    private void resetTime() {
        mTarget.setStartTime(TimeUtil.getAfterTenMinutes());
        mTarget.setEndTime(TimeUtil.getAfterTenMinutes());
    }
}

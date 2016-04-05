package com.xue.siu.module.calendar.presenter;


import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.j256.ormlite.dao.Dao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.xue.siu.R;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.SystemUtil;
import com.xue.siu.common.util.TextUtil;
import com.xue.siu.common.util.TimeUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.constant.C;
import com.xue.siu.db.OrmDBHelper;
import com.xue.siu.db.bean.Schedule;
import com.xue.siu.db.dao.ScheduleDao;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.calendar.activity.NewCalendarActivity;
import com.xue.siu.module.calendar.callback.CalendarSaveCallback;
import com.xue.siu.module.calendar.model.CalendarVO;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by XUE on 2016/3/24.
 */
public class NewCalendarPresenter extends BaseActivityPresenter<NewCalendarActivity> implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        AVIMResultListener {
    private final String ZERO_START = "00:00";
    private final String FINAL_END = "23:59";
    private Timepoint startTP;
    private Timepoint endTP;
    private Boolean isStartTime = null;
    private Date currentDate;

    public NewCalendarPresenter(NewCalendarActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        currentDate = (Date) mTarget.getIntent().getSerializableExtra(mTarget.DATE_KEY);
        if (currentDate == null) {
            currentDate = new Date(System.currentTimeMillis());
        }
        mTarget.setStartDate(ResourcesUtil.stringFormat(
                R.string.nca_date_format,
                currentDate.getYear() + 1900,
                currentDate.getMonth() + 1,
                currentDate.getDate(),
                TimeUtil.getDayOfWeek(currentDate)));

        resetTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_calendar_sd_container:
                mTarget.showDatePicker();
                //日期
                break;
            case R.id.new_calendar_st_container:
                resetTP();
                mTarget.showTimePicker(startTP, getCurrentTp());
                isStartTime = true;
                //开始时间
                break;
            case R.id.new_calendar_et_container:
                //结束时间
                resetTP();
                mTarget.showTimePicker(endTP, startTP);
                isStartTime = false;
                break;
            case R.id.new_calendar_confirm_btn:
                onConfirmClick();
                break;
        }
    }

    private Timepoint getCurrentTp() {
        Calendar c = Calendar.getInstance();
        return new Timepoint(c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
    }

    private boolean isToday() {
        return currentDate.compareTo(new Date()) == 0;
    }

    private void resetTP() {
        if (!isToday()) {
            return;
        }
        Timepoint tp = getCurrentTp();
        if (startTP.compareTo(tp) < 0) {
            startTP = new Timepoint(tp.getHour(), tp.getMinute());
            mTarget.setStartTime(ResourcesUtil.stringFormat(R.string.nca_time_format, startTP.getHour(), startTP.getMinute()));
        }
        if (endTP.compareTo(tp) < 0) {
            endTP = new Timepoint(tp.getHour(), tp.getMinute());
            mTarget.setEndTime(ResourcesUtil.stringFormat(R.string.nca_time_format, endTP.getHour(), endTP.getMinute()));
        }
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
        String currentTime = TimeUtil.getCurrentTime();
        mTarget.setStartTime(currentTime);
        mTarget.setEndTime(currentTime);
        int hour = Integer.valueOf(currentTime.split(C.COLON)[0]);
        int minute = Integer.valueOf(currentTime.split(C.COLON)[1]);
        startTP = new Timepoint(hour, minute, 0);
        endTP = new Timepoint(hour, minute, 0);
    }

    /**
     * @param view
     * @param year
     * @param monthOfYear 0-11
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        currentDate.setYear(year - 1900);
        currentDate.setMonth(monthOfYear);
        currentDate.setDate(dayOfMonth);
        mTarget.setStartDate(ResourcesUtil.stringFormat(
                R.string.nca_date_format,
                year,
                monthOfYear + 1,
                dayOfMonth,
                TimeUtil.getDayOfWeek(currentDate)));
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if (isStartTime) {
            startTP = new Timepoint(hourOfDay, minute, second);
            mTarget.setStartTime(ResourcesUtil.stringFormat(R.string.nca_time_format, startTP.getHour(), startTP.getMinute()));
            if (endTP.compareTo(startTP) < 0) {
                /* 如果结束时间小于开始时间，则重置结束时间到开始时间 */
                endTP = new Timepoint(hourOfDay, minute, second);
                mTarget.setEndTime(ResourcesUtil.stringFormat(R.string.nca_time_format, endTP.getHour(), endTP.getMinute()));
            }
        } else {
            endTP = new Timepoint(hourOfDay, minute, second);
            mTarget.setEndTime(ResourcesUtil.stringFormat(R.string.nca_time_format, endTP.getHour(), endTP.getMinute()));
        }
    }

    private void onConfirmClick() {
        String title = mTarget.getCalendarTitle();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.makeShortToast(R.string.nca_toast_title);
            return;
        }

        CalendarVO calendarVO = new CalendarVO();
        calendarVO.setTitle(title);
        calendarVO.setDate(getMill() / 1000);
        calendarVO.setStartTime(startTP.compareTo(new Timepoint(0, 0, 0)));
        calendarVO.setEndTime(endTP.compareTo(new Timepoint(0, 0, 0)));
        calendarVO.setOwner(AVUser.getCurrentUser());
        calendarVO.setLocation(mTarget.getLocation());
        calendarVO.addAttender(AVUser.getCurrentUser());
        DialogUtil.showProgressDialog(mTarget, false);
        AVObject object = calendarVO.getAvObject();
        object.saveInBackground(new CalendarSaveCallback(this).getCallback());

    }

    @Override
    public void onLeanError(String cbName, AVException e) {
        DialogUtil.hideProgressDialog(mTarget);
        if (cbName.equals(CalendarSaveCallback.class.getName())) {
            ToastUtil.makeShortToast(e.getMessage());
        }
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        DialogUtil.hideProgressDialog(mTarget);
        if (cbName.equals(CalendarSaveCallback.class.getName())) {
            addScheduleToDb();
            mTarget.setResult(mTarget.RESULT_OK);
            mTarget.finish();
        }
    }

    private void addScheduleToDb() {
        ScheduleDao dao = new ScheduleDao(mTarget.getApplicationContext());
        Schedule schedule = new Schedule();
        schedule.setTitle(mTarget.getCalendarTitle());
        schedule.setDate(getMill() / 1000);
        schedule.setStart(startTP.compareTo(new Timepoint(0, 0, 0)));
        schedule.setEnd(endTP.compareTo(new Timepoint(0, 0, 0)));
        schedule.setAttender(AVUser.getCurrentUser().getUsername());
        schedule.setOwner(AVUser.getCurrentUser().getUsername());
        schedule.setLocation(mTarget.getLocation());
        dao.add(schedule);
    }

    private long getMill() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(currentDate.getYear() + 1900, currentDate.getMonth(), currentDate.getDate(), 0, 0, 0);
        return cal.getTimeInMillis();
    }
}

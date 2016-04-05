package com.xue.siu.module.calendar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.calendar.presenter.NewCalendarPresenter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by XUE on 2016/3/24.
 */
public class NewCalendarActivity extends BaseActionBarActivity<NewCalendarPresenter> {
    public static final String DATE_KEY = "date";
    private EditText etTitle;
    private EditText etLocation;
    private CheckBox cbAllDay;
    private TextView tvSd;//开始日期
    private TextView tvSt;//开始时间
    private TextView tvEt;//结束时间
    private Button btnMore;

    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NewCalendarActivity.class);
        activity.startActivity(intent);
    }

    public static void startForResult(Fragment fragment, int requestCode,Date date) {
        Intent intent = new Intent(fragment.getActivity(), NewCalendarActivity.class);
        intent.putExtra(DATE_KEY,date);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_new_calendar);
        setStatueBarColor(R.color.action_bar_bg);
        setNavigationBarBlack();
        setTitle(R.string.nca_title);
        initViews();
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(R.color.white));
    }


    private void initViews() {
        etTitle = findView(R.id.new_calendar_title_et);
        etLocation = findView(R.id.new_calendar_location_et);
        cbAllDay = findView(R.id.new_calendar_time_cb);
        tvSd = findView(R.id.new_calendar_sd_tv);
        tvSt = findView(R.id.new_calendar_st_tv);
        tvEt = findView(R.id.new_calendar_et_tv);
        findView(R.id.new_calendar_confirm_btn).setOnClickListener(mPresenter);
        btnMore = findView(R.id.new_calendar_options_btn);
        btnMore.setOnClickListener(mPresenter);
        findView(R.id.new_calendar_sd_container).setOnClickListener(mPresenter);
        findView(R.id.new_calendar_st_container).setOnClickListener(mPresenter);
        findView(R.id.new_calendar_et_container).setOnClickListener(mPresenter);
        cbAllDay.setOnCheckedChangeListener(mPresenter);
        initTimePicker();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new NewCalendarPresenter(this);
    }

    public void setStartDate(String date) {
        tvSd.setText(date);
    }

    public void setStartTime(String time) {
        tvSt.setText(time);
    }


    public void setEndTime(String time) {
        tvEt.setText(time);
    }

    public void hideOptionsButton() {
        btnMore.setVisibility(View.GONE);
    }

    public void showDatePicker() {
        if (datePicker == null) {
            Calendar now = Calendar.getInstance();
            datePicker = DatePickerDialog.newInstance(
                    mPresenter,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.setMinDate(now);
        }
        datePicker.show(getFragmentManager(), "DatePicker");
    }

    public void showTimePicker(Timepoint tp, Timepoint minTP) {
        timePicker.setMinTime(minTP);
        timePicker.setStartTime(tp.getHour(), tp.getMinute());
        timePicker.show(getFragmentManager(), "TimePicker");
    }

    private void initTimePicker() {
        if (timePicker == null) {
            timePicker = TimePickerDialog.newInstance(
                    mPresenter,
                    0,
                    0,
                    false
            );
        }
    }


    public String getCalendarTitle() {
        return etTitle.getText().toString();
    }

    public String getLocation() {
        return etLocation.getText().toString();
    }

    public String getDate() {
        return tvSd.getText().toString();
    }

    public String getStartTime() {
        return tvSt.getText().toString();
    }

    public String getEndTime() {
        return tvEt.getText().toString();
    }
}

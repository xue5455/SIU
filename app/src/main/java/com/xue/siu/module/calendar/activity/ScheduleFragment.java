package com.xue.siu.module.calendar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarFragment;
import com.xue.siu.module.base.activity.BaseActivity;
import com.xue.siu.module.calendar.presenter.SchedulePresenter;
import com.xue.siu.module.mainpage.activity.MainPageActivity;

import java.lang.ref.WeakReference;

import com.xue.siu.common.view.datepicker.DateView;

/**
 * Created by XUE on 2015/12/9.
 */
public class ScheduleFragment extends BaseActionBarFragment<SchedulePresenter> {

    public final int REQUEST_CODE = 1;
    private ImageView btnCalendar;
    private HTSwipeRecyclerView rvCalendar;
    private DateView dvCalendar;

    @Override
    protected void initPresenter() {
        mPresenter = new SchedulePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootViewRef == null || mRootViewRef.get() == null) {
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            setRealContentView(R.layout.fragment_calendar);
            initNavigationBar();
            initContentView(rootView);

            mRootViewRef = new WeakReference<>(rootView);
        } else {
            ViewGroup parent = (ViewGroup) mRootViewRef.get().getParent();
            if (parent != null) {
                parent.removeView(mRootViewRef.get());
            }
        }
        return mRootViewRef.get();
    }

    private void initNavigationBar() {
        setTitle(R.string.mainpage_tab_schedule);
        setNavigationBarBlack();
        setRightView(R.mipmap.ic_add_white);
        setRightClickListener(mPresenter);
    }

    private void initContentView(View view) {
        dvCalendar = (DateView) view.findViewById(R.id.schedule_fragment_calendar_dv);
        rvCalendar = (HTSwipeRecyclerView) view.findViewById(R.id.schedule_fragment_calendar_rv);
        btnCalendar = (ImageView) view.findViewById(R.id.schedule_fragment_calendar_btn);
        btnCalendar.setOnClickListener(mPresenter);
        dvCalendar.setAnimationListener(mPresenter);
        rvCalendar.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.initAdapter();
        dvCalendar.setSuccessor(mPresenter);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        rvCalendar.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getClass().equals(MainPageActivity.class)) {
            ((BaseActivity) getActivity()).setStatueBarColor(R.color.action_bar_bg);
        }
    }

    public void toggle() {
        dvCalendar.toggle();
    }

    public void setCalendarButton(int id) {
        btnCalendar.setImageResource(id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                mPresenter.getScheduleFromDb();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

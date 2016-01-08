package com.xue.siu.module.schedule.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarFragment;
import com.xue.siu.module.base.activity.BaseFragment;
import com.xue.siu.module.schedule.presenter.SchedulePresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2015/12/9.
 */
public class ScheduleFragment extends BaseActionBarFragment<SchedulePresenter> {
    GridView categoryGv;
    Button addBtn;

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
//            presenter.initRecyclerViewAdapter();
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
    }

    private void initContentView(View view) {

    }
}

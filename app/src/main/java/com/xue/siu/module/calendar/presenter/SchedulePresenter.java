package com.xue.siu.module.calendar.presenter;

import android.util.SparseArray;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventHelper;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.view.datepicker.DateBean;
import com.xue.siu.common.view.datepicker.DateView;
import com.xue.siu.common.view.datepicker.OnCalendarClickHandler;
import com.xue.siu.db.bean.Schedule;
import com.xue.siu.db.dao.ScheduleDao;
import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.calendar.activity.NewCalendarActivity;
import com.xue.siu.module.calendar.activity.ScheduleFragment;
import com.xue.siu.module.calendar.adapter.CategoryAdapter;
import com.xue.siu.module.calendar.model.CalendarVO;
import com.xue.siu.module.calendar.model.ScheduleVO;
import com.xue.siu.module.calendar.viewholder.DividerViewHolder;
import com.xue.siu.module.calendar.viewholder.EmptyScheduleViewHolder;
import com.xue.siu.module.calendar.viewholder.ScheduleItemViewHolder;
import com.xue.siu.module.calendar.viewholder.item.DividerViewHolderItem;
import com.xue.siu.module.calendar.viewholder.item.EmptyViewHolderItem;
import com.xue.siu.module.calendar.viewholder.item.ScheduleItemType;
import com.xue.siu.module.calendar.viewholder.item.ScheduleViewHolderItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by XUE on 2015/12/9.
 */
public class SchedulePresenter extends BaseFragmentPresenter<ScheduleFragment> implements
        View.OnClickListener,
        OnCalendarClickHandler,
        DateView.OnAnimationListener,
        ItemEventListener {
    /* Calendar是否打开 */
    private boolean isOpen = false;
    private ScheduleDao scheduleDao;
    private final SparseArray<Class<? extends TRecycleViewHolder>> viewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(ScheduleItemType.ITEM_TYPE_SCHEDULE, ScheduleItemViewHolder.class);
                    put(ScheduleItemType.ITEM_TYPE_DIVIDER, DividerViewHolder.class);
                    put(ScheduleItemType.ITEM_TYPE_BLANK, EmptyScheduleViewHolder.class);
                }
            };
    private TRecycleViewAdapter scheduleAdapter;
    private List<TAdapterItem> adapterItems = new ArrayList<>();
    GregorianCalendar cal = new GregorianCalendar();

    public SchedulePresenter(ScheduleFragment target) {
        super(target);
    }

    public void initAdapter() {
        scheduleAdapter = new TRecycleViewAdapter(mTarget.getContext(), viewHolders, adapterItems);
        scheduleAdapter.setItemEventListener(this);
        mTarget.setAdapter(scheduleAdapter);
    }

    @Override
    public void initFragment() {
        scheduleDao = new ScheduleDao(mTarget.getActivity().getApplicationContext());
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        getScheduleFromDb();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_right_container:
                NewCalendarActivity.startForResult(mTarget, mTarget.REQUEST_CODE, null);
                break;
            case R.id.schedule_fragment_calendar_btn:
                mTarget.toggle();
                break;
        }
    }

    @Override
    public void setSuccessor(OnCalendarClickHandler handler) {

    }

    @Override
    public void handleCalendarClick(DateBean dateBean) {
        /* Date中 m 为0-11，所以这里需要减1 */
        cal.set(dateBean.y, dateBean.m - 1, dateBean.d, 0, 0, 0);
        getScheduleFromDb();
    }

    @Override
    public void onAnimationStart() {
        isOpen = !isOpen;
        mTarget.setCalendarButton(isOpen ?
                R.mipmap.ic_btn_calendar_up :
                R.mipmap.ic_btn_calendar_down);
    }

    @Override
    public void onAnimationEnd() {

    }

    public void getScheduleFromDb() {
        List<Schedule> schedules = scheduleDao.query(cal.getTimeInMillis() / 1000);
        adapterItems.clear();
        adapterItems.add(new DividerViewHolderItem());
        LogUtil.i("xxj","日程数量 " + schedules.size());
        if (schedules.size() != 0) {
            for (Schedule schedule : schedules) {
                adapterItems.add(new ScheduleViewHolderItem(schedule));
                adapterItems.add(new DividerViewHolderItem());
            }
        } else {
            adapterItems.add(new EmptyViewHolderItem());
            adapterItems.add(new DividerViewHolderItem());
        }
        scheduleAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        if (ItemEventHelper.isClick(eventName)) {
            switch (view.getId()) {
                case R.id.schedule_item_new_btn:
                    NewCalendarActivity.startForResult(mTarget, mTarget.REQUEST_CODE, cal.getTime());
                    break;
            }
        }
        return true;
    }
}

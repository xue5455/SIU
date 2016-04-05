package com.xue.siu.module.calendar.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.db.bean.Schedule;


/**
 * Created by XUE on 2016/3/27.
 */
public class ScheduleViewHolderItem implements TAdapterItem<Schedule> {
    private Schedule scheduleVO;

    public ScheduleViewHolderItem(Schedule scheduleVO) {
        this.scheduleVO = scheduleVO;
    }

    @Override
    public int getViewType() {
        return ScheduleItemType.ITEM_TYPE_SCHEDULE;
    }

    @Override
    public int getId() {
        return scheduleVO.hashCode();
    }

    @Override
    public Schedule getDataModel() {
        return scheduleVO;
    }
}

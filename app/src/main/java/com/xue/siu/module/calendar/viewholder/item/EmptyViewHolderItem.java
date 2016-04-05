package com.xue.siu.module.calendar.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;

/**
 * Created by XUE on 2016/3/27.
 */
public class EmptyViewHolderItem implements TAdapterItem {
    @Override
    public int getViewType() {
        return ScheduleItemType.ITEM_TYPE_BLANK;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Object getDataModel() {
        return null;
    }
}

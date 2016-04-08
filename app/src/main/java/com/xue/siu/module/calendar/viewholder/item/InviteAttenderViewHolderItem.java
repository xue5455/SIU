package com.xue.siu.module.calendar.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.module.calendar.viewholder.InviteAttenderViewHolder;

/**
 * Created by XUE on 2016/4/7.
 */
public class InviteAttenderViewHolderItem implements TAdapterItem<InviteAttenderViewHolder.UserWrapper>{
    private InviteAttenderViewHolder.UserWrapper model;

    public InviteAttenderViewHolderItem(InviteAttenderViewHolder.UserWrapper model) {
        this.model = model;
    }

    @Override
    public int getViewType() {
        return InviteItemType.ITEM_TYPE_USER;
    }

    @Override
    public int getId() {
        return model.hashCode();
    }

    @Override
    public InviteAttenderViewHolder.UserWrapper getDataModel() {
        return model;
    }
}

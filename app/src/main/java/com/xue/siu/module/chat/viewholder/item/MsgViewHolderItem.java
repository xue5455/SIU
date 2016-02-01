package com.xue.siu.module.chat.viewholder.item;

import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.db.bean.SIUMessage;

/**
 * Created by XUE on 2016/1/27.
 */
public class MsgViewHolderItem implements TAdapterItem<MessageUserWrapper> {
    protected MessageUserWrapper mWrapper;
    private int mItemType;
    public MsgViewHolderItem(SIUMessage msg, AVUser user) {
        mWrapper = new MessageUserWrapper(msg, user);
    }
    public MsgViewHolderItem(SIUMessage msg,AVUser user,int itemType){
        this(msg,user);
        mItemType = itemType;
    }

    @Override
    public int getViewType() {
        return mItemType;
    }

    @Override
    public int getId() {
        return mWrapper.hashCode();
    }

    @Override
    public MessageUserWrapper getDataModel() {
        return mWrapper;
    }
}

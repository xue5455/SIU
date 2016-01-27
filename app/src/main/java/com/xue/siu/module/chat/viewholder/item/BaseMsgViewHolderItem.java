package com.xue.siu.module.chat.viewholder.item;

import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.db.bean.SIUMessage;

/**
 * Created by XUE on 2016/1/27.
 */
public abstract class BaseMsgViewHolderItem implements TAdapterItem<MessageUserWrapper> {
    protected MessageUserWrapper mWrapper;

    public BaseMsgViewHolderItem(SIUMessage msg, AVUser user) {
        mWrapper = new MessageUserWrapper(msg, user);
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

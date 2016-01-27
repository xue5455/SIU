package com.xue.siu.module.chat.viewholder.item;

import com.avos.avoscloud.AVUser;
import com.xue.siu.db.bean.SIUMessage;

/**
 * Created by XUE on 2016/1/27.
 */
public class MessageUserWrapper {
    private SIUMessage mMsg;
    private AVUser mUser;

    public MessageUserWrapper(SIUMessage msg,AVUser user){
        mMsg = msg;
        mUser = user;
    }

    public SIUMessage getMsg() {
        return mMsg;
    }

    public void setMsg(SIUMessage msg) {
        this.mMsg = msg;
    }

    public AVUser getUser() {
        return mUser;
    }

    public void setUser(AVUser user) {
        this.mUser = user;
    }
}

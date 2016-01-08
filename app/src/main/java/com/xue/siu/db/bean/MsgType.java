package com.xue.siu.db.bean;

import android.text.TextUtils;

/**
 * Created by XUE on 2015/12/10.
 */
public enum MsgType {
    Text("text"),//文字消息
    Image("image"),//图片消息
    Schedule("schedule");//日程消息

    private String str;

    private MsgType(String str) {
        this.str = str;
    }


    public static MsgType getMsgType(String msgType) {
        if (TextUtils.equals(msgType, "text"))
            return Text;
        else if (TextUtils.equals(msgType, "image"))
            return Image;
        else if (TextUtils.equals(msgType, "schedule"))
            return Schedule;
        return null;
    }
}

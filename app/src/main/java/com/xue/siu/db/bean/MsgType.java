package com.xue.siu.db.bean;

import android.text.TextUtils;

/**
 * Created by XUE on 2015/12/10.
 */
public enum MsgType {
    Text(4),//文字消息
    Image(5),//图片消息
    Schedule(6);//日程消息

    private int type;

    private MsgType(int type) {
        this.type = type;
    }


    public int getValue() {
        return type;
    }
}

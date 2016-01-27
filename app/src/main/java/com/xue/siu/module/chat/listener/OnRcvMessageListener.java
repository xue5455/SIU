package com.xue.siu.module.chat.listener;

import com.xue.siu.db.bean.SIUMessage;

/**
 * Created by XUE on 2016/1/27.
 */
public interface OnRcvMessageListener {
    void onRcvMessage(SIUMessage message);
}

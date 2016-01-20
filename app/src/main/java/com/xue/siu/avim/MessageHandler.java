package com.xue.siu.avim;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

/**
 * Created by XUE on 2016/1/18.
 */
public class MessageHandler extends AVIMMessageHandler {
    private Context mContext;

    public MessageHandler(Context context) {
        mContext = context;
    }

    //接收到消息后的处理逻辑
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        //通知数据库添加消息
    }

    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

    }
}

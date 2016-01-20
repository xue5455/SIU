package com.xue.siu.service;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;


/**
 * Created by XUE on 2015/12/10.
 */
public class LeanManager {
    private static LeanManager mInstance;
    public static AVIMClient mClient;
    private AVIMConversationCallback mConversationCallback = new AVIMConversationCallback() {
        @Override
        public void done(AVIMException e) {
            //通知数据库修改消息状态
            if (e == null) {
                //消息发送成功，修改数据库消息状态为已发送
            } else {
                //消息发送失败，修改数据库消息状态为发送失败
            }
        }
    };
    private AVIMHandler msgHandler = new AVIMHandler();

    public class AVIMHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            //通知数据库添加消息
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

        }
    }

    private LeanManager() {
        AVIMMessageManager.registerDefaultMessageHandler(msgHandler);

    }

    public static void init() {
        if (mInstance == null) {
            synchronized (LeanManager.class) {
                if (mInstance == null)
                    mInstance = new LeanManager();
            }
        }

    }

    public static void send(AVIMConversation conversation, AVIMTypedMessage msg) {
        mInstance.sendMessage(conversation, msg);
    }

    private void sendMessage(AVIMConversation conversation, AVIMTypedMessage msg) {
        conversation.sendMessage(msg, mConversationCallback);
    }
}

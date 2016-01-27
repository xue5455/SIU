package com.xue.siu.avim;


import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.xue.siu.avim.AVIMClientManager;
import com.xue.siu.avim.DefaultMessageHandler;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.module.chat.listener.OnRcvMessageListener;

/**
 * Created by XUE on 2016/1/27.
 */
public class ActivityMessageHandler extends DefaultMessageHandler {
    private OnRcvMessageListener mMsgListener;
    /* 当前聊天对象 */
    private String mCurrentChatUser;

    public ActivityMessageHandler(Context context, String user, OnRcvMessageListener listener) {
        super(context);
        mCurrentChatUser = user;
        mMsgListener = listener;
    }

    //接收到消息后的处理逻辑
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        String clientId = client.getClientId();
        if (TextUtils.equals(clientId, AVIMClientManager.getInstance().getClientId()) && message.getFrom().equals(mCurrentChatUser)) {
            //通知页面更新
            SIUMessage siuMessage = convertMessage(message);
            if (mMsgListener != null)
                mMsgListener.onRcvMessage(siuMessage);
        }
    }
}

package com.xue.siu.common.util;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.MsgStatus;
import com.xue.siu.db.bean.MsgType;
import com.xue.siu.db.bean.SIUMessage;

/**
 * Created by XUE on 2016/1/26.
 */
public class MessageUtil {

    public static AVIMTextMessage convertStrToMessage(String msg) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(msg);
        return message;
    }

    public static SIUMessage convertSiuToAVIMMsg(String conversationId, String toUser, AVIMTextMessage textMessage) {
        String content = textMessage.getText();
        long time = textMessage.getTimestamp();
        MsgDirection direction = MsgDirection.OUT;
        MsgType type = MsgType.Text;
        MsgStatus status = MsgStatus.SENT_SUCCESS;
        return new SIUMessage(conversationId, AVUser.getCurrentUser().getUsername(), toUser, type, content, time, direction, status);

    }
}

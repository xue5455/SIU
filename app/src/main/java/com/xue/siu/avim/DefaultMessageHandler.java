package com.xue.siu.avim;

import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xue.siu.db.OrmDBHelper;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.MsgStatus;
import com.xue.siu.db.bean.MsgType;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.db.dao.SIUMessageDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by XUE on 2016/1/18.
 */
public class DefaultMessageHandler extends AVIMMessageHandler {
    private Context mContext;
    private SIUMessageDao mDao;

    public DefaultMessageHandler(Context context) {
        mContext = context;
        mDao = new SIUMessageDao(mContext);
    }

    //接收到消息后的处理逻辑
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        String clientId = client.getClientId();
        if (TextUtils.equals(clientId, AVIMClientManager.getInstance().getClientId())) {
            //通知数据库添加消息
            SIUMessage siuMessage = convertMessage(message);
            // OrmDBHelper.getHelper(mContext).getDao(SIUMessage.class).
            mDao.add(siuMessage);
        }
    }

    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

    }

    protected SIUMessage convertMessage(AVIMMessage message) {
        String conversationId = message.getConversationId();
        String fromUser = message.getFrom();
        String toUser = AVUser.getCurrentUser().getUsername();
        long time = message.getTimestamp();
        MsgStatus status = MsgStatus.RECV_NOR;
        String content = null;
        MsgType type = null;
        MsgDirection direction = MsgDirection.IN;
        if (message instanceof AVIMTextMessage) {
            content = ((AVIMTextMessage) message).getText();
            type = MsgType.Text;
        } else if (message instanceof AVIMImageMessage) {
            JSONObject jsonObject = new JSONObject();
            String url = ((AVIMImageMessage) message).getFileUrl();
            int width = ((AVIMImageMessage) message).getWidth();
            int height = ((AVIMImageMessage) message).getHeight();
            try {
                jsonObject.put("url", url);
                jsonObject.put("width", width);
                jsonObject.put("height", height);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            content = jsonObject.toString();
            type = MsgType.Image;
        }
        return new SIUMessage(conversationId, fromUser, toUser, type, content, time, direction, status);
    }
}

package com.xue.siu.db.bean;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XUE on 2015/12/10.
 */
@DatabaseTable(tableName = "tb_message")
public class SIUMessage {
    public static final String COLUMN_CONVERSATION_ID = "conversationId";
    public static final String COLUMN_FROM_USER = "fromUser";
    public static final String COLUMN_TO_USER = "toUser";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_S_TIME = "sTime";
    public static final String COLUMN_DIRECTION = "direction";
    public static final String COLUMN_STATUS = "status";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = COLUMN_CONVERSATION_ID)
    private String conversationId;//讨论组，若为空，则为私有消息，若不为空，则为讨论组消息
    @DatabaseField(columnName = COLUMN_FROM_USER)
    private String fUser;//消息发出方
    @DatabaseField(columnName = COLUMN_TO_USER)
    private String tUser;//消息接收方
    @DatabaseField(columnName = COLUMN_TYPE)
    private MsgType type;//消息类型
    @DatabaseField(columnName = COLUMN_CONTENT)
    private String content;//消息内容
    @DatabaseField(columnName = COLUMN_S_TIME)
    private long sTime;//消息时间
    @DatabaseField(columnName = COLUMN_DIRECTION)
    private MsgDirection direction;//消息模式
    @DatabaseField(columnName = COLUMN_STATUS)
    private MsgStatus status;//消息状态

    public SIUMessage() {

    }

    public SIUMessage(String conversationId, String fUser, String tUser, MsgType type, String content, long sTime, MsgDirection direction, MsgStatus status) {
        this.conversationId = conversationId;
        this.fUser = fUser;
        this.tUser = tUser;
        this.type = type;
        this.content = content;
        this.sTime = sTime;
        this.direction = direction;
        this.status = status;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfUser() {
        return fUser;
    }

    public void setfUser(String fUser) {
        this.fUser = fUser;
    }

    public String gettUser() {
        return tUser;
    }

    public void settUser(String tUser) {
        this.tUser = tUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public MsgStatus getStatus() {
        return status;
    }

    public void setStatus(MsgStatus status) {
        this.status = status;
    }

    public MsgDirection getDirection() {
        return direction;
    }

    public void setDirection(MsgDirection direction) {
        this.direction = direction;
    }
}

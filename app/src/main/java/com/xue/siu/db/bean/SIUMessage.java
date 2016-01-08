package com.xue.siu.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XUE on 2015/12/10.
 */
@DatabaseTable(tableName = "tb_message")
public class SIUMessage {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "group")
    private String group;//讨论组，若为空，则为私有消息，若不为空，则为讨论组消息
    @DatabaseField(columnName = "fuser")
    private String fUser;//消息发出方
    @DatabaseField(columnName = "tuser")
    private String tUser;//消息接收方
    @DatabaseField(columnName = "type")
    private MsgType type;//消息类型
    @DatabaseField(columnName = "content")
    private String content;//消息内容
    @DatabaseField(columnName = "stime")
    private long sTime;//消息时间
    @DatabaseField(columnName = "mode")
    private MsgDirection mode;//消息模式
    @DatabaseField(columnName = "status")
    private MsgStatus status;//消息状态

    public SIUMessage(String group, String fUser, String tUser, MsgType type, String content, long sTime, MsgDirection mode, MsgStatus status) {
        this.group = group;
        this.fUser = fUser;
        this.tUser = tUser;
        this.type = type;
        this.content = content;
        this.sTime = sTime;
        this.mode = mode;
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public MsgDirection getMode() {
        return mode;
    }

    public void setMode(MsgDirection mode) {
        this.mode = mode;
    }
}

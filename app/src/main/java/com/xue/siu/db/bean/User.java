package com.xue.siu.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XUE on 2015/12/10.
 */
@DatabaseTable(tableName = "tb_user")
public class User {
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_NOTE_NAME = "note_name";
    public static final String COLUMN_CONVERSATION_ID = "conversationId";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = COLUMN_ACCOUNT)
    private String account;//用户帐号
    @DatabaseField(columnName = COLUMN_NICKNAME)
    private String nickname;//用户昵称
    @DatabaseField(columnName = COLUMN_NOTE_NAME)
    private String noteName;//备注名
    @DatabaseField(columnName = COLUMN_CONVERSATION_ID)
    private String conversationId = "";
    public User(){

    }
    public User(String noteName, String nickname, String account) {
        this.noteName = noteName;
        this.nickname = nickname;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }
}

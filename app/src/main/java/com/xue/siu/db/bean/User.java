package com.xue.siu.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XUE on 2015/12/10.
 */
@DatabaseTable(tableName = "tb_user")
public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "account")
    private String account;//用户帐号
    @DatabaseField(columnName = "nickname")
    private String nickname;//用户昵称
    @DatabaseField(columnName = "markedname")
    private String markedName;//备注名

    public User(String markedName, String nickname, String account) {
        this.markedName = markedName;
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

    public String getMarkedName() {
        return markedName;
    }

    public void setMarkedName(String markedName) {
        this.markedName = markedName;
    }
}

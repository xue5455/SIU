package com.xue.siu.module.news.model;

import com.avos.avoscloud.AVUser;

/**
 * Created by XUE on 2016/3/2.
 */
public class CommentVO {
    private AVUser fromUser;
    private AVUser toUser;
    private String content;

    public CommentVO() {
    }

    public CommentVO(AVUser fromUser, AVUser toUser, String content) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
    }

    public AVUser getToUser() {
        return toUser;
    }

    public void setToUser(AVUser toUser) {
        this.toUser = toUser;
    }

    public AVUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(AVUser fromUser) {
        this.fromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

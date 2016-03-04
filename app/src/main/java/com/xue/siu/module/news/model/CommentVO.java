package com.xue.siu.module.news.model;

import com.avos.avoscloud.AVUser;

/**
 * Created by XUE on 2016/3/2.
 */
public class CommentVO {
    private AVUser from;
    private AVUser to;
    private String content;

    public CommentVO() {
    }


    public AVUser getFrom() {
        return from;
    }

    public void setFrom(AVUser from) {
        this.from = from;
    }

    public AVUser getTo() {
        return to;
    }

    public void setTo(AVUser to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.xue.siu.module.news.model;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by XUE on 2016/3/2.
 */
public class ActionVO {
    private List<CommentVO> commentList;
    private AVUser creator;
    private long createTime;
    private String content;
    private List<String> picList;
    private List<AVUser> likeList;
    private String location;

    public ActionVO() {
    }

    public List<CommentVO> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentVO> commentList) {
        this.commentList = commentList;
    }

    public AVUser getCreator() {
        return creator;
    }

    public void setCreator(AVUser creator) {
        this.creator = creator;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public List<AVUser> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<AVUser> likeList) {
        this.likeList = likeList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

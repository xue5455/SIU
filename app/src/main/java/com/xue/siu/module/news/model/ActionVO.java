package com.xue.siu.module.news.model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by XUE on 2016/3/2.
 */
public class ActionVO {
    private List<CommentVO> commentList;
    private AVUser creator;
    private long createdAt;
    private String content;
    private List<AVFile> picList;
    private List<AVUser> likeList;
    private String location;
    private String objectId;

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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AVFile> getPicList() {
        return picList;
    }

    public void setPicList(List<AVFile> picList) {
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

    public static ActionVO parse(AVObject object) {
        ActionVO actionVO = new ActionVO();
        actionVO.setCreator((AVUser) object.get("creator"));
        actionVO.setLocation((String) object.get("location"));
        actionVO.setContent((String) object.get("content"));
        actionVO.setCommentList((List<CommentVO>) object.get("commentList"));
        actionVO.setLikeList((List<AVUser>) object.get("likeList"));
        actionVO.setPicList((List<AVFile>) object.get("picList"));
        actionVO.setObjectId(object.getObjectId());
        return actionVO;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}

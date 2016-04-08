package com.xue.siu.module.news.model;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.xue.siu.common.util.LogUtil;

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

    public AVObject toAVObject(String postId) {
        AVObject avObject = new AVObject("Comment");
        avObject.put("from", from);
        avObject.put("to", to);
        avObject.put("content", content);
        AVObject post = AVObject.createWithoutData("Post", postId);
        avObject.put("post", post);
        return avObject;
    }

    public static CommentVO parse(AVObject avObject) {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent((String) avObject.get("content"));
        commentVO.setFrom((AVUser) avObject.getAVObject("from"));
        AVObject to = avObject.getAVObject("to");
        commentVO.setTo((AVUser) to);
        LogUtil.i("xxj", "from : " + commentVO.getFrom().getUsername());
        return commentVO;
    }
}

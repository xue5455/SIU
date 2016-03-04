package com.xue.siu.module.news.model;

import java.util.List;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishEditModel {
    private List<String> localPicList;
    private String content;

    public List<String> getLocalPicList() {
        return localPicList;
    }

    public void setLocalPicList(List<String> localPicList) {
        this.localPicList = localPicList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

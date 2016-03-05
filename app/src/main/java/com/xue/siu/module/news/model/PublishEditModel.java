package com.xue.siu.module.news.model;

import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.List;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishEditModel {
    private List<PhotoInfo> localPicList;
    private String content;

    public List<PhotoInfo> getLocalPicList() {
        return localPicList;
    }

    public void setLocalPicList(List<PhotoInfo> localPicList) {
        this.localPicList = localPicList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.xue.siu.module.follow.model;

import android.os.Bundle;

/**
 * Created by XUE on 2016/1/18.
 */
public class User {
    public static final String BUNDLE_KEY_NAME = "name";
    public static final String BUNDLE_KEY_ID = "userId";
    public static final String BUNDLE_KEY_URL = "url";
    private String mName;
    private String mUserId;
    private String mUrl = "http://cdn.duitang.com/uploads/item/201503/17/20150317091106_BeVfy.thumb.224_0.jpeg";//头像地址

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_ID, mUserId);
        bundle.putString(BUNDLE_KEY_NAME, mName);
        bundle.putString(BUNDLE_KEY_URL, mUrl);
        return bundle;
    }
}

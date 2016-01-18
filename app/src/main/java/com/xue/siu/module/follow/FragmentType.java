package com.xue.siu.module.follow;

import android.text.TextUtils;

/**
 * Created by XUE on 2016/1/18.
 */
public enum FragmentType {
    FolloweeFragment("Followee"),
    FollowerFragment("Follower");

    private String value;

    private FragmentType(String type) {
        value = type;
    }
    @Override
    public String toString(){
        return value;
    }
    public static FragmentType getType(String value){
        if(TextUtils.equals(value,FolloweeFragment.toString()))
            return FolloweeFragment;
        else if(TextUtils.equals(value,FollowerFragment.toString()))
            return FollowerFragment;
        throw new RuntimeException("unknown fragment type");
    }
}

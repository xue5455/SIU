package com.xue.siu.module.userpage.usertype;

/**
 * Created by XUE on 2016/1/26.
 */
public enum FriendshipType {
    FOLLOWEE(1),
    FOLLOWER(2),
    NONE(3);

    private int value;

    private FriendshipType(int value) {
        this.value = value;
    }

}

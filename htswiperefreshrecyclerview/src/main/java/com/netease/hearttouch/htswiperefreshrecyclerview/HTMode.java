package com.netease.hearttouch.htswiperefreshrecyclerview;

/**
 * Created by stone on 15/9/24.
 */
public enum HTMode {
    /**
     * 不允许刷新
     */
    DISABLE(1),

    /**
     * 允许下拉刷新
     */
    REFRESH(1 << 1),

    /**
     * 允许加载更多
     */
    LOADMORE(1 << 2),

    /**
     * 允许下拉刷新和加载更多
     */
    BOTH(6);

    private int mValue;

    HTMode(int value) {
        this.mValue = value;
    }

    public static HTMode getFromInt(int value) {
        for (HTMode direction : HTMode.values()) {
            if (direction.mValue == value) {
                return direction;
            }
        }
        return REFRESH;//返回默认值为下拉刷新
    }
}

package com.netease.hearttouch.htswiperefreshrecyclerview;

/**
 * Created by stone on 15/11/21.
 */
public interface LoadMoreUIChangeListener {
    /**
     * 开始加载更多时,添加操作
     */
    void onBeginLoadingMore(boolean hasMore);


    /**
     * 结束加载更多时,添加操作
     */
    void onEndLoadingMore(boolean hasMore);
}

package com.netease.hearttouch.htswiperefreshrecyclerview;

/**
 * 下拉刷新监听接口,可以控制刷新视图在不同的阶段进行不同的操作
 * Created by stone on 15/9/25.
 */

public interface RefreshUIChangeListener {
    /**
     * 进入到未处理下拉刷新状态
     */
     void onUIReset();
    /**
     * 进入下拉状态
     */
     void onUIRefreshBegin();

    /**
     * 进入释放刷新状态
     */
     void onUIReleaseRefresh();

    /**
     * 进入正在刷新状态
     */
    void onUIRefreshing();

    /**
     * 结束下拉刷新
     */
     void onUIRefreshComplete();
    /**
     * 下拉刷新控件可见时，处理上下拉进度
     *
     * @param scale         下拉过程0 到 1，回弹过程1 到 0，没有加上弹簧距离移动时的比例
     * @param moveYDistance 整个下拉刷新控件paddingTop变化的值，如果有弹簧距离，会大于整个下拉刷新控件的高度
     */
     void onUIPositionChange(float scale, int moveYDistance);

}

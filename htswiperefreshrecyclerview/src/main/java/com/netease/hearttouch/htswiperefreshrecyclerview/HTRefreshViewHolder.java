package com.netease.hearttouch.htswiperefreshrecyclerview;

import android.content.Context;
import android.view.View;

/**
 * Created by stone on 15/9/25.
 * 继承该抽象类实现响应的抽象方法，做出各种下拉刷新效果
 */
public abstract class HTRefreshViewHolder implements RefreshUIChangeListener, LoadMoreUIChangeListener {

    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值
     */
    private static final float PULL_DISTANCE_SCALE = 1.8f;
    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值
     */
    private static final float SPRING_DISTANCE_SCALE = 2.4f;

    /**
     * 默认动画时间，单位ms
     */
    private static final int DEFAULT_ANIMATION_TIME = 500;

    protected Context mContext;
    /**
     * 下拉刷新上拉加载更多控件
     */
    protected HTSwipeRecyclerView mRefreshLayout;
    /**
     * 下拉刷新控件
     */
    // TODO 命名改下吧，不一定是下拉刷新，如果recycleview的方向改变了，那应该就是上拉刷新了
    protected View mRefreshHeaderView;
    /**
     * 上拉加载更多控件
     */
    // TODO 命名改下吧，同上
    protected View mLoadMoreFooterView;

    /**
     * 下拉刷新控件的背景资源id
     */
    protected int mRefreshViewBackgroundResId = -1;

    /**
     * 加载更多背景资源id
     */
    protected int mloadMoreViewBackgroundResId = -1;
    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值，默认1.8f
     */
    private float mPullDistanceScale = PULL_DISTANCE_SCALE;
    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值，默认2.4f
     */
    private float mSpringDistanceScale = SPRING_DISTANCE_SCALE;
    /**
     * 头部控件移动动画时常
     */
    private int mTopAnimDuration = DEFAULT_ANIMATION_TIME;

    public HTRefreshViewHolder(Context context) {
        mContext = context;
        mRefreshHeaderView = getRefreshHeaderView();
        mLoadMoreFooterView = getLoadMoreFooterView();
    }

    public int getRefreshViewBackgroundResId() {
        return mRefreshViewBackgroundResId;
    }

    public void setRefreshViewBackgroundResId(int refreshViewBackgroundResId) {
        mRefreshViewBackgroundResId = refreshViewBackgroundResId;
    }

    public int getloadMoreViewBackgroundResId() {
        return mloadMoreViewBackgroundResId;
    }

    public void setloadMoreViewBackgroundResId(int mloadMoreViewBackgroundResId) {
        this.mloadMoreViewBackgroundResId = mloadMoreViewBackgroundResId;
    }

    /**
     * 获取顶部未满足下拉刷新条件时回弹到初始状态、满足刷新条件时回弹到正在刷新状态、刷新完毕后回弹到初始状态的动画时间，默认为500毫秒
     */
    public int getTopAnimDuration() {
        return mTopAnimDuration;
    }

    /**
     * 设置顶部未满足下拉刷新条件时回弹到初始状态、满足刷新条件时回弹到正在刷新状态、刷新完毕后回弹到初始状态的动画时间，默认为300毫秒
     */
    public void setTopAnimDuration(int topAnimDuration) {
        // TODO 做下安全判断吧，topAnimDuration <= 0的话，要怎么处理？
        mTopAnimDuration = topAnimDuration;
    }

    /**
     * 获取上拉加载更多控件，如果不喜欢这种上拉刷新风格可重写该方法实现自定义LoadMoreFooterView
     */
    public abstract View getLoadMoreFooterView();

    /**
     * 获取头部下拉刷新控件
     */
    public abstract View getRefreshHeaderView();


    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值
     */
    public float getPaddingTopScale() {
        return mPullDistanceScale;
    }

    /**
     * 设置手指移动距离与下拉刷新控件paddingTop移动距离的比值
     */
    public void setPullDistanceScale(float pullDistanceScale) {
        mPullDistanceScale = pullDistanceScale;
    }

    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值
     */
    public float getSpringDistanceScale() {
        return mSpringDistanceScale;
    }

    /**
     * 设置下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值，不能小于0，如果刷新控件比较高，建议将该值设置小一些
     */
    public void setSpringDistanceScale(float springDistanceScale) {
        if (springDistanceScale < 0) {
            throw new RuntimeException("下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值springDistanceScale不能小于0");
        }
        mSpringDistanceScale = springDistanceScale;
    }

    /**
     * 进入加载更多状态
     */
    @Override
    public abstract void onBeginLoadingMore(boolean hasMore);

    /**
     * 结束上拉加载更多
     */
    @Override
    public abstract void onEndLoadingMore(boolean hasMore);

    /**
     * 获取下拉刷新控件的高度，如果初始化时的高度和最后展开的最大高度不一致，需重写该方法返回最大高度
     */
    public int getRefreshHeaderViewHeight() {
        if (mRefreshHeaderView != null) {
            // 测量下拉刷新控件的高度
            // TODO int measureSpec = View.MeasureSpec.AT_MOST | (~(0x3 << 30));
            mRefreshHeaderView.measure(0, 0);
            return mRefreshHeaderView.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * 获取加载刷新控件的高度，如果初始化时的高度和最后展开的最大高度不一致，需重写该方法返回最大高度
     */
    public int getLoadMoreFooterHeight() {
        if (mLoadMoreFooterView != null) {
            // 测量下拉刷新控件的高度
            // TODO int measureSpec = View.MeasureSpec.AT_MOST | (~(0x3 << 30));
            mLoadMoreFooterView.measure(0, 0);
            return mLoadMoreFooterView.getMeasuredHeight();
        }
        return 0;
    }

    public void setRefreshLayout(HTSwipeRecyclerView refreshLayout) {
        mRefreshLayout = refreshLayout;
    }


    @Override
    public abstract void onUIReset();

    @Override
    public abstract void onUIRefreshBegin();

    @Override
    public abstract void onUIReleaseRefresh();

    @Override
    public abstract void onUIRefreshing();

    @Override
    public abstract void onUIRefreshComplete();


    @Override
    public abstract void onUIPositionChange(float scale, int moveYDistance);
}

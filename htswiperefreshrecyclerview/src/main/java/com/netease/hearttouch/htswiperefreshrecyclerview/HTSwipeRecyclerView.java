package com.netease.hearttouch.htswiperefreshrecyclerview;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by stone on 15/11/13.
 */
public class HTSwipeRecyclerView extends LinearLayout {
    private static final String TAG = HTSwipeRecyclerView.class.getSimpleName();

    private static Class<? extends HTRefreshViewHolder> mViewHolderClass;//设置全局的默认刷新样式
    private HTRefreshViewHolder mHTRefreshViewHolder;//设置刷新样式

    /**
     * 维护自定义滚动接口列表
     */
    private ArrayList<OnScrollListener> mOnScrollListeners = new ArrayList<>();
    /**
     * 监听布局高度和宽度的变化
     */
    private OnLayoutSizeChangedListener mOnLayoutSizeChangedListener;
    /**
     * 一些xml可已进行定义的属性值
     */
    private int mScrollbarStyle;
    private boolean mClipToPadding;
    private boolean mDebug = false;
    private HTMode mMode;
    private int mSpanCount;
    private boolean mReverseLayout;
    private boolean mStackFromEnd;

    /**
     * 自定义的包裹的adapter从而支持下拉刷新
     */
    private HTWrapperAdapter mHTWrapperAdapter;

    /**
     * 用户设置的真正的adapter
     */
    private RecyclerView.Adapter mInnerAdapter;

    /**
     * 整个头部控件，包裹下拉刷新控件
     */
    private LinearLayout mHeaderView;

    /**
     * 整个底部控件，包裹下拉刷新控件
     */
    private LinearLayout mFooterView;
    /**
     * 下拉刷新控件
     */
    private View mRefreshHeaderView;
    /**
     * 当前刷新状态
     */
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.IDLE;
    /**
     * 上拉加载更多控件
     */
    private View mLoadMoreFooterView;
    /**
     * 上拉加载更多控件的高度,用padding值来隐藏\显示加载更多视图
     */
    private int mLoadMoreFooterViewHeight;
    /**
     * 下拉刷新代理
     */
    private OnRefreshListener mRefreshDelegate;
    /**
     * 上拉加载更多代理
     */
    private OnLoadMoreListener mLoadMoreDelegate;
    /**
     * 整个头部控件最小的paddingTop
     */
    private int mMinHeaderViewPaddingTop;
    /**
     * 整个头部控件最大的paddingTop
     */
    private int mMaxHeaderViewPaddingTop;

    /**
     * 是否处于正在加载更多状态
     */
    private boolean mIsLoadingMore = false;

    private float mInterceptTouchDownX = -1;

    private float mInterceptTouchDownY = -1;
    private int mRefreshDownY = -1;
    private int mTouchSlop;

    private boolean hasMore = true;
    private boolean showNoMoreNotice = false;//时候显示没有加载项的提示

    private RecyclerView mRecyclerView;
    private Runnable mDelayHiddenLoadingMoreViewTask = new Runnable() {
        @Override
        public void run() {
            ValueAnimator animator = ValueAnimator.ofInt(mFooterView.getPaddingBottom(), -mLoadMoreFooterViewHeight);
            animator.setDuration(mHTRefreshViewHolder.getTopAnimDuration());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int paddingBottom = (int) animation.getAnimatedValue();
                    mFooterView.setPadding(0, 0, 0, paddingBottom);
                }
            });
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isItemEnough()) {//如果满一屏幕,加载动画结束后,显示
                        changeLoadMoreFooterView(false);
                    }
                    mHTRefreshViewHolder.onEndLoadingMore(true);
                    mIsLoadingMore = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }
    };

    public HTSwipeRecyclerView(Context context) {
        this(context, null, 0);
    }

    public HTSwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTSwipeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            initAttrs(attrs);
        }
        initViews();
    }

    /**
     * 设置全局默认的属性样式
     */
    public static void initRefreshViewHolder(Class clazz) {
        mViewHolderClass = clazz;
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        setOrientation(LinearLayout.VERTICAL);//目前支持垂直方向的刷新
        removeAllViews();
        initHeaderAndFooterView();
        mRecyclerView = new RecyclerView(getContext());
        addView(mRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initRecyclerView();
        if (mViewHolderClass != null) {//如果设置了全局的默认刷新样式,就初始化
            try {
                Constructor constructor = mViewHolderClass.getConstructor(Context.class);
                setRefreshViewHolder((HTRefreshViewHolder) constructor.newInstance(getContext()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化xml中设置的参数
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HTSwipeRecyclerView);
        try {
            mScrollbarStyle = typedArray.getInt(R.styleable.HTSwipeRecyclerView_htScrollbarStyle, -1);
          /*  mPadding = (int) typedArray.getDimension(R.styleable.HSwipeRefreshLayout_recyclerviewPadding, 0.0f);
            mPaddingTop = (int) typedArray.getDimension(R.styleable.HSwipeRefreshLayout_recyclerviewPaddingTop, 0.0f);
            mPaddingBottom = (int) typedArray.getDimension(R.styleable.HSwipeRefreshLayout_recyclerviewPaddingBottom, 0.0f);
            mPaddingLeft = (int) typedArray.getDimension(R.styleable.HSwipeRefreshLayout_recyclerviewPaddingLeft, 0.0f);
            mPaddingRight = (int) typedArray.getDimension(R.styleable.HSwipeRefreshLayout_recyclerviewPaddingRight, 0.0f);*/
            mSpanCount = typedArray.getInt(R.styleable.HTSwipeRecyclerView_htSpanCount, 1);
            mReverseLayout = typedArray.getBoolean(R.styleable.HTSwipeRecyclerView_htReverseLayout, false);
            mStackFromEnd = typedArray.getBoolean(R.styleable.HTSwipeRecyclerView_htStackFromEnd, false);
            mClipToPadding = typedArray.getBoolean(R.styleable.HTSwipeRecyclerView_htClipToPadding, false);
            mDebug = typedArray.getBoolean(R.styleable.HTSwipeRecyclerView_htDebug, false);
            mMode = HTMode.getFromInt(typedArray.getInt(R.styleable.HTSwipeRecyclerView_htMode, 2));
        } finally {
            typedArray.recycle();
        }
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 初始化整个头部控件
     */
    private void initHeaderAndFooterView() {
        mHeaderView = new LinearLayout(getContext());
        mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mHeaderView.setOrientation(LinearLayout.VERTICAL);
        addView(mHeaderView);
        mFooterView = new LinearLayout(getContext());
        mFooterView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mFooterView.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 初始化RecyclerView的属性并添加滚动监听事件
     */
    private void initRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setClipToPadding(mClipToPadding);
            //初始化设置的xml属性到LayoutManager
            RecyclerView.LayoutManager layout = getLayoutManager();
            if (layout instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layout).setReverseLayout(mReverseLayout);
                ((LinearLayoutManager) layout).setStackFromEnd(mStackFromEnd);
                if (layout instanceof GridLayoutManager) {
                    ((GridLayoutManager) layout).setSpanCount(mSpanCount);
                }
            } else if (layout instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) layout).setReverseLayout(mReverseLayout);
                ((StaggeredGridLayoutManager) layout).setSpanCount(mSpanCount);
            }
            if (mScrollbarStyle != -1) {
                mRecyclerView.setScrollBarStyle(mScrollbarStyle);
            }
        }
        setRecyclerViewOnScrollListener();
        //setRecyclerViewOnTouchListener();
    }

    public void setRefreshViewHolder(HTRefreshViewHolder refreshViewHolder) {
        mHTRefreshViewHolder = refreshViewHolder;
        if (mHTRefreshViewHolder != null) {
            initRefreshHeaderView();
            initLoadMoreFooterView();
        }
    }

    public void startChangeHeaderViewPaddingTop(int distance) {
        ValueAnimator animator = ValueAnimator.ofInt(mHeaderView.getPaddingTop(), mHeaderView.getPaddingTop() - distance);
        animator.setDuration(mHTRefreshViewHolder.getTopAnimDuration());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int) animation.getAnimatedValue();
                mHeaderView.setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.start();
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initRefreshHeaderView() {
        mRefreshHeaderView = mHTRefreshViewHolder.getRefreshHeaderView();
        if (mRefreshHeaderView != null) {
            /*下拉刷新控件的高度*/
            int refreshHeaderViewHeight = mHTRefreshViewHolder.getRefreshHeaderViewHeight();
            mMinHeaderViewPaddingTop = -refreshHeaderViewHeight;
            mMaxHeaderViewPaddingTop = (int) (refreshHeaderViewHeight * mHTRefreshViewHolder.getSpringDistanceScale());
            if (mRefreshHeaderView.getParent() != null)
                ((ViewGroup) mRefreshHeaderView.getParent()).removeView(mRefreshHeaderView);
            mHeaderView.setPadding(0, mMinHeaderViewPaddingTop, 0, 0);
            int res = mHTRefreshViewHolder.getRefreshViewBackgroundResId();
            if (res != -1) {
                mHeaderView.setBackgroundResource(res);
            }
            mHeaderView.removeAllViews();//移除之前的视图
            mHeaderView.addView(mRefreshHeaderView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 初始化上拉加载更多控件
     */
    private void initLoadMoreFooterView() {
        mLoadMoreFooterView = mHTRefreshViewHolder.getLoadMoreFooterView();
        if (mLoadMoreFooterView.getParent() != null)
            ((ViewGroup) mLoadMoreFooterView.getParent()).removeView(mLoadMoreFooterView);
        if (mLoadMoreFooterView != null) {
            // 测量上拉加载更多控件的高度
            mLoadMoreFooterView.measure(0, 0);
            mLoadMoreFooterViewHeight = mHTRefreshViewHolder.getLoadMoreFooterHeight();
            changeLoadMoreFooterView(false);
            int res = mHTRefreshViewHolder.getloadMoreViewBackgroundResId();
            if (res != -1) {
                mLoadMoreFooterView.setBackgroundResource(res);
            }
            mFooterView.removeAllViews();
            mFooterView.addView(mLoadMoreFooterView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * RecyclerView Bug：IndexOutOfBoundsException: Inconsistency detected. Invalid item position
     * 解决方法
     * 参见: http://drakeet.me/recyclerview-bug-indexoutofboundsexception-inconsistency-detected-invalid-item-position-solution
     */
    private void setRecyclerViewOnTouchListener() {
        if (mRecyclerView != null) {
            mRecyclerView.setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return mCurrentRefreshStatus == RefreshStatus.REFRESHING;
                        }
                    }
            );
        }
    }

    private void setRecyclerViewOnScrollListener() {
        if (mRecyclerView != null) {
            RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (recyclerView.getAdapter() != null && !(recyclerView.getAdapter() instanceof HTWrapperAdapter))
                        if (mDebug) {
                            Log.w(TAG, "Adapter 类型不正确,加载更多显示会出错");
                        }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && shouldHandleRecyclerViewLoadingMore()) {
                        beginLoadingMore();
                    }
                    synchronized (this) {
                        for (OnScrollListener listener : mOnScrollListeners) {
                            listener.onScrollStateChanged(recyclerView, newState);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    synchronized (this) {
                        for (OnScrollListener listener : mOnScrollListeners) {
                            listener.onScrolled(recyclerView, dx, dy);
                        }
                    }
                }
            };
            mRecyclerView.addOnScrollListener(listener);
        }
    }

    /**
     * 设置刷新控件的背景色
     */
    public void setRefreshViewBackgroundResId(int refreshViewBackgroundResId) {
        if (mHTRefreshViewHolder != null) {
            mHTRefreshViewHolder.setRefreshViewBackgroundResId(refreshViewBackgroundResId);
        }
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.setBackgroundResource(refreshViewBackgroundResId);
        }
    }

    /**
     * 设置加载更多控件的背景色
     */
    public void setLoadMoreViewBackgroundRes(int loadMoreViewBackgroundResId) {
        if (mHTRefreshViewHolder != null) {
            mHTRefreshViewHolder.setloadMoreViewBackgroundResId(loadMoreViewBackgroundResId);
        }
        if (mFooterView != null) {
            mLoadMoreFooterView.setBackgroundResource(loadMoreViewBackgroundResId);
        }
    }

    /**
     * 是否达到加载更多的条件
     */
    private boolean shouldHandleRecyclerViewLoadingMore() {
        if (mMode == HTMode.DISABLE || mMode == HTMode.REFRESH || mIsLoadingMore ||
                mCurrentRefreshStatus == RefreshStatus.REFRESHING || mLoadMoreDelegate == null) {
            return false;
        }
        if (mRecyclerView.getAdapter() == null || mRecyclerView.getAdapter().getItemCount() == 0) {
            return false;
        }
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager == null || manager.getItemCount() == 0) {
            return false;
        }
        int firstVisibleItem = getFirstVisibleItemPosition(manager);
        int visibleItemCount = manager.getChildCount();
//        if (visibleItemCount >= manager.getItemCount()) return false;//未满一屏幕
        return firstVisibleItem + visibleItemCount >= manager.getItemCount();
    }

    /**
     * 获取第一个item的位置索引
     */
    private int getFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisibleItemPosition = -1;
        int[] firstScrollPositions;
        if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            firstScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findFirstVisibleItemPositions(firstScrollPositions);
            firstVisibleItemPosition = firstScrollPositions[0];
        }
        return firstVisibleItemPosition;
    }

    /**
     * 获取最后一个item的索引
     */
    public int getLastVisibleItemPosition() {
        if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null) {
            return 0;
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int lastVisibleItemPosition = -1;
        int[] lastScrollPositions;
        if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastScrollPositions);
            lastVisibleItemPosition = lastScrollPositions[lastScrollPositions.length - 1];
        }
        return lastVisibleItemPosition;
    }

    /**
     * 是否符合显示加载更多item的条件,初始化时,不满一屏幕,不显示footerView
     */
    public boolean isItemEnough() {
        if (mRecyclerView == null || mRecyclerView.getLayoutManager() == null) {
            return false;
        }

        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        return (mMode == HTMode.BOTH || mMode == HTMode.LOADMORE) &&
                mCurrentRefreshStatus != RefreshStatus.REFRESHING &&
                (mRecyclerView.canScrollVertically(-1) || manager.getChildCount() + 1 < manager.getItemCount());
    }


    public boolean isShowNoMoreNotice() {
        // TODO hasMore 为 true，那是不是应该显示的是“加载更多”，而不是“加载完毕”
        return hasMore || showNoMoreNotice;
    }

    public void setShowNoMoreNotice(boolean showNoMoreNotice) {
        this.showNoMoreNotice = showNoMoreNotice;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInterceptTouchDownX = event.getRawX();
                mInterceptTouchDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsLoadingMore && (mCurrentRefreshStatus != RefreshStatus.REFRESHING)) {
                    if (mInterceptTouchDownX == -1) {
                        mInterceptTouchDownX = (int) event.getRawX();
                    }
                    if (mInterceptTouchDownY == -1) {
                        mInterceptTouchDownY = (int) event.getRawY();
                    }
                    int interceptTouchMoveDistanceY = (int) (event.getRawY() - mInterceptTouchDownY);
                    if (Math.abs(event.getRawX() - mInterceptTouchDownX) < Math.abs(interceptTouchMoveDistanceY) && mRefreshHeaderView != null) {
                        if ((interceptTouchMoveDistanceY > mTouchSlop && shouldHandleRefresh())) {
                            // ACTION_DOWN时没有消耗掉事件，子控件会处于按下状态，这里设置ACTION_CANCEL，使子控件取消按下状态
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            super.onInterceptTouchEvent(event);
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 重置
                mInterceptTouchDownX = -1;
                mInterceptTouchDownY = -1;
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否满足处理刷新的条件
     */
    private boolean shouldHandleRefresh() {
        if (mMode == HTMode.DISABLE || mMode == HTMode.LOADMORE ||
                mIsLoadingMore || mCurrentRefreshStatus == RefreshStatus.REFRESHING ||
                mRefreshHeaderView == null || mRefreshDelegate == null) {
            return false;
        }

        if (mRecyclerView != null) {
            int firstChildTop = 0;
            if (mRecyclerView.getChildCount() > 0) {
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top
                // 解决item的topMargin不为0时不能触发下拉刷新
                MarginLayoutParams layoutParams = (MarginLayoutParams) mRecyclerView.getChildAt(0).getLayoutParams();
                firstChildTop = mRecyclerView.getChildAt(0).getTop() - layoutParams.topMargin - mRecyclerView.getPaddingTop();
            }

            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();

            if (manager == null || manager.getItemCount() == 0) {
                return true;
            }

            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && firstChildTop == 0) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

                int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                if (out[0] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRefreshHeaderView != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mRefreshDownY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (handleActionMove(event)) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (handleActionUpOrCancel(event)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 处理手指滑动事件
     *
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionMove(MotionEvent event) {
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING || mIsLoadingMore) {
            return false;
        }
        if (mRefreshDownY == -1) {
            mRefreshDownY = (int) event.getY();
        }

        int refreshDiffY = (int) event.getY() - mRefreshDownY;
        refreshDiffY = (int) (refreshDiffY / mHTRefreshViewHolder.getPaddingTopScale());
        // 如果是向下拉，并且当前可见的第一个条目的索引等于0，才处理整个头部控件的padding
        if (refreshDiffY > 0 && shouldHandleRefresh()) {
            int paddingTop = mMinHeaderViewPaddingTop + refreshDiffY;
            if (paddingTop > 0 && mCurrentRefreshStatus != RefreshStatus.RELEASE_REFRESH) {
                // 下拉刷新控件完全显示，并且当前状态没有处于释放开始刷新状态
                mCurrentRefreshStatus = RefreshStatus.RELEASE_REFRESH;
                handleRefreshStatusChanged();
                mHTRefreshViewHolder.onUIPositionChange(1.0f, refreshDiffY);
            } else if (paddingTop < 0) {
                // 下拉刷新控件没有完全显示，并且当前状态没有处于下拉刷新状态
                if (mCurrentRefreshStatus != RefreshStatus.PULL_DOWN) {
                    boolean isPreRefreshStatusNotIdle = mCurrentRefreshStatus != RefreshStatus.IDLE;
                    mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
                    if (isPreRefreshStatusNotIdle) {
                        handleRefreshStatusChanged();
                    }
                }
                float scale = 1 - paddingTop * 1.0f / mMinHeaderViewPaddingTop;
                /**
                 * 往下滑
                 * paddingTop    mMinHeaderViewPaddingTop 到 0
                 * scale         0 到 1
                 * 往上滑
                 * paddingTop    0 到 mMinHeaderViewPaddingTop
                 * scale         1 到 0
                 */
                mHTRefreshViewHolder.onUIPositionChange(scale, refreshDiffY);
            }
            paddingTop = Math.min(paddingTop, mMaxHeaderViewPaddingTop);
            mHeaderView.setPadding(0, paddingTop, 0, 0);
            return true;
        }
        return false;
    }

    /**
     * 处理手指抬起事件
     *
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionUpOrCancel(MotionEvent event) {
        boolean isReturnTrue = false;
        // 如果当前头部刷新控件没有完全隐藏，则需要返回true，自己消耗ACTION_UP事件
        if (mHeaderView.getPaddingTop() != mMinHeaderViewPaddingTop) {
            isReturnTrue = true;
        }
        if (mCurrentRefreshStatus == RefreshStatus.PULL_DOWN || mCurrentRefreshStatus == RefreshStatus.IDLE) {
            // 处于下拉刷新状态，松手时隐藏下拉刷新控件
            hiddenRefreshHeaderView();
            mCurrentRefreshStatus = RefreshStatus.IDLE;
            handleRefreshStatusChanged();
        } else if (mCurrentRefreshStatus == RefreshStatus.RELEASE_REFRESH) {
            // 处于松开进入刷新状态，松手时完全显示下拉刷新控件，进入正在刷新状态
            beginRefreshing();
        }

        mRefreshDownY = -1;
        return isReturnTrue;
    }

    /**
     * 处理下拉刷新控件状态变化
     */
    private void handleRefreshStatusChanged() {
        switch (mCurrentRefreshStatus) {
            case IDLE:
                mHTRefreshViewHolder.onUIReset();
                if (mDebug) {
                    Log.d(TAG, "mCurrentRefreshStatus = IDLE");
                }
                break;
            case PULL_DOWN:
                mHTRefreshViewHolder.onUIRefreshBegin();
                if (mDebug) {
                    Log.d(TAG, "mCurrentRefreshStatus = PULL_DOWN");
                }
                break;
            case RELEASE_REFRESH:
                mHTRefreshViewHolder.onUIReleaseRefresh();
                if (mDebug) {
                    Log.d(TAG, "mCurrentRefreshStatus = RELEASE_REFRESH");
                }
                break;
            case REFRESHING:
                mHTRefreshViewHolder.onUIRefreshing();
                if (mDebug) {
                    Log.d(TAG, "mCurrentRefreshStatus = REFRESHING");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 触发自动下拉刷新
     */
    public void setAutoRefresh() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
                layoutManager.scrollToPosition(0);
            }
            mHTRefreshViewHolder.onUIPositionChange(1.0f, mMaxHeaderViewPaddingTop);
            beginRefreshing();
        }
    }

    /**
     * 触发自动加载更多
     */
    public void setAutoLoadMore() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            beginLoadingMore();
            if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
                layoutManager.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }
    }

    /**
     * 是否隐藏加载更多视图
     */
    public void changeLoadMoreFooterView(boolean isHide) {
        if (mFooterView != null) {
            mFooterView.setPadding(0, 0, 0, isHide ? -mLoadMoreFooterViewHeight : 0);
        }
    }

    /**
     * 切换到正在刷新状态
     */
    private void beginRefreshing() {
        if (mCurrentRefreshStatus != RefreshStatus.REFRESHING && mRefreshDelegate != null) {
            mCurrentRefreshStatus = RefreshStatus.REFRESHING;
            changeRefreshHeaderViewToZero();
            handleRefreshStatusChanged();
            mRefreshDelegate.onRefresh();
            changeLoadMoreFooterView(true);//刷新的时候重置下footerView
        }
    }

    /**
     * 结束下拉刷新
     */
    public void endRefreshing() {
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
            mCurrentRefreshStatus = RefreshStatus.IDLE;
            hiddenRefreshHeaderView();
            mHTRefreshViewHolder.onUIRefreshComplete();
        }
    }

    /**
     * 隐藏下拉刷新控件，带动画
     */
    private void hiddenRefreshHeaderView() {
        ValueAnimator animator = ValueAnimator.ofInt(mHeaderView.getPaddingTop(), mMinHeaderViewPaddingTop);
        animator.setDuration(mHTRefreshViewHolder.getTopAnimDuration());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int) animation.getAnimatedValue();
                mHeaderView.setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                handleRefreshStatusChanged();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                handleRefreshStatusChanged();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 设置下拉刷新控件的paddingTop到0，带动画
     */
    private void changeRefreshHeaderViewToZero() {
        ValueAnimator animator = ValueAnimator.ofInt(mHeaderView.getPaddingTop(), 0);
        animator.setDuration(mHTRefreshViewHolder.getTopAnimDuration());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int) animation.getAnimatedValue();
                mHeaderView.setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.start();
    }

    /**
     * 开始上拉加载更多
     */
    private void beginLoadingMore() {
        if (!mIsLoadingMore && mLoadMoreDelegate != null) {
            mHTRefreshViewHolder.onBeginLoadingMore(hasMore);
            if (hasMore) {//只有加载更多的时候
                if (mDebug) {
                    Log.d(TAG, "begin Load more");
                }
                changeLoadMoreFooterView(false);
                showLoadingMoreView();
                mIsLoadingMore = true;
                mLoadMoreDelegate.onLoadMore();
            } else {
                if (mDebug) {
                    Log.d(TAG, "has no more");
                }
            }
        }
    }

    private void autoHideLoadMoreView() {
        postDelayed(mDelayHiddenLoadingMoreViewTask, 300);
    }

    /**
     * 显示上拉加载更多控件
     */
    private void showLoadingMoreView() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
                layoutManager.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }
    }

    /**
     * 结束上拉加载更多
     */
    public void endLoadingMore() {
        if (mIsLoadingMore) {
            if (mDebug) {
                Log.d(TAG, "loadMore is completed");
            }
            if (hasMore) {//还有更多时,网络出错或加载出错,会缩回去
                autoHideLoadMoreView();
            } else {
                mHTRefreshViewHolder.onEndLoadingMore(false);//没有更多时,直接改变文案
                mIsLoadingMore = false;
            }
        }
    }

    /**
     * 兼容之前写的代码,默认不会触发加载更多
     */
    @Deprecated
    public void setRefreshComplete() {
        this.hasMore = false;
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
            endRefreshing();
        } else if (mIsLoadingMore) {
            endLoadingMore();
        }
    }

    /**
     * 关闭加载状态
     *
     * @param hasMore hasMore:false,不触发加载更多
     */
    public void setRefreshComplete(boolean hasMore) {
        this.hasMore = hasMore;
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
            endRefreshing();
        } else if (mIsLoadingMore) {
            endLoadingMore();
        }
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mRefreshDelegate = onRefreshListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mLoadMoreDelegate = onLoadMoreListener;
    }


    public LinearLayout getFooterView() {
        return mFooterView;
    }

    public LinearLayout getHeaderView() {
        return mHeaderView;
    }

    /**
     * 返回mRecyclerView
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    /**
     * Set the LayoutManager that this RecyclerView will use.
     * <p/>
     * <p>In contrast to other adapter-backed views such as {@link android.widget.ListView}
     * or {@link android.widget.GridView}, RecyclerView allows client code to provide custom
     * layout arrangements for child views. These arrangements are controlled by the
     * LayoutManager. A LayoutManager must be provided for RecyclerView to function.</p>
     * <p/>
     * <p>Several default strategies are provided for common uses such as lists and grids.</p>
     *
     * @param layout LayoutManager to use
     */
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }

    /**
     * Gets the current ItemAnimator for this RecyclerView. A null return value
     * indicates that there is no animator and that item changes will happen without
     * any animations. By default, RecyclerView instantiates and
     * uses an instance of DefaultItemAnimator.
     *
     * @return ItemAnimator The current ItemAnimator. If null, no animations will occur
     * when changes occur to the items in this RecyclerView.
     */
    public RecyclerView.ItemAnimator getItemAnimator() {
        return getRecyclerView().getItemAnimator();
    }

    /**
     * Sets the {@link RecyclerView.ItemAnimator} that will handle animations involving changes
     * to the items in this RecyclerView. By default, RecyclerView instantiates and
     * uses an instance of DefaultItemAnimator. Whether item animations are
     * enabled for the RecyclerView depends on the ItemAnimator and whether
     * the LayoutManager supports item animations.
     *
     * @param animator The ItemAnimator being set. If null, no animations will occur
     *                 when changes occur to the items in this RecyclerView.
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        getRecyclerView().setItemAnimator(animator);
    }

    /**
     * Add an ItemDecoration to this RecyclerView. Item decorations can
     * affect both measurement and drawing of individual item views.
     * <p/>
     * <p>Item decorations are ordered. Decorations placed earlier in the list will
     * be run/queried/drawn first for their effects on item views. Padding added to views
     * will be nested; a padding added by an earlier decoration will mean further
     * item decorations in the list will be asked to draw/pad within the previous decoration's
     * given area.</p>
     *
     * @param decor Decoration to add
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        getRecyclerView().addItemDecoration(decor);
    }

    /**
     * Remove an ItemDecoration from this RecyclerView.
     * <p/>
     * <p>The given decoration will no longer impact the measurement and drawing of
     * item views.</p>
     *
     * @param decor Decoration to remove
     * @see #addItemDecoration(RecyclerView.ItemDecoration)
     */
    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        getRecyclerView().removeItemDecoration(decor);
    }

    /**
     * RecyclerView can perform several optimizations if it can know in advance that changes in
     * adapter content cannot change the size of the RecyclerView itself.
     * If your use of RecyclerView falls into this category, set this to true.
     *
     * @param hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
     */
    public void setHasFixedSize(boolean hasFixedSize) {
        getRecyclerView().setHasFixedSize(hasFixedSize);
    }

    /**
     * @return true if the app has specified that changes in adapter content cannot change
     * the size of the RecyclerView itself.
     */

    public boolean hasFixedSize() {
        return getRecyclerView().hasFixedSize();
    }

    /**
     * Retrieves the previously set adapter or null if no adapter is set.
     *
     * @return The previously set adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return getRecyclerView().getAdapter();
    }

    /**
     * Set a new adapter to provide child views on demand.
     * <p/>
     * When adapter is changed, all existing views are recycled back to the pool. If the pool has
     * only one adapter, it will be cleared.
     *
     * @param adapter The new adapter to set, or null to set no adapter.
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) return;
        // 使用包装了头部和脚部的适配器
        mInnerAdapter = adapter;
        mHTWrapperAdapter = new HTWrapperAdapter(adapter, this);
            /*mHTWrapperAdapter.addHeader(mHeaderView);*/
    /*    if (mHTRefreshViewHolder != null)
            mHTWrapperAdapter.addFooterView(mFooterView);*/
        //对Grid布局进行支持
        final RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mHTWrapperAdapter.isHeader(position) || mHTWrapperAdapter.isFooter(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
        if (mRecyclerView != null)
            mRecyclerView.setAdapter(mHTWrapperAdapter);
        setMode(mMode);
    }

    public void setRecyclerViewClipToPadding(boolean clipToPadding) {
        getRecyclerView().setClipToPadding(clipToPadding);
    }

    /**
     * Register a listener that will be notified whenever a child view is recycled.
     * <p/>
     * <p>This listener will be called when a LayoutManager or the RecyclerView decides
     * that a child view is no longer needed. If an application associates expensive
     * or heavyweight data with item views, this may be a good place to release
     * or free those resources.</p>
     *
     * @param listener Listener to register, or null to clear
     */

    public void setRecyclerListener(RecyclerView.RecyclerListener listener) {
        getRecyclerView().setRecyclerListener(listener);
    }

    /**
     * Remove all secondary listener that were notified of any changes in scroll state or position.
     */
    public void clearOnScrollListeners() {
        getRecyclerView().clearOnScrollListeners();
    }

    /**
     * Retrieve the {@link RecyclerView.ViewHolder} for the given child view.
     *
     * @param child Child of this RecyclerView to query for its ViewHolder
     * @return The child view's ViewHolder
     */
    public RecyclerView.ViewHolder getChildViewHolder(View child) {

        return getRecyclerView().getChildViewHolder(child);
    }

    /**
     * Add an {@link RecyclerView.OnItemTouchListener} to intercept touch events before they are
     * dispatched
     * to child views or this view's standard scrolling behavior.
     * <p/>
     * <p>Client code may use listeners to implement item manipulation behavior. Once a listener
     * returns true from
     * {@link RecyclerView.OnItemTouchListener onInterceptTouchEvent(RecyclerView, MotionEvent)}
     * its
     * {@link RecyclerView.OnItemTouchListener onTouchEvent(RecyclerView, MotionEvent)} method will
     * be called
     * for each incoming MotionEvent until the end of the gesture.</p>
     *
     * @param listener Listener to add
     * @see RecyclerView.SimpleOnItemTouchListener
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        getRecyclerView().addOnItemTouchListener(listener);
    }

    /**
     * Remove an {@link RecyclerView.OnItemTouchListener}. It will no longer be able to intercept
     * touch events.
     *
     * @param listener Listener to remove
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        getRecyclerView().removeOnItemTouchListener(listener);
    }

    public HTMode getMode() {
        return mMode;
    }

    public void setMode(HTMode mode) {
        mMode = mode;
    }

    /**
     * Add a listener that will be notified of any changes in scroll state or position.
     * <p/>
     * <p>Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call {@link #()}
     * to remove all attached listeners.</p>
     *
     * @param listener listener to set or null to clear
     */
    public void addOnScrollListener(HTSwipeRecyclerView.OnScrollListener listener) {
        if (mOnScrollListeners == null) mOnScrollListeners = new ArrayList<>();
        if (listener != null) {
            mOnScrollListeners.add(listener);
        }
    }

    /**
     * Remove a listener that was notified of any changes in scroll state or position.
     *
     * @param listener listener to set or null to clear
     */
    public void removeOnScrollListener(HTSwipeRecyclerView.OnScrollListener listener) {
        if (mOnScrollListeners != null && !mOnScrollListeners.isEmpty() && listener != null) {
            mOnScrollListeners.remove(listener);
        }
    }

    public void setOnLayoutSizeChangedListener(OnLayoutSizeChangedListener layoutSizeChangedListener) {
        this.mOnLayoutSizeChangedListener = layoutSizeChangedListener;
    }

    public enum RefreshStatus {
        IDLE, PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }


    public interface OnScrollListener {
        /**
         * Callback method to be invoked when RecyclerView's scroll state changes.
         *
         * @param recyclerView The RecyclerView whose scroll state has changed.
         * @param newState     The updated scroll state. One of SCROLL_STATE_IDLE,
         *                     SCROLL_STATE_DRAGGING or SCROLL_STATE_SETTLING.
         */
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        /**
         * Callback method to be invoked when the RecyclerView has been scrolled. This will be
         * called after the scroll has completed.
         * <p/>
         * This callback will also be called if visible item range changes after a layout
         * calculation. In that case, dx and dy will be 0.
         *
         * @param recyclerView The RecyclerView which scrolled.
         * @param dx           The amount of horizontal scroll.
         * @param dy           The amount of vertical scroll.
         */
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public interface OnLayoutSizeChangedListener {
        void onSizeChanged(int w, int h, int oldW, int oldH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (oldH == 0 || oldW == 0)
            return;//初次sizeChange 忽略
        if (mOnLayoutSizeChangedListener != null)
            mOnLayoutSizeChangedListener.onSizeChanged(w, h, oldW, oldH);
    }
}

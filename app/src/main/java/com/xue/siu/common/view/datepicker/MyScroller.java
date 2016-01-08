package com.xue.siu.common.view.datepicker;


import android.util.Log;


/**
 * Created by Administrator on 2015/6/15.
 */
class MyScroller extends android.widget.Scroller{
    private static final String TAG = "MyScroller";
    private ScrollerListener mScrollerListener;
    private android.os.Handler mHandler = new android.os.Handler();
    private Runnable mStartRunnable = new Runnable() {

        @Override
        public void run() {
            if (mScrollerListener != null)
                mScrollerListener.onScrollStart();
        }
    };

    private Runnable mEndRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScrollerListener != null)
                mScrollerListener.onScrollerEnd();
        }
    };

    MyScroller(android.content.Context context) {
        // super(context, new android.view.animation.LinearInterpolator());
        super(context, new android.view.animation.AccelerateDecelerateInterpolator());
    }

    void setHandler(android.os.Handler handler) {
        mHandler = handler;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mHandler.post(mStartRunnable);
        super.startScroll(startX, startY, dx, dy, duration);
        Log.i(TAG, "startScroll");
        mHandler.postDelayed(mEndRunnable, duration);
    }

    @Override
    public void abortAnimation() {
        Log.i(TAG, "abortAnimation");
        mHandler.removeCallbacks(mEndRunnable);
        super.abortAnimation();
    }

    void setOnScrollerListener(ScrollerListener scrollerListener) {
        mScrollerListener = scrollerListener;
    }



    interface ScrollerListener {
        public void onScrollerEnd();

        public void onScrollStart();
    }
}

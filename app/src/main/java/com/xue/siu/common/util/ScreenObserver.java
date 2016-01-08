package com.xue.siu.common.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.tencent.plus.DensityUtil;
import com.xue.siu.application.AppProfile;

// For more information, see https://code.google.com/p/android/issues/detail?id=5497
// To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

/**
 * Created by Administrator on 2015/7/22 0022.
 */
public class ScreenObserver {

    private OnScreenHeightChangedListener mScreenHeightListener;

    public static void assistActivity(Activity activity) {
        new ScreenObserver(activity);
    }

    public static void assistActivity(Activity activity, OnScreenHeightChangedListener listener) {
        new ScreenObserver(activity, listener);

    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int mStatusBarHeight;

    private ScreenObserver(Activity activity, OnScreenHeightChangedListener listener) {
        this(activity);
        mScreenHeightListener = listener;
    }

    private ScreenObserver(Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();

        mStatusBarHeight = ScreenUtil.getStatusBarHeight();

    }

    private void possiblyResizeChildOfContent() {
        boolean bigger;
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                bigger = false;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard - mStatusBarHeight;
                bigger = true;
            }
            mChildOfContent.requestLayout();
            if (mScreenHeightListener != null)
                mScreenHeightListener.onSizedChanged(bigger,usableHeightNow,usableHeightPrevious);
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);

        return (r.bottom - r.top);
    }

    public interface OnScreenHeightChangedListener {
        void onSizedChanged(boolean bigger,int newHeight,int oldHeight);
    }
}


package com.xue.siu.common.util;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.siu.R;
import com.xue.siu.application.AppProfile;


/**
 * Created by zyl06 on 9/18/15.
 */
public class ToastUtil {
    private static Toast toast = Toast.makeText(AppProfile.getContext(), "", Toast.LENGTH_LONG);
    private static View toastView;
    private static TextView toastTextView;
    private static boolean isShowing = false;

    private static int shortDuration = 1000;
    private static int longDuration = 2000;

    static {
        initToastView();
    }

    // 显示短提示
    public static void makeShortToast(String content) {
        initToastView();
        toastTextView.setText(content);
        handleHide(shortDuration);
    }

    // 显示短提示
    public static void makeShortToast(int stringId) {
        initToastView();
        toastTextView.setText(stringId);
        handleHide(shortDuration);
    }

    // 显示短提示
    public static void makeShortToast(int stringId, Object... objects) {
        String content = ResourcesUtil.stringFormat(stringId, objects);
        makeShortToast(content);
    }

    // 显示长提示
    public static void makeLongToast(String content) {
        initToastView();
        toastTextView.setText(content);
        handleHide(longDuration);
    }

    // 显示长提示
    public static void makeLongToast(int stringId) {
        initToastView();
        toastTextView.setText(stringId);
        toast.show();
        handleHide(longDuration);
    }

    // 显示长提示
    public static void makeLongToast(int stringId, Object... objects) {
        String content = ResourcesUtil.stringFormat(stringId, objects);
        makeLongToast(content);
    }

    private static void initToastView() {
        if (toastView == null) {
            toastView = View.inflate(AppProfile.getContext(), R.layout.view_toast, null);
            toastTextView = (TextView) toastView.findViewById(R.id.toast_text);
            toast.setView(toastView);
        }
    }

    private static void handleHide(final long delay) {
        //直接调用会导致显示不了
        if (isShowing) {
            toast.cancel();
        }
        //为了让上一个先消失，不然的话，可能下一个show不出来
        HandleUtil.doDelay(new Runnable() {
            @Override
            public void run() {
                isShowing = true;
                toast.show();
                HandleUtil.doDelay(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                        isShowing = false;
                    }
                }, delay);
            }
        }, 300);
    }
}

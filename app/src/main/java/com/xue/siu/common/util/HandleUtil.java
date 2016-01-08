package com.xue.siu.common.util;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

/**
 * Created by zyl06 on 10/8/15.
 */
public class HandleUtil {
    public static void doIdleHandler(MessageQueue.IdleHandler idleHandler) {
        Looper.getMainLooper().myQueue().addIdleHandler(idleHandler);
    }

    public static void doDelay(Runnable runable, long delayMillis) {
        Handler handler = new Handler();
        handler.postDelayed(runable, delayMillis);
    }

    public static void doOnMainThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}

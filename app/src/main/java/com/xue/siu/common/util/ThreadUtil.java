package com.xue.siu.common.util;

/**
 * Created by XUE on 2016/1/19.
 */
public class ThreadUtil {
    public static void runAsync(Runnable runnable, String threadName) {
        Thread thread = new Thread(runnable, threadName);
        thread.start();
    }
}

package com.xue.siu.common.util;


import android.util.Log;


/**
 * Created by zyl06 on 9/18/15.
 */
public class LogUtil {
    private static final String sTag = "SIU";

    public static void yxLogW(String msg) {
        Log.w(sTag, msg);
    }

    public static void yxLogE(String msg) {
        Log.e(sTag, "error running: " + msg);
    }

    public static void yxLogPrintStackTrace(StackTraceElement[] ste) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement s : ste) {
            sb.append('\n');
            sb.append(s.toString());
        }
        yxLogE(sb.toString());
    }

    public static void yxLogD(String msg) {
        Log.d(sTag, msg);
    }



    public static void i(String Tag, String msg) {
        Log.i(Tag, msg);
    }

    public static void e(String Tag, String msg) {
        Log.e(Tag, msg);
    }

    public static void d(String Tag, String msg) {
        Log.d(Tag, msg);
    }


}

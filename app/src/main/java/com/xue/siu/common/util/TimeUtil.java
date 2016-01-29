package com.xue.siu.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XUE on 2016/1/29.
 */
public class TimeUtil {
    public static final long SEC = 1000;
    public static final long MINUTE = SEC * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = HOUR * 24;
    public static final long YEAR = DAY * 365;
    public static final String TIME_FORMAT_YY_MM_DD = "yy年MM月dd日";
    public static final String TIME_FORMAT_LAST_DAY = "昨天hh:mm";
    public static final String TIME_FORMAT_HH_MM = "HH:mm";
    public static final String TIME_FORMAT_MM_DD = "MM月dd日";

    public static String convertLongToString(long millsec) {
        long current = System.currentTimeMillis();
        Date date = new Date(millsec);
        SimpleDateFormat format;
        if (current - millsec >= 2 * DAY && current - millsec < YEAR) {
            format = new SimpleDateFormat(TIME_FORMAT_MM_DD);
        } else if (current - millsec >= YEAR) {
            format = new SimpleDateFormat(TIME_FORMAT_YY_MM_DD);
        } else if (current - millsec < 2 * DAY && current - millsec >= DAY) {
            format = new SimpleDateFormat(TIME_FORMAT_LAST_DAY);
        } else {
            format = new SimpleDateFormat(TIME_FORMAT_HH_MM);
        }
        return format.format(date);
    }
}

package com.xue.siu.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static final String TIME_FORMAT_SLASH = "yyyy/MM/dd";
    public static final String[] DAY_OF_WEEK = {"周日","周一", "周二", "周三", "周四", "周五", "周六"};

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

    /**
     * 获取今天是星期几
     *
     * @return
     */
    public static String getDayOfWeek() {
        return getDayOfWeek(new Date());
    }

    /**
     * 获取某个日期是星期几
     *
     * @param date
     * @return
     */
    public static String getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK)-1];
    }

    public static String getTodayDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getCurrentTime() {
        return convertLongToTime(System.currentTimeMillis());
    }

    public static String getAfterTenMinutes() {
        long mill = System.currentTimeMillis();
        mill += 10 * MINUTE;
        return convertLongToTime(mill);
    }

    public static String convertLongToTime(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_HH_MM);
        return sdf.format(date);
    }
}

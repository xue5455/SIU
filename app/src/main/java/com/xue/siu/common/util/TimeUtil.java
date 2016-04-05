package com.xue.siu.common.util;

import android.support.annotation.StringRes;

import com.xue.siu.R;
import com.xue.siu.common.util.string.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static final String TIME_FORMAT_HM = "%1$s:%2$s";
    public static final String[] DAY_OF_WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

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
        return DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getTodayDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayOfWeek = getDayOfWeek(date);
        return ResourcesUtil.stringFormat(R.string.nca_date_format, year, month + 1, day, dayOfWeek);
    }

    public static String getScheduleDate(long mill) {
        LogUtil.i("xxj", "getScheduleDate mill = " + mill);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(mill);
        return ResourcesUtil.stringFormat(R.string.nca_date_format, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK)-1]);

    }

    public static String convertSecondsToTime(int seconds) {
        int hour = seconds / 3600;
        String h = String.valueOf(hour);
        if (hour < 10) {
            h = '0' + h;
        }
        seconds = seconds - hour * 3600;
        int minute = seconds / 60;
        String m = String.valueOf(minute);
        if (minute < 10) {
            m = '0' + m;
        }
        return StringUtil.format(TIME_FORMAT_HM, h, m);
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

    public static String getDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}

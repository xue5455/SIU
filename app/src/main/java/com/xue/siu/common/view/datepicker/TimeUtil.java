package com.xue.siu.common.view.datepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class TimeUtil {

    /**
     * 计算某个月有几天
     *
     * @param y
     * @param m
     * @return
     */
    static int getDaysOfMonth(int y, int m) {

        if (m == 1 || m == 3 || m == 5 || m == 7 | m == 8 || m == 10 || m == 12) {
            return 31;
        } else if (m == 2) {
            if (y % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return 30;
        }
    }

    static int getMonth() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String month = sdf.format(dt).split("-")[1];
        return Integer.parseInt(month);
    }

    static int getDay() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String day = sdf.format(dt).split("-")[2];
        return Integer.parseInt(day);
    }

    static int getYear() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String year = sdf.format(dt).split("-")[0];
        return Integer.parseInt(year);
    }

    static int getHour() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String hour = sdf.format(dt).split(" ")[1].split(":")[0];
        return Integer.parseInt(hour);
    }

    static int getMinute() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mi = sdf.format(dt).split(" ")[1].split(":")[1];
        return Integer.parseInt(mi);
    }

    /**
     * 周日为1,周六为7
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    static int getWeek(int year, int month, int day) {
        Calendar now = Calendar.getInstance();
        now.set(year, month - 1, day);
        int week = now.get(Calendar.DAY_OF_WEEK);
        return week;
    }

    /**
     * 获取某月的第一天为星期几
     *
     * @param year
     * @param month
     * @return
     */
    static int getWeek(int year, int month) {
        return getWeek(year, month, 1);
    }

    static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    static String getHourAndMinAndSec(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time));
    }

}

package com.xue.siu.module.calendar.model;

/**
 * Created by XUE on 2016/3/27.
 */
public class ScheduleVO {
    private String title;
    /* 微秒数 */
    private long date;
    /* 秒数 */
    private int startTime;
    private int endTime;

    public ScheduleVO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}

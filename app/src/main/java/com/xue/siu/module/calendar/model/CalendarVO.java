package com.xue.siu.module.calendar.model;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.xue.siu.avim.LeanConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/3/25.
 */
public class CalendarVO {
    private AVObject avObject;
    private String title;
    private String location;
    private long date;
    private int startTime;
    private int endTime;
    private AVUser owner;
    private List<AVUser> attender = new ArrayList<>();

    public CalendarVO() {
        avObject = new AVObject(LeanConstants.CALENDAR_TABLE_NAME);
    }

    public CalendarVO(AVObject avObject) {
        this.avObject = avObject;
        this.title = (String) avObject.get(LeanConstants.CALENDAR_FIELD_TITLE);
        this.location = (String) avObject.get(LeanConstants.CALENDAR_FIELD_LOCATION);
        this.date = (long) avObject.get(LeanConstants.CALENDAR_FIELD_DATE);
        this.startTime = (int) avObject.get(LeanConstants.CALENDAR_FIELD_START_TIME);
        this.endTime = (int) avObject.get(LeanConstants.CALENDAR_FIELD_END_TIME);
        this.owner = (AVUser) avObject.get(LeanConstants.CALENDAR_FIELD_OWNER);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        avObject.put(LeanConstants.CALENDAR_FIELD_TITLE, title);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        avObject.put(LeanConstants.CALENDAR_FIELD_LOCATION, location);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        avObject.put(LeanConstants.CALENDAR_FIELD_DATE, date);
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
        avObject.put(LeanConstants.CALENDAR_FIELD_START_TIME, startTime);
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        avObject.put(LeanConstants.CALENDAR_FIELD_END_TIME, endTime);
    }

    public AVObject getAvObject() {
        return this.avObject;
    }

    public AVUser getOwner() {
        return owner;
    }

    public void setOwner(AVUser owner) {
        this.owner = owner;
        avObject.put(LeanConstants.CALENDAR_FIELD_OWNER, owner);
    }

    public void addAttender(AVUser user) {
        attender.add(user);
        avObject.put(LeanConstants.CALENDAR_FIELD_ATTENDER, attender);
    }


}

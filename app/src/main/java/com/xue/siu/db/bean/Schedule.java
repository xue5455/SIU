package com.xue.siu.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XUE on 2016/3/27.
 */
@DatabaseTable(tableName = "tb_schedule")
public class Schedule {
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "sdate";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_ATTENDER = "attender";
    public static final String COLUMN_OWNER = "owner";
    public static final String COLUMN_LOCATION = "location";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;
    @DatabaseField(columnName = COLUMN_START)
    private int start;
    @DatabaseField(columnName = COLUMN_END)
    private int end;
    @DatabaseField(columnName = COLUMN_ATTENDER)
    private String attender;
    @DatabaseField(columnName = COLUMN_DATE)
    private long date;

    @DatabaseField(columnName = COLUMN_OWNER)
    private String owner;/* 创建人id */
    @DatabaseField(columnName = COLUMN_LOCATION)
    private String location;

    public Schedule() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getAttender() {
        return attender;
    }

    public void setAttender(String attender) {
        this.attender = attender;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

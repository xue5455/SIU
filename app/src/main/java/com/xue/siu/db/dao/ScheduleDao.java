package com.xue.siu.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.db.OrmDBHelper;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.db.bean.Schedule;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/3/27.
 */
public class ScheduleDao {
    private final String TAG = "schedule_db";
    private Context mContext;
    private Dao<Schedule, Integer> mMsgDaoOpe;
    private OrmDBHelper mHelper;

    public ScheduleDao(Context context) {
        this.mContext = context;
        try {
            mHelper = OrmDBHelper.getHelper(context);
            mMsgDaoOpe = mHelper.getDao(Schedule.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void add(Schedule schedule) {
        try {
            mMsgDaoOpe.create(schedule);
            LogUtil.i(TAG, "插入一条新日程 date is " + schedule.getDate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public List<Schedule> query(long date) {
        LogUtil.i("xxj", "query date is " + date);
        List<Schedule> msgList = new ArrayList<>();
        QueryBuilder<Schedule, Integer> queryBuilder = mMsgDaoOpe.queryBuilder();
        queryBuilder.orderBy(Schedule.COLUMN_START,true);
        Where<Schedule, Integer> where = queryBuilder.where();
        try {
            where.eq(Schedule.COLUMN_DATE, date);
            msgList = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msgList;
    }

    /**
     * Delete Schedule
     *
     * @param schedule
     */
    public void delete(Schedule schedule) {
        try {
            mMsgDaoOpe.delete(schedule);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void clear(long date) {
        DeleteBuilder<Schedule, Integer> deleteBuilder = mMsgDaoOpe.deleteBuilder();
        Where<Schedule, Integer> where = deleteBuilder.where();
        try {
            where.eq(Schedule.COLUMN_DATE, date);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

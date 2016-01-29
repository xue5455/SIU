package com.xue.siu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.db.bean.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XUE on 2015/12/10.
 */
public class OrmDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = OrmDBHelper.class.getSimpleName();
    private static final String TABLE_NAME = "OhShock.db";
    private Map<String, Dao> mDaoMap = new HashMap<String, Dao>();
    /* 单例 */
    private static OrmDBHelper mInstance;

    public OrmDBHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    public OrmDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, SIUMessage.class);
            TableUtils.createTable(connectionSource,User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, SIUMessage.class, true);
            TableUtils.dropTable(connectionSource, User.class,true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized OrmDBHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (mInstance == null) {
            synchronized (OrmDBHelper.class) {
                if (mInstance == null)
                    mInstance = new OrmDBHelper(context);
            }
        }

        return mInstance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (mDaoMap.containsKey(className)) {
            dao = mDaoMap.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            mDaoMap.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : mDaoMap.keySet()) {
            Dao dao = mDaoMap.get(key);
            dao = null;
        }
    }
}

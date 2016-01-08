package com.xue.siu.db;

import android.content.SharedPreferences;

/**
 * Created by zyl06 on 6/16/15.
 */
public class MySharePreferenceHelper {
    public static final String COPYRIGHTED = "Copyrighted";

    private String userId;

    public MySharePreferenceHelper(String uid) {
        userId = uid;
    }

    public boolean getCopyrighted() {
        return SharePreferenceHelper.getBoolean(userId, COPYRIGHTED, false);
    }

    public void setCopyrighted(boolean copyrighted) {
        SharePreferenceHelper.putBoolean(userId, COPYRIGHTED, copyrighted);
    }

    public void putString(String key, String value) {
        SharePreferenceHelper.putString(userId, key, value);
    }

    public String getString(String key) {
        return SharePreferenceHelper.getString(userId, key, null);
    }

    public void putLong(String key, long value) {
        SharePreferenceHelper.putLong(userId, key, value);
    }

    public long getLong(String key) {
        return SharePreferenceHelper.getLong(userId, key, Long.MIN_VALUE);
    }

    public void putInt(String key, int value) {
        SharePreferenceHelper.putInt(userId, key, value);
    }

    public int getInt(String key) {
        return SharePreferenceHelper.getInt(userId, key, Integer.MIN_VALUE);
    }

    public void putBoolean(String key, boolean value) {
        SharePreferenceHelper.putBoolean(userId, key, value);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return SharePreferenceHelper.getBoolean(userId, key, defValue);
    }

    public void putFloat(String key, float value) {
        SharePreferenceHelper.putFloat(userId, key, value);
    }

    public float getFloat(String key, float defValue) {
        return SharePreferenceHelper.getFloat(userId, key, defValue);
    }

    public SharedPreferences getSharePreference() {
        return SharePreferenceHelper.getSharePreference(userId);
    }


//    public  void putIsLatestPostCreateTime(String currentCircleId, Long time) {
//        SharePreferenceHelper.putGlobalLong(currentCircleId, time);
//    }
//
//    public  long getIsLatestPostCreateTime(String currentCircleId) {
//        return SharePreferenceHelper.getGlobalLong(currentCircleId, Long.MAX_VALUE);
//    }

    public long getLatestPostUpdateTime(String userId,String currentCircleId) {
        return SharePreferenceHelper.getLong(userId,currentCircleId, Long.MIN_VALUE);
    }

    public void setLatestPostUpdateTime(String userId,String currentCircleId,long time) {
        SharePreferenceHelper.putLong(userId, currentCircleId, time);
    }
}

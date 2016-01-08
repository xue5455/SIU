package com.xue.siu.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.xue.siu.application.AppProfile;
import com.xue.siu.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyl06 on 6/15/15.
 */
public class SharePreferenceHelper {
    private static final String APP_NAME = "YanXuan";
    private static Map<String, SharedPreferences> sAccount2SP = new HashMap<>();
    //表示是否需要缓存sp引用
    private static boolean sNeedCache = true;

    public static SharedPreferences getSharePreference(String accountName) {
        String spName = getSharePreferenceName(accountName);
        return getSharePreferencesWithSPName(spName, true);
    }

    public static SharedPreferences getGlobalSharePreference() {
        return getSharePreferencesWithSPName(APP_NAME, true);
    }

    public static SharedPreferences getGlobalSharePreference(Context context) {
        return getSharePreferencesWithSPName(context, APP_NAME, true);
    }

    public static void putString(String accountName, String key, String value) {
        String spName = getSharePreferenceName(accountName);
        putStringWithSPName(spName, key, value);
    }

    public static void putString(Context context, String accountName, String key, String value) {
        String spName = getSharePreferenceName(accountName);
        putStringWithSPName(context, spName, key, value);
    }

    public static String getString(String accountName, String key, String defValue) {
        String spName = getSharePreferenceName(accountName);
        return getStringWithSPName(spName, key, defValue);
    }

    public static String getString(Context context, String accountName, String key, String defValue) {
        String spName = getSharePreferenceName(accountName);
        return getStringWithSPName(context, spName, key, defValue);
    }

    public static void putLong(String accountName, String key, Long value) {
        String spName = getSharePreferenceName(accountName);
        putLongWithSPName(spName, key, value);
    }

    public static long getLong(String accountName, String key, long defValue) {
        String spName = getSharePreferenceName(accountName);
        return getLongWithSPName(spName, key, defValue);
    }

    public static void putInt(String accountName, String key, int value) {
        String spName = getSharePreferenceName(accountName);
        putIntWithSPName(spName, key, value);
    }

    public static int getInt(String accountName, String key, int defValue) {
        String spName = getSharePreferenceName(accountName);
        return getIntWithSPName(spName, key, defValue);
    }

    public static void putBoolean(String accountName, String key, boolean value) {
        String spName = getSharePreferenceName(accountName);
        putBooleanWithSPName(spName, key, value);
    }

    public static boolean getBoolean(String accountName, String key, boolean defValue) {
        String spName = getSharePreferenceName(accountName);
        return getBooleanWithSPName(spName, key, defValue);
    }

    public static void putFloat(String accountName, String key, float value) {
        String spName = getSharePreferenceName(accountName);
        putFloatWithSPName(spName, key, value);
    }

    public static float getFloat(String accountName, String key, float defValue) {
        String spName = getSharePreferenceName(accountName);
        return getFloatWithSPName(spName, key, defValue);
    }

    public static void remove(String accountName, String key) {
        String spName = getSharePreferenceName(accountName);
        removeWithSPName(spName, key);
    }

    public static String getGlobalString(String key, String defValue) {
        return getStringWithSPName(APP_NAME, key, defValue);
    }

    public static String getGlobalString(Context context, String key, String defValue) {
        return getStringWithSPName(context, APP_NAME, key, defValue);
    }


    public static void putGlobalString(String key, String value) {
        putStringWithSPName(APP_NAME, key, value);
    }

    public static long getGlobalLong(String key, long defValue) {
        return getLongWithSPName(APP_NAME, key, defValue);
    }

    public static void putGlobalLong(String key, long value) {
        putLongWithSPName(APP_NAME, key, value);
    }

    public static int getGlobalInt(String key, int defValue) {
        return getIntWithSPName(APP_NAME, key, defValue);
    }

    public static void putGlobalInt(String key, int value) {
        putIntWithSPName(APP_NAME, key, value);
    }

    public static float getGlobalFloat(String key, float defValue) {
        return getFloatWithSPName(APP_NAME, key, defValue);
    }

    public static void putGlobalFloat(String key, float value) {
        putFloatWithSPName(APP_NAME, key, value);
    }

    public static boolean getGlobalBoolean(String key, boolean defValue) {
        return getBooleanWithSPName(APP_NAME, key, defValue);
    }

    public static void putGlobalBoolean(String key, boolean value) {
        putBooleanWithSPName(APP_NAME, key, value);
    }

    public static void removeGlobal(String key) {
        removeWithSPName(APP_NAME, key);
    }

    private static void removeWithSPName(String spName, String key) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    private static boolean getBooleanWithSPName(String spName, String key, boolean defValue) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        return sp != null ? sp.getBoolean(key, defValue) : defValue;
    }

    private static void putBooleanWithSPName(String spName, String key, boolean value) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    private static float getFloatWithSPName(String spName, String key, float defValue) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        return sp != null ? sp.getFloat(key, defValue) : defValue;
    }

    private static void putFloatWithSPName(String spName, String key, float value) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    private static int getIntWithSPName(String spName, String key, int defValue) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        return sp != null ? sp.getInt(key, defValue) : defValue;
    }

    private static void putIntWithSPName(String spName, String key, int value) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    private static long getLongWithSPName(String spName, String key, long defValue) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        return sp != null ? sp.getLong(key, defValue) : defValue;
    }

    private static void putLongWithSPName(String spName, String key, long value) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    private static String getStringWithSPName(String spName, String key, String defValue) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        return (sp != null) ? sp.getString(key, defValue) : defValue;
    }

    private static String getStringWithSPName(Context context, String spName, String key, String defValue) {
        SharedPreferences sp = getSharePreferencesWithSPName(context, spName, true);
        return (sp != null) ? sp.getString(key, defValue) : defValue;
    }


    private static void putStringWithSPName(String spName, String key, String value) {
        SharedPreferences sp = getSharePreferencesWithSPName(spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    private static void putStringWithSPName(Context context, String spName, String key, String value) {
        SharedPreferences sp = getSharePreferencesWithSPName(context, spName, true);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }


    private static String getSharePreferenceName(String accountName) {
        if (TextUtils.isEmpty(accountName)) {
            LogUtil.yxLogE("SharePreferenceHelper getSharePreferenceName: accountName == null");
            return null;
        }
        return accountName + APP_NAME;
    }

    private static SharedPreferences getSharePreferencesWithSPName(String spName, boolean createNewIfNotExist) {
        return getSharePreferencesWithSPName(AppProfile.getContext(), spName, createNewIfNotExist);
    }

    private static SharedPreferences getSharePreferencesWithSPName(Context context, String spName, boolean createNewIfNotExist) {
        if (TextUtils.isEmpty(spName)) {
            LogUtil.yxLogE("SharePreferenceHelper getOrCreateSharePreferences: spName == null");
            return null;
        }
        SharedPreferences sp = null;
        if (sNeedCache) {
            sp = sAccount2SP.get(spName);
        }
        if (sp == null && createNewIfNotExist) {
            sp = context.getSharedPreferences(spName, Context.MODE_MULTI_PROCESS);
            sAccount2SP.put(spName, sp);
        }
        return sp;
    }

    public static void setNeedCache(boolean needCache) {
        sNeedCache = needCache;
    }
}

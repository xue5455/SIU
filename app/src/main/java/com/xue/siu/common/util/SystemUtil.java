package com.xue.siu.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;


import com.xue.siu.application.AppProfile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by zyl06 on 9/18/15.
 */
public class SystemUtil {
    /**
     * 获取android id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        if (isEmulator(context)) {
            return null;
        }
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 是否是模拟器
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        String imei = getIMEI(context);
        if ("000000000000000".equals(imei)) {
            return true;
        }
        return (Build.MODEL.equalsIgnoreCase("sdk")) || (Build.MODEL.equalsIgnoreCase("google_sdk")) || Build.BRAND.equalsIgnoreCase("generic");
    }

    /**
     * 当前是否横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 基于Android3.0的平板
     *
     * @param context
     * @return
     */
    public static boolean isHoneycombTablet(Context context) {
        return isHoneycomb() && isTablet(context);
    }

//    /**
//     * 系统为Android5.0
//     *
//     * @return
//     */
//    public static boolean isLollipop() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
//    }

    /**
     * 系统为Android3.0
     *
     * @return
     */
    public static boolean isHoneycomb() {
        // Can use static final constants like HONEYCOMB, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * 系统为Android2.1.x
     *
     * @return
     */
    public static boolean isEclair_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1;
    }

    /**
     * 判断是否为平板设备
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        //暂时屏蔽平板
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
//    		return (context.getResources().getConfiguration().screenLayout
//                    & Configuration.SCREENLAYOUT_SIZE_MASK)
//                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        return false;
    }

    /**
     * 获取设备ID.
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String id = getIMEI(context);
        if (TextUtils.isEmpty(id)) {
            id = getAndroidId(context);
        }
        return id;
    }

    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            return imei;
        } catch (Exception ioe) {
        }
        return null;
    }

    /**
     * 获取设备名称.
     *
     * @return
     */
    public static String getBuildModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备SDK版本号.
     *
     * @return
     */
    public static int getBuildVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备系统版本号.
     *
     * @return
     */
    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 判断SD卡是否插入 即是否有SD卡
     */
    public static boolean isSDCardMounted() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState());
    }

    /**
     * 是否：已经挂载,但只拥有可读权限
     */
    public static boolean isSDCardMountedReadOnly() {
        return android.os.Environment.MEDIA_MOUNTED_READ_ONLY.equals(android.os.Environment
                .getExternalStorageState());
    }

    /**
     * 获取android当前可用内存大小
     */
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取屏幕的亮度
     */
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver,
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 获取手机屏幕的宽和高
     *
     * @param c
     * @return map("w", width) map("h",height);
     */
    public static HashMap<String, Integer> getWidth_Height(Context c) {
        DisplayMetrics metrics = c.getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        HashMap<String, Integer> m = new HashMap<String, Integer>();
        m.put("w", width);
        m.put("h", height);
        return m;
    }

    /**
     * 获取平板在横屏时webview的宽度
     *
     * @param c
     * @return
     */
    public static int getTabletWebViewWidth(Context c) {
        // 0.82f根据当前webview的padding计算得来
        return (int) ((float) SystemUtil.getScreenWidth(c) * 0.82f / c.getResources().getDisplayMetrics().density);
    }

    /**
     * 获取手机屏幕的宽和高size wxh
     *
     * @param c
     * @return width X height
     */
    public static String getWidthXHeight(Context c) {
        Map<String, Integer> m = getWidth_Height(c);
        String size = m.get("w") + "x" + m.get("h");
        return size;
    }

    /**
     * 获取手机分辨率宽度大小
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取手机分辨率长度大小
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取手机屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 4.0+获取虚拟导航高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Rect rect = new Rect();
        ((FragmentActivity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 获取屏幕内容视图的高度（屏幕高度 - 系统状态栏高度 - 虚拟导航条高度）
     *
     * @return
     */
    public static int getScreenContentHeight(Context context) {
        int screenHeight = getScreenHeight(context);
        int statusBarHeight = getStatusBarHeight(context);
        int naviHeight = getNavigationBarHeight(context);
        return screenHeight - statusBarHeight - naviHeight;
    }

    /**
     * 获取应用窗口高度
     *
     * @param context
     * @return
     */
    public static int getAppWindowHeight(Context context) {
        Rect rect = new Rect();
        ((FragmentActivity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }

    /**
     * 获得设备html宽度
     *
     * @param context
     * @return
     */
    public static int getDeviceHtmlWidth(Context context) {

        if (isHoneycombTablet(context) && isLandscape(context)) {
            return getTabletWebViewWidth(context);
        }

        int width = (int) (getScreenWidth(context) / context.getResources().getDisplayMetrics().density);

        return width;
    }

    /**
     * 得到dimen定义的大小
     *
     * @param context
     * @param dimenId
     * @return
     */
    public static int getDimension(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }

    private static String NEWLY_INSTALLED_KEY = "newly_installed_key";


    /**
     * 判断应用是否安装
     *
     * @param context
     * @param appName
     * @return
     */
    public static boolean isAppInstalled(Context context, String appName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(appName, 0);
            if (null != packageInfo) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private static String VERSTION_FIRST_START_APP_KEY = "first_start_%s_app_key";
    private static String VERSTION_PREF = "VERSTION_PREF";

    public static String FIRST_INIT_DATA_KEY = "first_init_data_key";


    /**
     * 返回应用版本号
     *
     * @return
     */
    public static int getAppVersionCode() {
        Context context = AppProfile.getContext();
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 返回应用版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 返回应用版本号
     *
     * @return
     */
    public static String getAppVersion() {
        Context context = AppProfile.getContext();
        return getAppVersion(context, context.getPackageName());
    }

    /**
     * 返回应用版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        return getAppVersion(context, context.getPackageName());
    }

    /**
     * 根据packageName包名的应用获取应用版本名称,如未安装返回null
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppVersion(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 根据packageName包名的应用获取应用版本名称,如未安装返回null
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return 0;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 根据packageName包名的应用获取应用信息,如未安装返回null
     *
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getAppInfo(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 判断打开新闻，按返回键是否要回到列表页面
     * 1、网易新闻内部点击链接：不回到列表页
     * 2、外部浏览器点击链接：不回到列表
     */
    public static boolean shouldStartMain(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasksInfoList = am.getRunningTasks(1);

            if (tasksInfoList == null || tasksInfoList.size() == 0) {
                return false;
            }

            String appName = context.getPackageName();
            ActivityManager.RunningTaskInfo taskInfo = tasksInfoList.get(0);
            if (taskInfo.numActivities == 1 || !appName.equals(taskInfo.baseActivity.getPackageName())) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断是不是合法时间
     *
     * @return
     */
    public static boolean isValidTime(String startTime, String endTime) {
        return isValidTime("yyyy-MM-dd HH:mm:ss", "Asia/Shanghai", startTime, endTime);
    }

    /**
     * 判断是不是合法时间
     *
     * @return
     */
    public static boolean isValidTime(String format, String timeZone, String startTime, String endTime) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            TimeZone timeZoneshanghai = TimeZone.getTimeZone(timeZone);
            df.setTimeZone(timeZoneshanghai);

            Date startDate = df.parse(startTime);
            Date endDate = df.parse(endTime);
            long start = startDate.getTime();
            long end = endDate.getTime();

            long now = getCurrentTimeMillis();
            if (now > start && now < end) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getNetworkTime() {
        try {
            URL url = new URL("http://www.baidu.com");//new URL("http://www.bjtime.cn"); //取得资源对象
            URLConnection uc = url.openConnection(); //生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            return ld;
        } catch (MalformedURLException e) {
            Log.e("time", "getBeijingTime error:" + e.toString());
        } catch (IOException e) {
            Log.e("time", "getBeijingTime error" + e.toString());
        }

        return 0;
    }

    /* package */ static long sTimeBias = 0;

    public static final long getCurrentTimeMillis() {
        return System.currentTimeMillis() + sTimeBias;
    }

    public static final void updateSystemBias() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                long bgTime = SystemUtil.getNetworkTime();
                if (bgTime != 0) {
                    long currentSystemTime = System.currentTimeMillis();
                    sTimeBias = bgTime - currentSystemTime;
                }
                return null;
            }
        };

        task.execute();
    }


//    public static String getChannel() {
//        return getMetaDataValue("CHANNEL");
//    }

    public static String getMetaDataValue(String key) {
        String value = "";
        Context context = AppProfile.getContext();
        try {
            value = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Check if network is opened.
     *
     * @return true network is open
     */
    public static boolean isNetworkOpened() {
        try {
            ConnectivityManager cm = (ConnectivityManager) AppProfile.getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isAvailable() && info.isConnected()) {
                LogUtil.yxLogD("network is available.");
                return true;
            }
            LogUtil.yxLogD("network is unavailable.");
            return false;
        } catch (Throwable th) {
            return false;
        }
    }
}

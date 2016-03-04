package com.netease.hearttouch.htimagepicker.util.image;

import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * Created by zyl06 on 3/1/16.
 */
public class BitmapCache implements IBitmapCache {
    private static final String TAG = "HT_BitmapCache";
    private static BitmapMemoryCache memoryCache;
    private static BitmapDiskCache diskCache;

    private static BitmapCache sDefault = null;
    public static synchronized BitmapCache getDefault() {
        if (sDefault == null || sDefault.diskCache == null || sDefault.memoryCache == null) {
            synchronized (BitmapCache.class) {
                if (sDefault == null || sDefault.diskCache == null || sDefault.memoryCache == null) {
                    sDefault = new BitmapCache();
                }
            }
        }
        return sDefault;
    }

    private BitmapCache() {
        if (memoryCache == null) {
            synchronized (BitmapCache.class) {
                if (memoryCache == null) {
                    memoryCache = new BitmapMemoryCache();
                }
            }
        }
        if (diskCache == null) {
            synchronized (BitmapCache.class) {
                if (diskCache == null) {
                    diskCache = new BitmapDiskCache("image_cache");
                }
            }
        }
    }

    @Override
    public Bitmap get(String key) {
        Bitmap bm = memoryCache.get(key);
        if (bm == null) {
            bm = diskCache.get(key);
            if (bm != null) {
                memoryCache.put(key, bm);
            }
        }
        return bm;
    }

    public Bitmap getFromMemory(String key) {
        return memoryCache.get(key);
    }

    @Override
    public Bitmap put(String key, Bitmap value) {
        if (!TextUtils.isEmpty(key) && value != null) {
            Bitmap bm = memoryCache.put(key, value);
            diskCache.put(key, value); // 返回为null
            return bm;
        }
        return null;
    }

    @Override
    public void close() {
        memoryCache.close();
        diskCache.close();
        memoryCache = null;
        diskCache = null;
    }

    @Override
    public void clear() {
        memoryCache.clear();
        diskCache.clear();
    }

    public void clear(boolean clearMemory, boolean clearDisk) {
        if (clearMemory) memoryCache.clear();
        if (clearDisk) diskCache.clear();
    }
}

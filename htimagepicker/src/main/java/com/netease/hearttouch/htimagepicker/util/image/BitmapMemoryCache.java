package com.netease.hearttouch.htimagepicker.util.image;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.Map;

/**
 * Created by zyl06 on 2/29/16.
 */
public class BitmapMemoryCache extends LruCache<String, Bitmap> implements IBitmapCache {
    private static final String TAG = "HTImagePicker_BitmapCache";
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory(); //分配的可用内存
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;//使用的缓存数量

    public BitmapMemoryCache() {
        //this((int) Runtime.getRuntime().freeMemory() / 2);
        this(MAX_MEMORY_CACHE_SIZE);
    }

    public BitmapMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    protected void entryRemoved(boolean evicted, String key,
                                Bitmap oldValue, Bitmap newValue) {
        // 不要在这里强制回收oldValue，因为从LruCache清掉的对象可能在屏幕上显示着，
        // 这样就会出现空白现象
        super.entryRemoved(evicted, key, oldValue, newValue);
    }

    @Override
    public void clear() {
        for (Map.Entry<String, Bitmap> entry : snapshot().entrySet()) {
            Bitmap bm = entry.getValue();
            if (!bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}

package com.xue.siu.common.view.asynclist;

import android.view.View;

import java.util.HashMap;

/**
 * Created by XUE on 2016/3/1.
 */
public class LayoutCacheManager {
    private static LayoutCacheManager mInstance;
    private HashMap<Object, View> mViewCache;

    private LayoutCacheManager() {
        mViewCache = new HashMap<>();
    }

    public static LayoutCacheManager getInstance() {
        if (mInstance == null) {
            synchronized (LayoutCacheManager.class) {
                if (mInstance == null)
                    mInstance = new LayoutCacheManager();
            }
        }
        return mInstance;
    }

    public void put(Object key, View value) {
        mViewCache.put(key, value);
    }

    public boolean contains(Object key) {
        return mViewCache.containsKey(key);
    }

    public void remove(Object key) {
        mViewCache.remove(key);
    }

    public View get(Object key){
        return mViewCache.get(key);
    }

    public void clear(){
        mViewCache.clear();
    }
}

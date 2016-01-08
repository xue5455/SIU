package com.xue.siu.application;

import android.content.Context;


/**
 * Created by zyl06 on 9/18/15.
 */
public class AppProfile {
    /* package */
    static Context mContext;

    private AppProfile() {

    }

    public static final Context getContext() {
        return mContext;
    }

    public static final String getPackageName() {
        return mContext.getPackageName();
    }


}

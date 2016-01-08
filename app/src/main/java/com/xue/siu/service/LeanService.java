package com.xue.siu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;

import org.json.JSONObject;

/**
 * Created by XUE on 2015/12/10.
 */
public class LeanService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LeanManager.init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

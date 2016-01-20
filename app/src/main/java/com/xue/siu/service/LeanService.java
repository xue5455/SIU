package com.xue.siu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.xue.siu.config.UserInfo;

import org.json.JSONObject;

/**
 * Created by XUE on 2015/12/10.
 */
public class LeanService extends Service {
    private boolean mIsLogged = false;
    private AVIMClientCallback mAVIMClientCallback = new AVIMClientCallback() {
        @Override
        public void done(AVIMClient avimClient, AVIMException e) {
            if (e == null) {
                mIsLogged = true;
                LeanManager.mClient = avimClient;
            } else {
                mIsLogged = false;
                login();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LeanManager.init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        super.onStartCommand(intent, flags, id);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void login() {
        if (!mIsLogged) {
            AVIMClient client = AVIMClient.getInstance(UserInfo.userId);
            client.open(mAVIMClientCallback);
        }
    }
}

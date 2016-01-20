package com.xue.siu.avim;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by XUE on 2016/1/18.
 */
public class AVIMClientManager {
    private static AVIMClientManager instance;
    private AVIMClient avimClient;
    private String clientId;

    private AVIMClientManager() {
    }

    public static AVIMClientManager getInstance() {
        if (instance == null) {
            synchronized (AVIMClientManager.class) {
                if (instance == null)
                    instance = new AVIMClientManager();
            }
        }
        return instance;
    }

    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(callback);
    }

    public AVIMClient getClient() {
        return avimClient;
    }

    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }
}

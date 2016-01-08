package com.xue.siu.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.HurlStack;
import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xue.eventbus.HTEventBus;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.util.SystemUtil;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by XUE on 2015/12/9.
 */
public class SIUApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();
        //  CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(context);

        // 所有实现IReceivierType的class都可以当做HTEventBus注册的对象
        HTEventBus.getDefault().registerReceiverClass(ISubscriber.class);
        ScreenUtil.GetInfo(context);
        AppProfile.mContext = context;

        // 进入应用就开始在后台请求网络时间
        SystemUtil.updateSystemBias();
        AVOSCloud.initialize(context, AppInfo.LEAN_ID, AppInfo.LEAN_SECRET);

        // initImageLoader();
        Fresco.initialize(context);
    }


}
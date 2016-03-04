package com.xue.siu.application;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.netease.hearttouch.htimagepicker.HTImagePicker;
import com.netease.hearttouch.htimagepicker.HTRuntimeConfig;
import com.netease.hearttouch.htimagepicker.imagescan.ImageScanUtil;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.eventbus.HTEventBus;
import com.xue.siu.avim.DefaultMessageHandler;
import com.xue.siu.common.util.EmojiUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.util.SystemUtil;
import com.xue.siu.common.view.refreshviewholder.DotStyleRefreshViewHolder;
import com.xue.siu.module.imagepick.activity.PickImageActivity;

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
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new DefaultMessageHandler(this));
        HTSwipeRecyclerView.initRefreshViewHolder(DotStyleRefreshViewHolder.class);
        EmojiUtil.init();
        HTRuntimeConfig config = new HTRuntimeConfig(PickImageActivity.class,
                null, null, null);
        HTImagePicker.getDefault().init(context, config);
        ImageScanUtil.scanImages(null, true);
    }


}
package com.netease.hearttouch.htimagepicker;

import android.content.Context;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.imagepick.activity.HTBaseImagePickActivity;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;

import java.util.ArrayList;

/**
 * Created by zyl06 on 2/18/16.
 */
public class HTImagePicker {
    // 包级作用域
    HTRuntimeConfig mConfig;

    public static HTImagePicker sDefault;

    public static HTImagePicker getDefault() {
        if (sDefault == null) {
            synchronized (HTImagePicker.class) {
                if (sDefault == null) {
                    sDefault = new HTImagePicker();
                }
            }
        }
        return sDefault;
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, @Nullable HTRuntimeConfig config) {
        ContextUtil.getInstance().init(context);

        mConfig = (config != null) ? config : new HTRuntimeConfig();
    }

    public HTRuntimeConfig getConfig() {
        return mConfig;
    }

    public void start(Context context, HTImageFrom from,
                      AlbumInfo albumInfo, ArrayList<PhotoInfo> photoInfos,
                      boolean isSupportOrig, boolean crop, int outputX, int outputY,
                      String title, HTPickFinishedListener listener) {
        start(context, from, albumInfo, photoInfos, false, 0, isSupportOrig,
                crop, outputX, outputY, title, listener);
    }

    public void start(Context context, HTImageFrom from,
                      AlbumInfo albumInfo, ArrayList<PhotoInfo> photoInfos,
                      HTPickFinishedListener listener) {
        start(context, from, albumInfo, photoInfos, false, 1, false,
                false, 0, 0, C.EMPTY, listener);
    }

    public void start(Context context, HTImageFrom from,
                      AlbumInfo albumInfo, ArrayList<PhotoInfo> photoInfos,
                      boolean needCrop, HTPickFinishedListener listener) {
        start(context, from, albumInfo, photoInfos, false, 1, false,
                needCrop, 0, 0, C.EMPTY, listener);
    }

    public void start(Context context,
                      AlbumInfo albumInfo, ArrayList<PhotoInfo> photoInfos,
                      boolean mutiSelectMode, int mutiSlectLimitSize, boolean isSupportOrig,
                      String title, HTPickFinishedListener listener) {
        start(context, HTImageFrom.FROM_LOCAL,
                albumInfo, photoInfos,
                mutiSelectMode, mutiSlectLimitSize, isSupportOrig,
                false, 0, 0, title, listener);
    }

    private void start(Context context, HTImageFrom from,
                       AlbumInfo albumInfo, ArrayList<PhotoInfo> photoInfos,
                       boolean mutiSelectMode, int mutiSlectLimitSize, boolean isSupportOrig,
                       boolean crop, int outputX, int outputY,
                       String title, HTPickFinishedListener listener) {

        HTBaseImagePickActivity.start(mConfig.getImagePickActivityClass(), context, from,
                albumInfo, photoInfos, mutiSelectMode,
                mutiSlectLimitSize, isSupportOrig,
                crop, outputX, outputY,
                title, listener);
    }
}

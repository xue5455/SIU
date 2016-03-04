package com.netease.hearttouch.htimagepicker.imagepreview.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.HTImagePicker;
import com.netease.hearttouch.htimagepicker.imagepreview.listener.IIntentProcess;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.ArrayList;

/**
 * Created by zyl06 on 2/23/16.
 */
public class ImagePreviewHelper {
    public static void startMultiImagePreview(@NonNull Activity activity,
                                               final ArrayList<PhotoInfo> photos,
                                               @Nullable IIntentProcess intentProcess) {
        Class imagePreviewActivityClass = HTImagePicker.getDefault().getConfig().getMultiImagePreviewActivityClazz();
        start(activity, imagePreviewActivityClass, photos, intentProcess);
    }

    public static void startSingleImagePreview(@NonNull Activity activity,
                                               PhotoInfo photoInfo,
                                               @Nullable IIntentProcess intentProcess) {
        Class imagePreviewActivityClass = HTImagePicker.getDefault().getConfig().getSingleImagePreviewActivity();
        ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
        photoInfos.add(photoInfo);
        start(activity, imagePreviewActivityClass, photoInfos, intentProcess);
    }

    private static void start(@NonNull Activity activity, @NonNull Class imagePreviousActivity,
                              final ArrayList<PhotoInfo> photos,
                              @Nullable IIntentProcess intentProcess) {
        Intent intent = new Intent(activity, imagePreviousActivity);
        intent.putExtra(HTBaseImagePreviewActivity.PHOTO_INFO_LIST_KEY, photos);
        if (intentProcess != null) {
            intentProcess.onProcessIntent(intent);
        }

        activity.startActivity(intent);
    }
}

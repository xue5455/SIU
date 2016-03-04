package com.netease.hearttouch.htimagepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.imagecrop.activity.HTBaseImageCropActivity;
import com.netease.hearttouch.htimagepicker.imagecrop.activity.HTImageCropActivity;
import com.netease.hearttouch.htimagepicker.imagepick.activity.HTBaseImagePickActivity;
import com.netease.hearttouch.htimagepicker.imagepick.activity.HTImagePickActivity;
import com.netease.hearttouch.htimagepicker.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.imagepreview.activity.HTMultiImagesPreviewActivity;
import com.netease.hearttouch.htimagepicker.imagepreview.activity.HTSingleImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.storage.StorageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zyl06 on 2/20/16.
 */
public class HTRuntimeConfig {
    private static final String TAG = "PickImage-Config";

    Class mImagePickActivityClazz = HTImagePickActivity.class;
    Class mSingleImagePreviewActivityClazz = HTSingleImagePreviewActivity.class;
    Class mMultiImagePreviewActivityClazz = HTMultiImagesPreviewActivity.class;
    Class mImageCropActivityClazz = HTImageCropActivity.class;

    public HTRuntimeConfig() {
        this(null, null, null, null);
    }

    public HTRuntimeConfig(@Nullable Class imagePickActivityClazz,
                           @Nullable Class singleImagePreviewActivityClazz,
                           @Nullable Class multiImagePreviewActivityClazz,
                           @Nullable Class imageCropActivityClazz) {
        if (checkClassExtendsBase(imagePickActivityClazz, HTBaseImagePickActivity.class)) {
            mImagePickActivityClazz = imagePickActivityClazz;
        } else if (imagePickActivityClazz != null) {
            Log.e(TAG, "pickImageActivity class is not extends of HTBaseImagePickActivity");
        }

        if (checkClassExtendsBase(singleImagePreviewActivityClazz, HTBaseImagePreviewActivity.class)) {
            mSingleImagePreviewActivityClazz = singleImagePreviewActivityClazz;
        } else if (singleImagePreviewActivityClazz != null) {
            Log.e(TAG, "singleImagePreviewActivity class is not extends of HTBaseImagePreviewActivity");
        }

        if (checkClassExtendsBase(multiImagePreviewActivityClazz, HTBaseImagePreviewActivity.class)) {
            mMultiImagePreviewActivityClazz = multiImagePreviewActivityClazz;
        } else if (multiImagePreviewActivityClazz != null) {
            Log.e(TAG, "multiImagePreviewActivity class is not extends of HTBaseImagePreviewActivity");
        }

        if (checkClassExtendsBase(imageCropActivityClazz, HTBaseImageCropActivity.class)) {
            mImageCropActivityClazz = imageCropActivityClazz;
        } else if (imageCropActivityClazz != null) {
            Log.e(TAG, "imageCropActivityClazz class is not extends of HTBaseImageCropActivity");
        }
    }

    /**
     * @return 拍照的照片保存的文件名，包含路径
     */
    public String getPhotoFileSavePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_" + C.FileSuffix.JPG;

        return StorageUtil.getWriteSystemCameraPath(fileName, true);
    }

    private Context getContext() {
        return ContextUtil.getInstance().getContext();
    }

    public Class getImagePickActivityClass() {
        return mImagePickActivityClazz;
    }

    public Class getSingleImagePreviewActivity() {
        return mSingleImagePreviewActivityClazz;
    }

    public Class getMultiImagePreviewActivityClazz() {
        return mMultiImagePreviewActivityClazz;
    }

    public Class getImageCropActivityClazz() {
        return mImageCropActivityClazz;
    }

    private boolean checkClassExtendsBase(Class clz, @NonNull Class base) {
        Class superClz = clz;
        while (superClz != null && superClz != base) {
            superClz = superClz.getSuperclass();
        }

        return superClz == base;
    }
}

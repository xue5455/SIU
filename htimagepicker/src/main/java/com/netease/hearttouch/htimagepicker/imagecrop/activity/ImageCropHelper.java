package com.netease.hearttouch.htimagepicker.imagecrop.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.HTImagePicker;
import com.netease.hearttouch.htimagepicker.imagepick.activity.Extras;
import com.netease.hearttouch.htimagepicker.imagepreview.listener.IIntentProcess;

/**
 * Created by zyl06 on 2/23/16.
 */
public class ImageCropHelper {
    public static void startForFile(@NonNull Activity activity, String srcFile, int outputX, int outputY, String path, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        startForFile(activity, srcFile, outputX, outputY, path, "", requestCode, intentProcess);
    }

    public static void startForFile(@NonNull Activity activity, String srcFile, int outputX, int outputY,
                                    String path, String from, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        Class imageCropActivityClazz = HTImagePicker.getDefault().getConfig().getImageCropActivityClazz();

        Intent intent = new Intent(activity, imageCropActivityClazz);

        intent.putExtra(Extras.EXTRA_SRC_FILE, srcFile);
        intent.putExtra(Extras.EXTRA_OUTPUTX, outputX);
        intent.putExtra(Extras.EXTRA_OUTPUTY, outputY);
        intent.putExtra(Extras.EXTRA_FILE_PATH, path);
        intent.putExtra(Extras.EXTRA_FROM, from);
        activity.startActivityForResult(intent, requestCode);

        if (intentProcess != null) {
            intentProcess.onProcessIntent(intent);
        }

        activity.startActivity(intent);
    }

    public static void startForData(Activity activity,
                                    String srcFile, int outputX, int outputY, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        startForData(activity, srcFile, outputX, outputY, "", requestCode, intentProcess);
    }

    public static void startForData(Activity activity, String srcFile,
                                    int outputX, int outputY, String from, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        Class imageCropActivityClazz = HTImagePicker.getDefault().getConfig().getImageCropActivityClazz();
        Intent intent = new Intent(activity, imageCropActivityClazz);
        intent.putExtra(Extras.EXTRA_SRC_FILE, srcFile);
        intent.putExtra(Extras.EXTRA_OUTPUTX, outputX);
        intent.putExtra(Extras.EXTRA_OUTPUTY, outputY);
        intent.putExtra(Extras.EXTRA_RETURN_DATA, true);
        intent.putExtra(Extras.EXTRA_FROM, from);

        if (intentProcess != null) {
            intentProcess.onProcessIntent(intent);
        }

        activity.startActivityForResult(intent, requestCode);
    }
}

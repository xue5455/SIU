package com.netease.hearttouch.htimagepicker.util.storage;

import android.os.Environment;
import android.text.TextUtils;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.image.ImageUtil;

import java.io.File;

/**
 * Created by zyl06 on 2/18/16.
 */
public class StorageUtil {
    // 外置存储卡默认预警临界值
    private static final long THRESHOLD_WARNING_SPACE = 100 * C.M;

    // 保存文件时所需的最小空间的默认值
    public static final long THRESHOLD_MIN_SPCAE = 20 * C.M;

    /**
     * 获取文件保存路径，空间不足时有toast提示
     * @param fileName
     * @param fileType
     * @return 可用的保存路径或者null
     */
    public static String getWritePath(String fileName, StorageType fileType) {
        return getWritePath(fileName, fileType, true);
    }

    /**
     * 获取文件保存路径
     *
     * @param fileName 文件全名
     * @param tip      空间不足时是否给出默认的toast提示
     * @return 可用的保存路径或者null
     */
    private static String getWritePath(String fileName, StorageType fileType, boolean tip) {
        if (hasEnoughSpaceForWrite(fileType, tip)) {
            String path = ExternalStorage.getInstance().getWritePath(fileName, fileType);
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            File dir = new File(path).getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            return path;
        }
        return null;
    }

    /**
     * 判断外部存储是否存在，以及是否有足够空间保存指定类型的文件
     *
     * @param fileType
     * @param tip  是否需要toast提示
     * @return false: 无存储卡或无空间可写, true: 表示ok
     */
    public static boolean hasEnoughSpaceForWrite(StorageType fileType, boolean tip) {
        return hasEnoughSpaceForWrite(fileType.getStorageMinSize(), tip);
    }

    public static boolean hasEnoughSystemSpaceForWrite(boolean tip) {
        return hasEnoughSpaceForWrite(THRESHOLD_MIN_SPCAE, tip);
    }

    private static boolean hasEnoughSpaceForWrite(long storageMinSize, boolean tip) {
        if (!ExternalStorage.getInstance().isExternalStorageExist()) {
            if (tip) {
                ContextUtil.getInstance().makeToast(R.string.sdcard_not_exist_error);
            }
            return false;
        }

        long residual = ExternalStorage.getInstance().getAvailableExternalSize();
        if (residual < storageMinSize) {
            if (tip) {
                ContextUtil.getInstance().makeToast(R.string.sdcard_not_enough_error);
            }
            return false;
        } else if (residual < THRESHOLD_WARNING_SPACE) {
            if (tip) {
                ContextUtil.getInstance().makeToast(R.string.sdcard_not_enough_warning);
            }
        }

        return true;
    }

    public static String getWriteSystemCameraPath(String fileName, boolean tip) {
        if (TextUtils.isEmpty(fileName)) return null;

        if (hasEnoughSystemSpaceForWrite(tip)) {
            String cameraPath = ImageUtil.getCameraFilePath();
            if (!TextUtils.isEmpty(cameraPath)) {
                return cameraPath + "/" + fileName;
            } else {
                return StorageUtil.getWriteSystemDCIMPath("Camera/" + fileName, true);
            }
        }
        return null;
    }

    public static String getWriteSystemDCIMPath(String fileName, boolean tip) {
        return getWriteSystemPath(fileName, Environment.DIRECTORY_DCIM, tip);
    }

    public static String getWritePicturePath(String fileName, boolean tip) {
        return getWriteSystemPath(fileName, Environment.DIRECTORY_PICTURES, tip);
    }

    public static String getWriteSystemPath(String fileName, String directoryType, boolean tip) {
        if (hasEnoughSystemSpaceForWrite(tip)) {
            File file = Environment.getExternalStoragePublicDirectory(directoryType);
            if (file != null) {
                String path = file.getAbsolutePath();

                if (TextUtils.isEmpty(path)) {
                    return null;
                }
                File dir = new File(path).getParentFile();
                if (dir != null && !dir.exists()) {
                    dir.mkdirs();
                }
                return path + "/" + fileName;
            }
        }
        return null;
    }
}

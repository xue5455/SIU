package com.netease.hearttouch.htimagepicker.util.storage;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.util.ContextUtil;

import java.io.File;

public class ExternalStorage {
    private static final String TAG = "ExternalStorage";

    private static ExternalStorage sInstance;

    /**
     * 程序所需的文件夹是否都已经创建成功
     */
    private boolean mFoldersReady = false;

    /**
     * 外部存储是否MOUNTED
     */
    private boolean mMounted = false;
    /**
     * 外部存储根目录
     */
    private String mExternalStorageDir = null;

    private ExternalStorage() {}

    synchronized public static ExternalStorage getInstance() {
        if (sInstance == null) {
            sInstance = new ExternalStorage();
            sInstance.loadStorageState();
        }
        return sInstance;
    }

    private void loadStorageState() {
        mMounted = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (mMounted) {
            String externalPath = Environment.getExternalStorageDirectory().getPath();
            if (externalPath.equals(mExternalStorageDir)) {
                return;
            }
            mExternalStorageDir = externalPath;

            String appDirectory = mExternalStorageDir + "/" + ContextUtil.getInstance().getAppName();
            File root = new File(appDirectory);
            if (root.exists() && !root.isDirectory()) {
                root.delete();
            }

            Log.i(TAG, "external path is: " + mExternalStorageDir);
        } else {
            mExternalStorageDir = null;
        }
    }

    /**
     * 外置存储卡是否存在
     * @return
     */
    public boolean isExternalStorageExist() {
        return mMounted;
    }

    /**
     * 获取外置存储卡剩余空间
     * @return
     */
    public long getAvailableExternalSize() {
        return getResidualSpace(mExternalStorageDir);
    }

    /**
     * 获取目录剩余空间
     * @param directoryPath
     * @return
     */
    private long getResidualSpace(String directoryPath) {
        try {
            StatFs sf = new StatFs(directoryPath);
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            long availCountByte = availCount * blockSize;
            return availCountByte;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 文件全名转绝对路径（写）
     * @param fileName 文件全名（文件名.扩展名）
     * @return 返回绝对路径信息
     */
    public String getWritePath(String fileName, StorageType fileType) {
        if(TextUtils.isEmpty(fileName) || !checkSubFolders()) {
            return "";
        }
        return pathForName(fileName, fileType);
    }

    private boolean checkSubFolders() {
        if (!mFoldersReady) {
            return createSubFolders();
        }
        return true;
    }

    private boolean createSubFolders() {
        if (!isExternalStorageExist()) {
            return false;
        }
        boolean result = true;
        String appDirectory = ContextUtil.getInstance().getAppDir();
        File root = new File(appDirectory);
        if (root.exists() && !root.isDirectory()) {
            root.delete();
        }
        for (StorageType storageType : StorageType.values()) {
            result &= makeDirectory(appDirectory + storageType.getStoragePath());
        }

        mFoldersReady = result;
        return mFoldersReady;
    }

    /**
     * 创建目录
     * @param path
     * @return
     */
    private boolean makeDirectory(String path) {
        File file = new File(path);
        boolean exist = file.exists();
        if(!exist) {
            exist = file.mkdirs();
        }
        return exist;
    }

    private String pathForName(String fileName, StorageType type) {
        String directory = getDirectoryByDirType(type);
        if (directory != null && !TextUtils.isEmpty(fileName)) {
            return directory + fileName;
        }
        return "";
    }

    /**
     * 返回指定类型的文件夹路径
     * @param fileType
     * @return
     */
    public String getDirectoryByDirType(StorageType fileType) {
        if(isExternalStorageExist()) {
            return ContextUtil.getInstance().getAppDir() + fileType.getStoragePath();
        }

        return null;
    }
}

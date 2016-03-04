package com.netease.hearttouch.htimagepicker.util.storage;

/**
 * Created by zyl06 on 2/18/16.
 */
public enum StorageType {
    TYPE_DATA(DirectoryName.DATA_DIRECTORY_NAME, false),

    TYPE_TXT(DirectoryName.TXT_DIRECTORY_NAME, false),

    TYPE_LOG(DirectoryName.LOG_DIRECTORY_NAME, false),

    TYPE_APK(DirectoryName.APK_DIRECTORY_NAME, false),

    TYPE_TEMP(DirectoryName.TEMP_DIRECTORY_NAME, false),

    TYPE_FILE(DirectoryName.FILE_DIRECTORY_NAME, false),

    TYPE_IMAGE_CACHE(DirectoryName.IMAGE_CACHE_DIRECTORY_NAME, false),

    TYPE_IMAGE(DirectoryName.IMAGE_DIRECTORY_NAME, true),

    TYPE_AUDIO(DirectoryName.AUDIO_DIRECTORY_NAME, true),

    TYPE_VIDEO(DirectoryName.VIDEO_DIRECTORY_NAME, true),

    TYPE_THUMB_IMAGE(DirectoryName.THUMB_DIRECTORY_NAME, true),

    TYPE_THUMB_VIDEO(DirectoryName.THUMB_DIRECTORY_NAME, true),

    TYPE_THUMB_MUSIC(DirectoryName.THUMB_DIRECTORY_NAME, true),

    TYPE_THUMB_SHARE(DirectoryName.THUMB_DIRECTORY_NAME, true);


    private DirectoryName storageDirectoryName;
    private boolean storageByMD5;
    private long storageMinSize;

    public String getStoragePath() {
        return storageDirectoryName.getPath();
    }

    public boolean isStorageByMD5() {
        return storageByMD5;
    }

    public long getStorageMinSize() {
        return storageMinSize;
    }

    StorageType(DirectoryName dirName, boolean storageByMD5) {
        this(dirName, storageByMD5, StorageUtil.THRESHOLD_MIN_SPCAE);
    }

    StorageType(DirectoryName dirName, boolean storageByMD5, long storageMinSize) {
        this.storageDirectoryName = dirName;
        this.storageByMD5 = storageByMD5;
        this.storageMinSize = storageMinSize;
    }

    enum DirectoryName {
        DATA_DIRECTORY_NAME("data/", CacheClearStrategy.CLEAR_ALL),
        TXT_DIRECTORY_NAME("txt/", CacheClearStrategy.CLEAR_ALL),
        APK_DIRECTORY_NAME("apk/", CacheClearStrategy.CLEAR_ALL),
        FILE_DIRECTORY_NAME("file/", CacheClearStrategy.CLEAR_ALL),
        LOG_DIRECTORY_NAME("log/", CacheClearStrategy.CLEAR_KEEP_RECENTLY),
        TEMP_DIRECTORY_NAME("temp/", CacheClearStrategy.CLEAR_ALL),

        AUDIO_DIRECTORY_NAME("audio/", CacheClearStrategy.CLEAR_ALL),
        VIDEO_DIRECTORY_NAME("video/", CacheClearStrategy.CLEAR_ALL),
        IMAGE_DIRECTORY_NAME("image/", CacheClearStrategy.CLEAR_ALL),
        THUMB_DIRECTORY_NAME("thumb/", CacheClearStrategy.CLEAR_KEEP_RECENTLY),
        IMAGE_CACHE_DIRECTORY_NAME("image_cache/", CacheClearStrategy.CLEAR_ALL);

        private String path;
        private CacheClearStrategy cacheClearStrategy;

        public String getPath() {
            return path;
        }

        public boolean needClearCache() {
            return cacheClearStrategy != CacheClearStrategy.CLEAR_NEVER;
        }

        public int getKeepCacheDays() {
            return cacheClearStrategy.keepCacheDays;
        }

        private DirectoryName(String path, CacheClearStrategy cacheClearStrategy) {
            this.path = path;
            this.cacheClearStrategy = cacheClearStrategy;
        }
    }

    enum CacheClearStrategy {
        CLEAR_NEVER(-1),
        CLEAR_ALL(0),
        CLEAR_KEEP_RECENTLY(7);

        private int keepCacheDays;//>=0的表示清理缓存时需要删除，具体数字代表删除时需要保留的天数 <0的表示清理缓存时不删除

        private CacheClearStrategy(int keepCacheDays) {
            this.keepCacheDays = keepCacheDays;
        }
    }
}
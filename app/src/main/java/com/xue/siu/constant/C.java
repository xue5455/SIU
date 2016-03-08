package com.xue.siu.constant;


import android.os.Build;

/**
 * Created by XUE on 2015/12/9.
 */
public class C {
    // 关于文件后缀的常量
    public static final class FileSuffix {
        public static final String JPG = ".jpg";

        public static final String PNG = ".png";

        public static final String M4A = ".m4a";

        public static final String THREE_3GPP = ".3gp";

        public static final String BMP = ".bmp";

        public static final String MP4 = ".mp4";

        public static final String AMR_NB = ".amr";

        public static final String APK = ".apk";

        public static final String AAC = ".aac";

        public static final String GIF = ".gif";
    }

    public static final String FILE_PREFIX = "file://";

    public static final int MIN_SDK_ENABLE_LAYER_TYPE_HARDWARE = Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    // 头像图像的尺寸
    public static final int PORTRAIT_IMAGE_SIZE = 720;

    // 内存大小
    public static final int BYTES_IN_K = 1024;
    public static final int BYTES_IN_M = 1024 * 1024;
    public static final int BYTES_IN_G = 1024 * 1024 * 1024;


    public static class EXTRA {
        public static final String BASE = "com.netease.yanxuan.EXTRA_";
        public static final String TAGNAMES = BASE + "tagNames";
        public static final String LISTENER_KEY = BASE + "listenerKey";
    }


    // 内存大小
    public final static String COMMA = ",";
    public final static String DOT = ".";
    public final static String COLON = ":";
    public final static String SEMICOLON = ";";
    public final static String SPACER = " ";
    public final static String RETURN = "\n";
    public final static String EMPTY = "";

    //网页编码
    public final static String HTML_MIME_TYPE = "text/html";
    public final static String HTML_ENCODING = "utf-8";

}


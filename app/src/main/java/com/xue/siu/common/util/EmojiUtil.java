package com.xue.siu.common.util;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.R;
import com.xue.siu.common.view.imagespan.OSImageSpan;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XUE on 2016/1/29.
 */
public class EmojiUtil {
    public static final int FACE_COUNT_ONE_PAGE = 21;
    public static final int FACE_ACTUAL_COUNT_ONE_PAGE = FACE_COUNT_ONE_PAGE - 1;
    public static final String FACE_DELETE_KEY = "[delete]";
    public static final int FACE_DELETE_ID = R.drawable.icon_face_delete;
    private static final String TAG = "EmojiUtil";
    public static LinkedHashMap<String, Integer> mEmojiMap = new LinkedHashMap<>();
    public static List<PrefixWrapper> mPrefixList = new ArrayList<>();
    public static List<FaceWrapper> mEmojiWrappers = new ArrayList<>();
    private static int mCount = 0;

    private static HashMap<String, Bitmap> mFaceCache;

    static {
        mPrefixList.add(new PrefixWrapper("ali_", 70, 3));
        mPrefixList.add(new PrefixWrapper("b", 20, 2));
        mPrefixList.add(new PrefixWrapper("yz_", 8, 3));
        mPrefixList.add(new PrefixWrapper("image_emoticon", 51, 1));
    }

    public static void init() {
        mEmojiMap.put(FACE_DELETE_KEY, FACE_DELETE_ID);
        for (PrefixWrapper wrapper : mPrefixList) {
            initEmoji(wrapper.prefix, wrapper.imageCounts, wrapper.suffixLimit);
        }
        if (mEmojiWrappers.size() % 21 != 0) {
            mEmojiWrappers.add(new FaceWrapper(FACE_DELETE_KEY, FACE_DELETE_ID, false));
        }
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int mCacheSize = maxMemory / 8;
        mFaceCache = new HashMap<>();
    }

    public static void addFaceToCache(String faceStr, Bitmap imageSpan) {
        mFaceCache.put(faceStr, imageSpan);
    }

    public static Bitmap getSpanFromCache(String faceStr) {
        return mFaceCache.get(faceStr);
    }

    public static int getDrawableRes(String key) {
        return mEmojiMap.get(key);
    }

    public static boolean contains(String str) {
        return mEmojiMap.containsKey(str);
    }

    public static FaceWrapper getWrapper(int position) {
        if (position < mEmojiWrappers.size()) {
            return mEmojiWrappers.get(position);
        }
        return null;
    }

    public static int getFaceCount() {
        return mEmojiWrappers.size();
    }

    /**
     * @param prefix      图片前缀
     * @param numbers     图片个数
     * @param suffixLimit 图片后缀的最短字数
     */
    public static void initEmoji(String prefix, int numbers, int suffixLimit) {
        StringBuilder sb;
        for (int i = 1; i <= numbers; i++) {
            sb = new StringBuilder(prefix);
            sb.append(fillBlank(i, suffixLimit));
            try {
                int drawableId = getDrawableId(sb.toString());
                sb.insert(0, "[");
                sb.append("]");
                mEmojiMap.put(sb.toString(), drawableId);
                mEmojiWrappers.add(new FaceWrapper(sb.toString(), drawableId, true));
                if (mEmojiWrappers.size() % FACE_COUNT_ONE_PAGE == FACE_ACTUAL_COUNT_ONE_PAGE) {
                    mEmojiWrappers.add(new FaceWrapper(FACE_DELETE_KEY, FACE_DELETE_ID, false));
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

    }

    public static String fillBlank(int number, int count) {
        String num = String.valueOf(number);
        if (num.length() < count) {
            StringBuilder sb = new StringBuilder(3);
            for (int i = 0; i < count - num.length(); i++)
                sb.append("0");
            sb.append(num);
            return sb.toString();
        }
        return num;
    }

    public static int getDrawableId(String drawableName) throws Exception {
        Class clazz = R.drawable.class;
        try {
            Field field = clazz.getField(drawableName);
            field.setAccessible(true);
            int drawableId = (int) field.get(null);
            return drawableId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new Exception("no such a drawable called " + drawableName);
    }

    public static int getPageCount() {
        return (int) Math.ceil(mEmojiMap.keySet().size() / (FACE_ACTUAL_COUNT_ONE_PAGE * 1.0f));
    }

    private static class PrefixWrapper {
        String prefix;
        int imageCounts;
        int suffixLimit;

        public PrefixWrapper(String prefix, int imageCounts, int suffixLimit) {
            this.prefix = prefix;
            this.imageCounts = imageCounts;
            this.suffixLimit = suffixLimit;
        }
    }

    public static class FaceWrapper {
        String key;
        int id;
        boolean isEmoji;

        public FaceWrapper(String key, int id, boolean isEmoji) {
            this.key = key;
            this.id = id;
            this.isEmoji = isEmoji;
        }

        public String getKey() {
            return key;
        }

        public int getId() {
            return id;
        }

        public boolean isEmoji() {
            return this.isEmoji;
        }
    }
}

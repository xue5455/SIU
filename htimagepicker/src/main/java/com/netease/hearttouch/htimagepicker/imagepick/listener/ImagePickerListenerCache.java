package com.netease.hearttouch.htimagepicker.imagepick.listener;

import android.content.Context;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by zyl06 on 2/20/16.
 */
public class ImagePickerListenerCache {
    public static final int INVALID_IMAGE_PICK_KEY = 0;
    private static WeakHashMap<Context, HTPickFinishedListener> sPickerFinishedListeners =
            new WeakHashMap<Context, HTPickFinishedListener>();

    public static int insertImagePickerListener(Context context, HTPickFinishedListener listener) {
        if (context != null && listener != null) {
            sPickerFinishedListeners.put(context, listener);
            return context.hashCode();
        }
        return INVALID_IMAGE_PICK_KEY;
    }

    public static void callImagePickerFinishListener(int key, @Nullable AlbumInfo albumInfo,
                                                     List<PhotoInfo> pickPhotos) {
        if (key == INVALID_IMAGE_PICK_KEY)
            return;

        Context contextKey = getContextKey(key);

        if (contextKey != null) {
            HTPickFinishedListener listener = sPickerFinishedListeners.get(contextKey);
            if (listener != null) {
                listener.onImagePickFinished(albumInfo, pickPhotos);
            }
            sPickerFinishedListeners.remove(contextKey);
        }
    }

    public static void callImagePickerCancelListener(int key) {
        if (key == INVALID_IMAGE_PICK_KEY)
            return;

        Context contextKey = getContextKey(key);

        if (contextKey != null) {
            HTPickFinishedListener listener = sPickerFinishedListeners.get(contextKey);
            if (listener != null) {
                listener.onImagePickCanceled();
            }
            sPickerFinishedListeners.remove(contextKey);
        }
    }

    private static Context getContextKey(int key) {
        Context contextKey = null;
        for (WeakHashMap.Entry<Context, HTPickFinishedListener> entry : sPickerFinishedListeners.entrySet()) {
            Context context = entry.getKey();
            if (context != null && context.hashCode() == key) {
                contextKey = context;
                break;
            }
        }
        return contextKey;
    }
}

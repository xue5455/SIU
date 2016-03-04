package com.netease.hearttouch.htimagepicker.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.libcore.io.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zyl06 on 3/1/16.
 */
public class BitmapDiskCache implements IBitmapCache {
    private static final String TAG = "HT_BitmapDiskCache";
    private File cacheFileDir = null;
    DiskLruCache diskLruCache;

    public BitmapDiskCache(String cacheFileName) {
        try {
            cacheFileDir = getDiskCacheDir(cacheFileName);
            diskLruCache = DiskLruCache.open(cacheFileDir, ContextUtil.getInstance().getAppVersionCode(), 1, 10*1024*1024);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String key) {
        try {
            String hashKey = hashKeyForDisk(key);
            DiskLruCache.Snapshot snapshot = diskLruCache.get(hashKey);
            if (snapshot != null) {
                InputStream is = snapshot.getInputStream(0);
                Bitmap bm = BitmapFactory.decodeStream(is);
                return bm;
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key
     * @param value
     * @return null 不去解析原来老的bitmap数据
     */
    @Override
    public Bitmap put(String key, Bitmap value) {
        try {
            String hashKey = hashKeyForDisk(key);
            DiskLruCache.Editor editor = diskLruCache.edit(hashKey);
            if (editor != null) {
                OutputStream os = editor.newOutputStream(0);
                boolean success = value.compress(Bitmap.CompressFormat.JPEG, 75, os);
                os.close();
                if (success) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
            diskLruCache.flush();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    @Override
    public void close() {
        try {
            diskLruCache.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private File getDiskCacheDir(String uniqueName) {
        return getDiskCacheDir(ContextUtil.getInstance().getContext(), uniqueName);
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}

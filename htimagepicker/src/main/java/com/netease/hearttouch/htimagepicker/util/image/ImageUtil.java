package com.netease.hearttouch.htimagepicker.util.image;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.MediaDAO;
import com.netease.hearttouch.htimagepicker.imagescan.ImageScanUtil;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by zyl06 on 2/18/16.
 */
public class ImageUtil {
    private static String TAG = "htimagepicker_ImageUtil";
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void setImagePath(ImageView imageView, String imagePath) {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        setImagePath(imageView, imagePath, targetW, targetH);
    }

    public static void setImagePath(final ImageView imageView, final String imagePath,
                                    final int desWidth, final int desHeight) {
        setImagePath(imageView, imagePath, desWidth, desHeight, false);
    }

    public static void setImagePath(final ImageView imageView, final String imagePath,
                                    final int desWidth, final int desHeight, boolean autoRotate) {
        Bitmap bm = BitmapCache.getDefault().getFromMemory(imagePath);
        if (bm != null) {
            imageView.setImageBitmap(bm);
        } else {
            ImageAsyncTask task = new ImageAsyncTask(imageView, imagePath, desWidth, desHeight, autoRotate);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private static void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        if (imageView != null && bitmap != null && !bitmap.isRecycled()) {
            imageView.setImageBitmap(bitmap);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
            alphaAnimation.setDuration(300);                  //设置持续时间
            imageView.setAnimation(alphaAnimation);
            alphaAnimation.startNow();
        }
    }

    private static class ImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        WeakReference<ImageView> imageViewRef;
        String imagePath;
        int desWidth, desHeight;
        boolean autoRotate;
        public ImageAsyncTask(ImageView imageView, String imagePath, int width, int height, boolean autoRotate) {
            imageView.setTag(imagePath);
            imageView.setImageBitmap(null);
            this.imageViewRef = new WeakReference<ImageView>(imageView);
            this.imagePath = imagePath;
            this.desWidth = width;
            this.desHeight = height;
            this.autoRotate = autoRotate;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = BitmapCache.getDefault().get(imagePath);
            if (bitmap == null) {
                bitmap = BitmapDecoder.decodeFile(imagePath, desWidth, desHeight, autoRotate);
                BitmapCache.getDefault().put(imagePath, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = imageViewRef.get();
            if (imageView != null) {
                if (imagePath.equals(imageView.getTag())) {
                    setImageBitmap(imageView, result);
                }
            }
        }
    }

    public static float getImageRotate(String imageFilePath) {
        if (TextUtils.isEmpty(imageFilePath)) {
            return 0;
        }

        ExifInterface localExifInterface;
        try {
            localExifInterface = new ExifInterface(imageFilePath);
            int rotateInt = localExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            return getImageRotate(rotateInt);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return 0;
    }

    /**
     * 获得旋转角度
     *
     * @param rotate
     * @return
     */
    private static float getImageRotate(int rotate) {
        float f;
        if (rotate == 6) {
            f = 90.0F;
        } else if (rotate == 3) {
            f = 180.0F;
        } else if (rotate == 8) {
            f = 270.0F;
        } else {
            f = 0.0F;
        }

        return f;
    }

    /**
     * Image Save
     */
    // TODO 应该是可配置的
    private static String IMAGE_SAVE_PATH;

    private static String getSavePath() {
        if (IMAGE_SAVE_PATH == null) {
            IMAGE_SAVE_PATH = ContextUtil.getInstance().getAppDir() + "image/save/";
        }

        return IMAGE_SAVE_PATH;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path, boolean recycle) {
        if (bitmap == null || TextUtils.isEmpty(path)) {
            return false;
        }

        BufferedOutputStream bos = null;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

            return true;
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
            if (recycle) {
                bitmap.recycle();
            }
        }
    }

    public static void saveImage(Context context, File fromFile, String toFileName) {
        String imgPath = getSavePath() + toFileName;
        File toFile = new File(imgPath);
        SaveImageAsyncTask task = new SaveImageAsyncTask(context, fromFile, toFile);
        task.execute();
    }

//    public static void addImageToGallery(String filePath, Context context) {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//        values.put(MediaStore.MediaColumns.DATA, filePath);
//        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//    }

    public static void addImageToGallery(String albumPath, Context context) {
        if (TextUtils.isEmpty(albumPath)) {
            return;
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(albumPath);
        if (f.exists()) {
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
    }

    private static String sCameraFilePath = null;

    public static String getCameraFilePath() {
        if (sCameraFilePath != null) return sCameraFilePath;

        Cursor cursor = ImageScanUtil.getCameraPhotos(ContextUtil.getInstance().getContext());

        if (cursor != null && cursor.moveToFirst()) {
            String desPathName = null;
            do {
                String pathName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if (desPathName == null) {
                    desPathName = pathName;
                }
                if (pathName.contains("Camera")) {
                    desPathName = pathName;
                    break;
                }
            } while (cursor.moveToNext());

            File file = new File(desPathName);
            if (file.exists()) {
                sCameraFilePath = file.getParent();
            }
        }

        MediaDAO.safelyCloseCursor(cursor);

        return sCameraFilePath;
    }

    static class SaveImageAsyncTask extends AsyncTask<Object, Integer, String> {
        Context mContext;
        File mFromFile;
        File mToFile;

        public SaveImageAsyncTask(Context context, File fromFile, File toFile) {
            mContext = context;
            mFromFile = fromFile;
            mToFile = toFile;
        }

        @Override
        protected String doInBackground(Object[] params) {
            FileUtil.copyFile(mFromFile, mToFile, true);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            addImageToGallery(mToFile.getAbsolutePath(), mContext);
            ContextUtil.getInstance().makeToast(R.string.pic_save_success);
        }
    }
}

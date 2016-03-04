package com.netease.hearttouch.htimagepicker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by zyl06 on 6/23/15.
 */
public class MediaDAO {
    private static final String TAG = "MediaDAO";

    private MediaDAO() {
    }

    public static Cursor getCameraPhotos(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
        return context.getContentResolver().query(images, projection, query,
                null, MediaStore.Images.Media.DATE_ADDED + " DESC");
    }

    public static Cursor getCameraThumbnails(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Images.Thumbnails._ID,
                MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA
        };
        final Uri images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Images.Thumbnails.DATA + " LIKE \"%DCIM%\"";
        return context.getContentResolver().query(images, projection, query,
                null, MediaStore.Images.Thumbnails._ID + " DESC");
    }

    public static Cursor getCameraVideosThumbnails(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Video.Thumbnails._ID,
                MediaStore.Video.Thumbnails.DATA };
        final Uri videos = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Video.Thumbnails.DATA + " LIKE \"%DCIM%\"";
        return context.getContentResolver().query(videos, projection, query,
                null, MediaStore.Video.Thumbnails.DEFAULT_SORT_ORDER);
    }

    public static Cursor getCameraVideos(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA
        };
        final Uri videos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Video.Media.DATA + " LIKE \"%DCIM%\"";
        return context.getContentResolver().query(videos, projection, query,
                null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
    }

    public static Cursor getAllMediaPhotos(final Context context) {
//		final String[] projection = new String[] {
//              MediaStore.Images.Media._ID,
//				MediaStore.Images.Media.DATA
//      };
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(images, null, null,
                    null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        } catch (Exception e){
            Log.e(TAG, "getAllMediaPhotos exception");
        }

        return cursor;
    }

    public static Cursor getAllMediaThumbnails(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Images.Thumbnails._ID,
                MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA
        };
        final Uri images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try{
            cursor = context.getContentResolver().query(images, projection, null,
                    null, MediaStore.Images.Thumbnails._ID + " DESC");
        } catch(Exception e) {
            Log.e(TAG, "getAllMediaThumbnails exception");
        }
        return cursor;
    }

    public static Cursor getAllMediaVideosThumbnails(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Video.Thumbnails._ID,
                MediaStore.Video.Thumbnails.DATA
        };
        final Uri videos = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
        return context.getContentResolver().query(videos, projection, null,
                null, MediaStore.Video.Thumbnails.DEFAULT_SORT_ORDER);
    }

    public static Cursor getAllMediaVideos(final Context context) {
        final String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA
        };
        final Uri videos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return context.getContentResolver().query(videos, projection, null,
                null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
    }

    public static Uri getLastPhotoFromAllPhotos(final Context context) {
        Cursor allMediaPhotos = getAllMediaPhotos(context);
        Uri uri = getFirstImageItemUri(allMediaPhotos);
        safelyCloseCursor(allMediaPhotos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastPhotoThumbnailFromAllPhotos(final Context context) {
        Cursor allMediaPhotos = getAllMediaThumbnails(context);
        Uri uri = getFirstImageThumbnailUri(allMediaPhotos);
        safelyCloseCursor(allMediaPhotos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastPhotoFromCameraPhotos(final Context context) {
        Cursor cameraPhotos = getCameraPhotos(context);
        Uri uri = getFirstImageItemUri(cameraPhotos);
        safelyCloseCursor(cameraPhotos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastThumbnailFromCameraPhotos(final Context context) {
        Cursor cameraPhotos = getCameraThumbnails(context);
        Uri uri = getFirstImageThumbnailUri(cameraPhotos);
        safelyCloseCursor(cameraPhotos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastVideoThumbnailFromAllVideos(final Context context) {
        Cursor allMediaVideos = getAllMediaVideosThumbnails(context);
        Uri uri = getFirstVideoThumbnailUri(allMediaVideos);
        safelyCloseCursor(allMediaVideos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastVideoThumbnailFromCameraVideos(final Context context) {
        Cursor cameraVideos = getCameraVideosThumbnails(context);
        Uri uri = getFirstVideoThumbnailUri(cameraVideos);
        safelyCloseCursor(cameraVideos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastVideoFromCameraVideos(final Context context) {
        Cursor cameraVideos = getCameraVideos(context);
        Uri uri = getFirstVideoItemUri(cameraVideos);
        safelyCloseCursor(cameraVideos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static Uri getLastVideoFromAllVideos(final Context context) {
        Cursor allVideos = getAllMediaVideos(context);
        Uri uri = getFirstVideoItemUri(allVideos);
        safelyCloseCursor(allVideos);
        if (uri == null) {
            return Uri.parse("");
        }
        return uri;
    }

    public static String getVideoThumbnailFromCursor(final Context context,
                                                     final Cursor dataCursor,
                                                     int position) {
        String thumbPath = null;
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };
        if (dataCursor.moveToPosition(position)) {
            int id = dataCursor.getInt(dataCursor
                    .getColumnIndex(MediaStore.Video.Media._ID));
            Cursor thumbCursor = context.getContentResolver()
                    .query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                            thumbColumns,
                            MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id,
                            null, null);
            if (thumbCursor.moveToFirst()) {
                thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
            }
            safelyCloseCursor(thumbCursor);
        }
        return thumbPath;
    }

    public static String getImagePathFromCursor(final Context context,
                                                final Cursor dataCursor, int position) {
        String imagePath = null;
        String[] imageColumns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };
        if (dataCursor.moveToPosition(position)) {
            int id = dataCursor.getInt(dataCursor
                    .getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
            Cursor imageCursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
                    MediaStore.Images.Media._ID + "=" + id, null, null);
            if (imageCursor.moveToFirst()) {
                imagePath = imageCursor.getString(imageCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
            safelyCloseCursor(imageCursor);
        }
        return imagePath;
    }

    private static Uri getFirstImageItemUri(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            return Uri.fromFile(new File(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA))));
        }
        return null;
    }

    private static Uri getFirstImageThumbnailUri(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            return Uri.fromFile(new File(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Thumbnails.DATA))));
        }
        return null;
    }

    private static Uri getFirstVideoItemUri(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            return Uri.fromFile(new File(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.DATA))));
        }
        return null;
    }

    private static Uri getFirstVideoThumbnailUri(Cursor cursor) {
        if (cursor != null && cursor.moveToLast()) {
            return Uri.fromFile(new File(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Thumbnails.DATA))));
        }
        return null;
    }

    public static Bitmap getVideoThumbnail(Context context, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID));

        return MediaStore.Video.Thumbnails.getThumbnail(
                context.getContentResolver(), id,
                MediaStore.Video.Thumbnails.MICRO_KIND,
                (BitmapFactory.Options) null);
    }

    public static void safelyCloseCursor(final Cursor c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

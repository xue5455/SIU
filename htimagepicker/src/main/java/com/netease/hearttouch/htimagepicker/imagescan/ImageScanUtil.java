package com.netease.hearttouch.htimagepicker.imagescan;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.MediaDAO;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class ImageScanUtil {
    private static final String TAG = "ImageScan";

    public interface IImageScanComplete {
        void onScanComplete(ThumbnailsUtil thumbnailsMap, List<AlbumInfo> albumInfoList);
    }

    private static class ImageScanAsyncTask extends AsyncTask<Void, Void, Object> {
        IImageScanComplete mScanComplete = null;
        boolean mCreateAllPhotoAlbum = false;
        private Context mContext;

        public ImageScanAsyncTask(Context context, IImageScanComplete scanComplete, boolean isCreateAllPhotoAlbum) {
            mContext = context;
            mScanComplete = scanComplete;
            mCreateAllPhotoAlbum = isCreateAllPhotoAlbum;
        }

        @Override
        protected Object doInBackground(Void... params) {
            getAllMediaThumbnails(mContext);
            getAllMediaPhotos(mContext);
            if (mCreateAllPhotoAlbum) createAllPhotosAlbum(mContext);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (mScanComplete != null) {
                mScanComplete.onScanComplete(sThumbnailsMap, sAlbumInfoList);
            }
        }
    }

    private static ThumbnailsUtil sThumbnailsMap = ThumbnailsUtil.getInstance();
    private static List<AlbumInfo> sAlbumInfoList = null;

    public static void scanImages(@Nullable IImageScanComplete scanComplete, boolean isCreateAllPhotoAlbum) {
        ImageScanAsyncTask task = new ImageScanAsyncTask(ContextUtil.getInstance().getContext(), scanComplete, isCreateAllPhotoAlbum);
        task.execute();
    }

    public static ThumbnailsUtil getThumbnailsMap() {
        return sThumbnailsMap;
    }

    public static List<AlbumInfo> getAlbumInfoList() {
        return sAlbumInfoList;
    }

    public static Cursor getCameraPhotos(final Context context) {
        final String[] projection = new String[]{MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA};
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(images, projection, query,
                    null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    private static void getAllMediaThumbnails(Context context) {
        sThumbnailsMap.clear();
        Cursor cursorThumb = null;
        try {
            cursorThumb = MediaDAO.getAllMediaThumbnails(context);
            if (cursorThumb != null && cursorThumb.moveToFirst()) {
                int imageID;
                String imagePath;
                do {
                    imageID = cursorThumb.getInt(cursorThumb.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
                    imagePath = cursorThumb.getString(cursorThumb.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                    sThumbnailsMap.put(imageID, C.UrlPrefix.FILE + imagePath);
                } while (cursorThumb.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursorThumb != null) {
                    cursorThumb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void getAllMediaPhotos(Context context) {
        if (sAlbumInfoList == null) {
            sAlbumInfoList = new ArrayList<>();
        } else {
            sAlbumInfoList.clear();
        }

        Cursor cursorPhotos = null;
        try {
            cursorPhotos = MediaDAO.getAllMediaPhotos(context);
            HashMap<String, AlbumInfo> hash = new HashMap<>();
            AlbumInfo albumInfo = null;
            PhotoInfo photoInfo = null;

            if (cursorPhotos != null && cursorPhotos.moveToFirst()) {
                do {
                    int index = 0;
                    int _id = cursorPhotos.getInt(cursorPhotos.getColumnIndex(MediaStore.Images.Media._ID));
                    String path = cursorPhotos.getString(cursorPhotos.getColumnIndex(MediaStore.Images.Media.DATA));
                    String album = cursorPhotos.getString(cursorPhotos.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    long size = cursorPhotos.getLong(cursorPhotos.getColumnIndex(MediaStore.Images.Media.SIZE));

                    File file = FileUtil.tryGetFile(path);
                    if (file == null) {
                        Log.d(TAG, "it is not a vaild path:" + path);
                        continue;
                    }

                    List<PhotoInfo> photoList = new ArrayList<>();
                    photoInfo = new PhotoInfo();
                    if (hash.containsKey(album)) {
                        albumInfo = hash.remove(album);
                        if (sAlbumInfoList.contains(albumInfo))
                            index = sAlbumInfoList.indexOf(albumInfo);
                        photoInfo.setImageId(_id);
                        photoInfo.setFilePath(C.UrlPrefix.FILE + path);
                        photoInfo.setAbsolutePath(path);
                        photoInfo.setSize(size);
                        photoInfo.setLastModifyTime(file.lastModified());
                        addPhoto(albumInfo.getPhotoList(), photoInfo);

                        sAlbumInfoList.set(index, albumInfo);
                        hash.put(album, albumInfo);
                    } else {
                        albumInfo = new AlbumInfo();
                        photoList.clear();
                        photoInfo.setImageId(_id);
                        photoInfo.setFilePath(C.UrlPrefix.FILE + path);
                        photoInfo.setAbsolutePath(path);
                        photoInfo.setSize(size);
                        photoInfo.setLastModifyTime(file.lastModified());
                        photoList.add(photoInfo);

                        albumInfo.setAlbumName(album);
                        albumInfo.setPhotoList(photoList);

                        sAlbumInfoList.add(albumInfo);
                        hash.put(album, albumInfo);
                    }
                } while (cursorPhotos.moveToNext());


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursorPhotos != null) {
                    cursorPhotos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void addPhoto(List<PhotoInfo> photoList, PhotoInfo photoInfo) {
        int pos = 0;
        for (PhotoInfo pi : photoList) {
            if (photoInfo.getLastModifyTime() > pi.getLastModifyTime()) {
                break;
            }
            ++pos;
        }
        photoList.add(pos, photoInfo);
    }

    private static void createAllPhotosAlbum(Context context) {
        if (sAlbumInfoList == null || sAlbumInfoList.size() == 0) return;

        AlbumInfo firstAlbumInfo = sAlbumInfoList.get(0);
        AlbumInfo allPhotoAlbum = new AlbumInfo();
        allPhotoAlbum.setAlbumName(context.getResources().getString(R.string.pick_image_all_photo_album_name));
        allPhotoAlbum.setAbsolutePath(firstAlbumInfo.getAbsolutePath());
        allPhotoAlbum.setFilePath(firstAlbumInfo.getFilePath());
        allPhotoAlbum.setImageId(firstAlbumInfo.getImageId());

        ArrayList<PhotoInfo> allPhotos = new ArrayList<>();
        for (AlbumInfo albumInfo : sAlbumInfoList) {
            List<PhotoInfo> photoInfos = albumInfo.getPhotoList();
            if (photoInfos != null) {
                allPhotos.addAll(photoInfos);
            }
        }

        Collections.sort(allPhotos, new Comparator<PhotoInfo>() {
            @Override
            public int compare(PhotoInfo lhs, PhotoInfo rhs) {
                return lhs.getLastModifyTime() > rhs.getLastModifyTime() ? -1 :
                        (lhs.getLastModifyTime() == rhs.getLastModifyTime() ? 0 : 1);
            }
        });

        allPhotoAlbum.setPhotoList(allPhotos);
        sAlbumInfoList.add(0, allPhotoAlbum);
    }
}

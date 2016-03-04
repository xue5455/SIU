package com.netease.hearttouch.htimagepicker.imagescan;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zyl06 on 2/22/16.
 */
public class PhotoInfoHelper {
    private static HashMap<Integer, PhotoInfo> sId2Photo = new HashMap<>();
    public static PhotoInfo getPhotoInfo(AlbumInfo albumInfo, int imageId) {
        PhotoInfo result = sId2Photo.get(imageId);

        if (result == null) {
            if (albumInfo != null && albumInfo.getPhotoList() != null) {
                List<PhotoInfo> photoInfos = albumInfo.getPhotoList();
                int size = photoInfos.size();
                for (int i=0; i<size; ++i) {
                    PhotoInfo photoInfo = photoInfos.get(i);
                    if (photoInfo.getImageId() == imageId) {
                        sId2Photo.put(imageId, photoInfo);
                        result = photoInfo;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static List<File> getFiles(List<PhotoInfo> photos) {
        List<File> files = new ArrayList<>();

        if (photos != null && !photos.isEmpty()) {
            for (PhotoInfo photoInfo : photos) {
                if (!TextUtils.isEmpty(photoInfo.getAbsolutePath())) {
                    File file = new File(photoInfo.getAbsolutePath());
                    if (file.exists()) {
                        files.add(file);
                    }
                }
            }
        }

        return files;
    }
}

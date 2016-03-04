package com.netease.hearttouch.htimagepicker.imagescan;

import com.netease.hearttouch.htimagepicker.constants.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class AlbumInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int imageID;
    private String pathAbsolute;
    private String pathFile;
    private String nameAlbum;
    private List<PhotoInfo> photoList;

    public int getImageId() {
        if (imageID == 0 && photoList != null) {
            updateUndefinedValues();
        }
        return imageID;
    }

    public void setImageId(int imageID) {
        this.imageID = imageID;
    }

    public String getAbsolutePath() {
        if (pathAbsolute == null) {
            updateUndefinedValues();
        }
        return pathAbsolute;
    }

    public void setAbsolutePath(String pathAbsolute) {
        this.pathAbsolute = pathAbsolute;
    }

    public String getFilePath() {
        if (pathFile == null) {
            updateUndefinedValues();
        }
        return pathFile;
    }

    public void setFilePath(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getAlbumName() {
        return nameAlbum != null ? nameAlbum : "";
    }

    public void setAlbumName(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public List<PhotoInfo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoInfo> list) {
        this.photoList = list;
    }

    private String getCurrentAlbumFilePath(String photoPath) {
        int lastIndex = photoPath.lastIndexOf("/");
        String filePath = photoPath.substring(0, lastIndex);
        return filePath;
    }

    private void updateUndefinedValues() {
        PhotoInfo photoInfo = getLastestModifiedPhoto();
        if (photoInfo != null) {
            imageID = photoInfo.getImageId();
            String albumPath = getCurrentAlbumFilePath(photoInfo.getAbsolutePath());

            this.pathFile = C.UrlPrefix.FILE + albumPath;
            this.pathAbsolute = albumPath;
        }
    }

    public PhotoInfo getLastestModifiedPhoto() {
        if (photoList == null) return null;
        PhotoInfo result = null;
        int photoSize = photoList.size();
        for (int i=0; i<photoSize; ++i) {
            PhotoInfo photoInfo = photoList.get(i);
            if (result == null || result.getLastModifyTime() < photoInfo.getLastModifyTime()) {
                result = photoInfo;
            }
        }
        return result;
    }
}

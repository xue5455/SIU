package com.xue.siu.module.image.imagepick.model;

import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

/**
 * Created by XUE on 2016/2/22.
 */
public class PhotoInfoWrapper {
    private PhotoInfo photoInfo;
    private boolean selected = false;
    private static boolean enabled = true;

    public PhotoInfoWrapper(PhotoInfo photoInfo, boolean selected) {
        this.photoInfo = photoInfo;
        this.selected = selected;
    }


    public PhotoInfoWrapper(PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        PhotoInfoWrapper.enabled = enabled;
    }

}

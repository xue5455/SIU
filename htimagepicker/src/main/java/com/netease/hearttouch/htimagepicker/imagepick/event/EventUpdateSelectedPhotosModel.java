package com.netease.hearttouch.htimagepicker.imagepick.event;

import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.ArrayList;

/**
 * Created by zyl06 on 6/25/15.
 */
public class EventUpdateSelectedPhotosModel {
    private ArrayList<PhotoInfo> mSelectedPhotos;

    public ArrayList<PhotoInfo> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    public void setSelectedPhotos(ArrayList<PhotoInfo> selectedPhotos) {
        this.mSelectedPhotos = selectedPhotos;
    }
}

package com.netease.hearttouch.htimagepicker.imagepreview.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.netease.hearttouch.htimagepicker.imagepick.event.EventNotifier;
import com.netease.hearttouch.htimagepicker.imagepick.event.EventUpdateSelectedPhotosModel;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 2/21/16.
 */
public abstract class HTBaseImagePreviewActivity extends FragmentActivity {
    static final String PHOTO_INFO_LIST_KEY = "HTBaseImagePreviewActivity_PhotoInfoListKey";
    protected ArrayList<PhotoInfo> mPhotoInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processExtras();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ArrayList<PhotoInfo> photoInfos = getSelectedPhotos();
        if (photoInfos != null) {
            EventUpdateSelectedPhotosModel event = new EventUpdateSelectedPhotosModel();
            event.setSelectedPhotos(photoInfos);
            EventNotifier.notifyUpdatePickMarksListener(event);
        }
    }

    public abstract ArrayList<PhotoInfo> getSelectedPhotos();

    protected List<String> getImagePaths() {
        List<String> imagePaths = new ArrayList<String>();
        int size = mPhotoInfos.size();
        for (int i = 0; i < size; ++i) {
            imagePaths.add(mPhotoInfos.get(i).getFilePath());
        }
        return imagePaths;
    }

    private void processExtras() {
        mPhotoInfos = getIntent().getExtras().getParcelableArrayList(PHOTO_INFO_LIST_KEY);
        if (mPhotoInfos == null) {
            mPhotoInfos = new ArrayList<>();
        }
    }
}

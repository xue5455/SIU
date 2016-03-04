package com.netease.hearttouch.htimagepicker;

import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.List;

/**
 * Created by zyl06 on 2/20/16.
 */
public interface HTPickFinishedListener {
    // 返回相册的信息和该相册中照片文件的列表
    void onImagePickFinished(@Nullable AlbumInfo albumInfo, List<PhotoInfo> photos);
    // 图片选择取消
    void onImagePickCanceled();
}

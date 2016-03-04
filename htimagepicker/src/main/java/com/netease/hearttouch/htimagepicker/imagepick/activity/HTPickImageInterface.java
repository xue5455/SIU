package com.netease.hearttouch.htimagepicker.imagepick.activity;

import com.netease.hearttouch.htimagepicker.HTImageFrom;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 2/20/16.
 */
public interface HTPickImageInterface {
    /**
     * 当图片选择页面返回的时候会调用该方法，true表示确认选择，可能会触发回调 onImagePickFinished
     * 当返回false，表示取消选择，则一定会触发回调 onImagePickCanceled
     * @return 是否确认选择
     */
    boolean isConfirmUse();

    /**
     * 在图片选择控件选择结束的时候，会调用该方法，需要将选择的图片列表返回
     * @return 选中的图片列表
     */
    List<PhotoInfo> getSelectedPhotos();

    /**
     * 从拍照界面拍好照，返回图片选择页面时触发的回调
     * @param photoInfo 拍照保存的图片信息
     */
    void onPickedFromCamera(PhotoInfo photoInfo);
    /**
     * 从拍照界面取消拍照或者取消保存，返回图片选择页面时触发的回调
     * @param
     */
    void onCancelFromCamera();

    /**
     * 从裁剪界面裁剪完毕并确认，返回图片选择页面时触发的回调
     * @param from 是对本地图片的裁剪还是直接在拍照界面触发的裁剪
     */
    void onCompleteFromCrop(HTImageFrom from);
    /**
     * 从裁剪界面取消裁剪，返回图片选择页面时触发的回调
     * @param from 是对本地图片的裁剪还是直接在拍照界面触发的裁剪
     */
    void onCancelFromCrop(HTImageFrom from);

    /**
     * 从图片预览控件等其他页面触发图片选择页面 更新选中的照片
     * 事件触发已经在主线程中
     * @param photoInfos 新的选中的图片列表
     */
    void onUpdateSelectedPhotoInfos(ArrayList<PhotoInfo> photoInfos);
}

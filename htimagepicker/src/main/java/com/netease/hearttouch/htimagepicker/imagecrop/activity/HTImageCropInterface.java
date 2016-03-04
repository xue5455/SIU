package com.netease.hearttouch.htimagepicker.imagecrop.activity;

import com.netease.hearttouch.htimagepicker.imagecrop.view.ImageCropView;

/**
 * Created by zyl06 on 2/23/16.
 */
public interface HTImageCropInterface {
    /**
     * 子类调用，触发取消裁剪并结束activity
     */
    void confirmCropFinish();
    /**
     * 子类调用，触发确认裁剪并结束activity
     */
    void cancelCropFinish();

    /**
     * 获取页面中的 ImageCropView 控件
     * @return xml中定义或者java中新建的图片裁剪控件
     */
    ImageCropView getImageCropView();
}

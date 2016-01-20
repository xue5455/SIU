package com.xue.siu.module.image.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;

/**
 * Created by XUE on 2016/1/19.
 */
public class ImageViewHolderItem implements TAdapterItem<ImageVO> {
    private ImageVO imageVO;

    public ImageViewHolderItem(ImageVO imageVO){
        this.imageVO = imageVO;
    }
    @Override
    public int getViewType() {
        return ItemType.ITEM_COMMON;
    }

    @Override
    public int getId() {
        return imageVO.hashCode();
    }

    @Override
    public ImageVO getDataModel() {
        return imageVO;
    }
}

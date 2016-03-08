package com.xue.siu.module.image.imagepick.viewholder.item;


import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.module.image.imagepick.model.PhotoInfoWrapper;

/**
 * Created by XUE on 2016/2/22.
 */
public class ImageViewHolderItem implements TAdapterItem<PhotoInfoWrapper> {
    private PhotoInfoWrapper photoInfoWrapper;

    public ImageViewHolderItem(PhotoInfoWrapper photoInfoWrapper) {
        this.photoInfoWrapper = photoInfoWrapper;
    }

    @Override
    public int getViewType() {
        return ImageItemType.ITEM_TYPE_COMMON;
    }

    @Override
    public int getId() {
        return photoInfoWrapper.hashCode();
    }

    @Override
    public PhotoInfoWrapper getDataModel() {
        return photoInfoWrapper;
    }
}

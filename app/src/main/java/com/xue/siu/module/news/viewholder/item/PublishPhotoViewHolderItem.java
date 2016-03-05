package com.xue.siu.module.news.viewholder.item;

import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htrecycleview.TAdapterItem;

/**
 * Created by XUE on 2016/3/5.
 */
public class PublishPhotoViewHolderItem implements TAdapterItem<PhotoInfo>{
    private PhotoInfo mInfo;

    public PublishPhotoViewHolderItem(PhotoInfo info) {
        this.mInfo = info;
    }

    @Override
    public int getViewType() {
        return NewsItemType.ITEM_PUBLISH_PHOTO;
    }

    @Override
    public int getId() {
        return mInfo.hashCode();
    }

    @Override
    public PhotoInfo getDataModel() {
        return mInfo;
    }
}

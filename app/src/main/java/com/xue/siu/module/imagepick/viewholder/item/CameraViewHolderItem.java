package com.xue.siu.module.imagepick.viewholder.item;


import com.netease.hearttouch.htrecycleview.TAdapterItem;

/**
 * Created by XUE on 2016/2/24.
 */
public class CameraViewHolderItem implements TAdapterItem {
    @Override
    public int getViewType() {
        return ImageItemType.ITEM_TYPE_CAMERA;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Object getDataModel() {
        return null;
    }
}

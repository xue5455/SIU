package com.xue.siu.module.chat.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.common.util.EmojiUtil.FaceWrapper;
/**
 * Created by XUE on 2016/1/29.
 */
public class FaceViewHolderItem implements TAdapterItem<FaceWrapper> {
    private FaceWrapper faceWrapper;

    public FaceViewHolderItem(FaceWrapper faceWrapper) {
        this.faceWrapper = faceWrapper;
    }

    @Override
    public int getViewType() {
        return ItemType.TYPE_FACE;
    }

    @Override
    public int getId() {
        return faceWrapper.hashCode();
    }

    @Override
    public FaceWrapper getDataModel() {
        return faceWrapper;
    }
}

package com.xue.siu.module.news.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.module.news.model.PublishEditModel;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishEditViewHolderItem implements TAdapterItem<PublishEditModel>{
    private PublishEditModel model;

    public PublishEditViewHolderItem(PublishEditModel model) {
        this.model = model;
    }

    @Override
    public int getViewType() {
        return NewsItemType.ITEM_PUBLISH_EDIT;
    }

    @Override
    public int getId() {
        return model.hashCode();
    }

    @Override
    public PublishEditModel getDataModel() {
        return model;
    }
}

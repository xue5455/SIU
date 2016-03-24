package com.xue.siu.module.news.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;

/**
 * Created by XUE on 2016/3/8.
 */
public class NewsDecorationViewHolderItem implements TAdapterItem {
    @Override
    public int getViewType() {
        return NewsItemType.ITEM_COMMON_DECORATION;
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

package com.xue.siu.module.query.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;

/**
 * Created by XUE on 2016/1/22.
 */
public class QueryKeyViewHolderItem implements TAdapterItem<String> {
    String mData;

    public QueryKeyViewHolderItem(String data) {
        mData = data;
    }

    @Override
    public int getViewType() {
        return QueryKeyItemType.COMMON_ITEM;
    }

    @Override
    public int getId() {
        return mData.hashCode();
    }

    @Override
    public String getDataModel() {
        return mData;
    }
}

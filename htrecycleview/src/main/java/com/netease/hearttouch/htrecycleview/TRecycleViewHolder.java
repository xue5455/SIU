package com.netease.hearttouch.htrecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netease.hearttouch.htrecycleview.base.TBaseRecycleViewHolder;


/**
 * recycle需要的viewholder
 * Created by zhengwen on 15-6-16.
 */
@TRecycleViewHolderAnnotation()
public abstract class TRecycleViewHolder<TDataModel>
        extends TBaseRecycleViewHolder<View, TDataModel> {

    public TRecycleViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
        inflate();
    }
}

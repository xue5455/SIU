package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.xue.siu.R;

/**
 * Created by XUE on 2016/3/8.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_decoration_news_fragment)
public class NewsDecorationViewHolder extends TRecycleViewHolder {
    public NewsDecorationViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {

    }

    @Override
    public void refresh(TAdapterItem item) {

    }
}

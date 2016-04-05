package com.xue.siu.module.calendar.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.xue.siu.R;

/**
 * Created by XUE on 2016/3/27.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_divider_schedule_list)
public class DividerViewHolder extends TRecycleViewHolder {
    public DividerViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {

    }

    @Override
    public void refresh(TAdapterItem item) {

    }
}

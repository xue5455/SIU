package com.xue.siu.module.query.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;

/**
 * Created by XUE on 2016/1/22.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_query_user)
public class QueryKeyViewHolder extends TRecycleViewHolder<String> implements View.OnClickListener {
    private TextView mTvKey;
    private View mViewContainer;

    public QueryKeyViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
        }
    }

    @Override
    public void inflate() {
        mTvKey = findViewById(R.id.tv_keyword);
        mViewContainer = findViewById(R.id.ll_container);
        mViewContainer.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<String> item) {
        mTvKey.setText(item.getDataModel());
    }
}

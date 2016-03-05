package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;

/**
 * Created by XUE on 2016/3/5.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_photo_add)
public class PublishAddViewHolder extends TRecycleViewHolder implements View.OnClickListener{
    private View mBtnAdd;
    public PublishAddViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mBtnAdd = findViewById(R.id.btn_photo_publish);
        mBtnAdd.setOnClickListener(this);
        PublishPhotoViewHolder.adaptSize(mBtnAdd);
    }

    @Override
    public void refresh(TAdapterItem item) {

    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onEventNotify(ItemEventListener.clickEventName,v,getAdapterPosition());
        }
    }
}

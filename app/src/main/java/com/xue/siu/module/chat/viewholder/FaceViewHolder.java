package com.xue.siu.module.chat.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.xue.siu.R;
import com.xue.siu.common.util.EmojiUtil;

/**
 * Created by XUE on 2016/1/29.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_face_rv)
public class FaceViewHolder extends TRecycleViewHolder<EmojiUtil.FaceWrapper> {
    SimpleDraweeView mSdvFace;

    public FaceViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvFace = findViewById(R.id.sdv_face);
    }

    @Override
    public void refresh(TAdapterItem<EmojiUtil.FaceWrapper> item) {
        mSdvFace.setImageResource(item.getDataModel().getId());
    }
}

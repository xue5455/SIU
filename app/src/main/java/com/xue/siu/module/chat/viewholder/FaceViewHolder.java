package com.xue.siu.module.chat.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.EmojiUtil;
import com.xue.siu.common.util.EmojiUtil.FaceWrapper;

/**
 * Created by XUE on 2016/1/29.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_face_rv)
public class FaceViewHolder extends TRecycleViewHolder<FaceWrapper> implements View.OnClickListener {
    private SimpleDraweeView mSdvFace;
    private int mHeight;
    private FaceWrapper mWrapper;

    public FaceViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }


    @Override
    public void inflate() {
        mSdvFace = findViewById(R.id.sdv_face);
        mSdvFace.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<FaceWrapper> item) {
        mWrapper = item.getDataModel();
        mSdvFace.setImageResource(item.getDataModel().getId());
        if (mHeight != 0) {
            ViewGroup.LayoutParams params = mSdvFace.getLayoutParams();
            params.height = mHeight;
            mSdvFace.invalidate();
        }
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition(), mWrapper);
        }
    }
}

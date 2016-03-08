package com.xue.siu.module.image.imagepick.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;

/**
 * Created by XUE on 2016/2/24.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_take_photo_pick_image)
public class CameraViewHolder extends TRecycleViewHolder implements View.OnClickListener {
    private View content;
    public static int ivSize = (ScreenUtil.getDisplayWidth() -
            3 * ResourcesUtil.getDimenPxSize(R.dimen.pia_gv_horizontal_space)) / 4;

    public CameraViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        content = view;
        content.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem item) {
        ViewGroup.LayoutParams params = content.getLayoutParams();
        params.height = ivSize;
        content.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
    }
}

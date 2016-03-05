package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.util.FrescoUtil;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;

/**
 * Created by XUE on 2016/3/5.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_photo_publish)
public class PublishPhotoViewHolder extends TRecycleViewHolder<PhotoInfo> implements View.OnClickListener {
    private SimpleDraweeView mSdvPic;
    public static final int VERTICAL_SPACING = ResourcesUtil.getDimenPxSize(R.dimen.pa_rv_pic_vertical_spacing);

    public static final int VIEW_SIZE = (ScreenUtil.getDisplayWidth() -
            5 * ResourcesUtil.getDimenPxSize(R.dimen.pa_rv_pic_horizontal_spacing)) / 4;
    public static final float RADIUS = ResourcesUtil.getDimenPxSize(R.dimen.radius_2dp);

    public PublishPhotoViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvPic = findViewById(R.id.sdv_photo_publish);
        mSdvPic.setOnClickListener(this);
        adaptSize(mSdvPic);
    }

    @Override
    public void refresh(TAdapterItem<PhotoInfo> item) {
        FrescoUtil.setImageUrl(mSdvPic, item.getDataModel().getFilePath(), VIEW_SIZE, VIEW_SIZE);
        mSdvPic.getHierarchy().setRoundingParams(RoundingParams.fromCornersRadius(RADIUS));
        adaptPadding(mSdvPic, getAdapterPosition());
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
    }

    public static void adaptPadding(View view, int position) {

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int t;
        if (position < 4) {
            t = 0;
        } else {
            t = VERTICAL_SPACING;
        }
        params.topMargin = t;
        params.bottomMargin = 0;
        view.setLayoutParams(params);
    }

    public static void adaptSize(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = params.height = VIEW_SIZE;
        view.setLayoutParams(params);
    }
}

package com.xue.siu.module.image.imagepick.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.util.image.ImageUtil;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.module.image.imagepick.model.PhotoInfoWrapper;


/**
 * Created by XUE on 2016/2/22.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_pick_image_list)
public class ImageViewHolder extends TRecycleViewHolder<PhotoInfoWrapper> implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final int TAG_CHECKBOX = 1;
    public static final int TAG_CHECKBOX_CONTAINER = 2;
    private SimpleDraweeView sdvPic;
    private CheckBox checkBox;
    private View viewOverlay;
    private PhotoInfoWrapper model;
    private View viewCbContainer;

    public ImageViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        sdvPic = (SimpleDraweeView) view.findViewById(R.id.sdv_pic_pick_image);
        checkBox = (CheckBox) view.findViewById(R.id.cb_pick_image);
        viewOverlay = view.findViewById(R.id.view_overlay_pick_image);
        checkBox.setClickable(false);
        checkBox.setOnCheckedChangeListener(this);
        viewCbContainer = view.findViewById(R.id.rl_container_pick_image);
        viewCbContainer.setOnClickListener(this);
        checkBox.setTag(TAG_CHECKBOX);
        viewCbContainer.setTag(TAG_CHECKBOX_CONTAINER);
        sdvPic.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<PhotoInfoWrapper> item) {
        int size = CameraViewHolder.ivSize;
        ViewGroup.LayoutParams params = sdvPic.getLayoutParams();
        params.height = size;
        sdvPic.setLayoutParams(params);
        viewOverlay.setLayoutParams(params);
        model = item.getDataModel();
        PhotoInfo info = model.getPhotoInfo();
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setChecked(model.isSelected());
        checkBox.setEnabled(PhotoInfoWrapper.isEnabled() || model.isSelected());
        viewCbContainer.setEnabled(PhotoInfoWrapper.isEnabled() || model.isSelected());
        String thumbUrl = ThumbnailsUtil.getThumbnailWithImageID(info.getImageId(), info.getFilePath());
        String oldUrl = (String) sdvPic.getTag();
        if (oldUrl == null || !thumbUrl.equals(oldUrl)) {
            sdvPic.setTag(thumbUrl);
            String filePath = Uri.parse(thumbUrl).getPath();
            ImageUtil.setImagePath(sdvPic, filePath, 100, 100, true);
//            FrescoUtil.setImageUri(sdvPic, thumbUrl, 100, 100);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_container_pick_image:
                onCheckBoxClick(v);
                break;
            case R.id.sdv_pic_pick_image:
                onImageClick(v);
                break;
        }
    }

    private void onImageClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition(), model.getPhotoInfo());
        }
    }

    private void onCheckBoxClick(View v) {
//        if (PhotoInfoWrapper.isEnabled() || model.isSelected()) {
            checkBox.setChecked(!checkBox.isChecked());
            if (listener != null)
                listener.onEventNotify(ItemEventListener.clickEventName, v,
                        getAdapterPosition(), checkBox.isChecked());
//        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        viewOverlay.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

}

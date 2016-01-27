package com.xue.siu.module.image.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.module.image.viewholder.item.ImageVO;

import java.io.File;

/**
 * Created by XUE on 2016/1/19.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_image_list)
public class ImageViewHolder extends TRecycleViewHolder<ImageVO> implements View.OnClickListener{
    private SimpleDraweeView mSdvImage;

    public ImageViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvImage = findViewById(R.id.sdv_image);
    }

    @Override
    public void refresh(TAdapterItem<ImageVO> item) {
        ImageVO imageVO = item.getDataModel();
        int height = imageVO.getHeight();
        String path = imageVO.getPath();
        LayoutParams params = (LayoutParams) mSdvImage.getLayoutParams();
        params.height = height;
        mSdvImage.setLayoutParams(params);

        Uri uri = Uri.fromFile(new File(path));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(imageVO.getWidth(), height))
                .setAutoRotateEnabled(true)
                .build();
        if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE || Fresco.getImagePipeline().isInBitmapMemoryCache(request)) {
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(mSdvImage.getController())
                    .setImageRequest(request)
                    .build();
            mSdvImage.setController(controller);
            mSdvImage.setOnClickListener(this);
        } else {
            mSdvImage.setImageURI(Uri.EMPTY);
            mSdvImage.setOnClickListener(null);
        }


    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onEventNotify(ItemEventListener.clickEventName,v,getAdapterPosition());
        }
    }
}

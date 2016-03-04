package com.netease.hearttouch.htimagepicker.imagepreview.activity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.view.NavigationBar;
import com.netease.hearttouch.htimagepicker.view.ViewWithNavationBar;
import com.netease.photoview.PhotoView;

import java.util.ArrayList;

/**
 * Created by zyl06 on 2/22/16.
 */
public class HTSingleImagePreviewActivity extends HTBaseImagePreviewActivity {
    static final int IMG_WIDTH = ContextUtil.getInstance().getScreenWidth();
    static final String GIF = "gif";

    FrameLayout mNavigationBarContainer;
    FrameLayout mContentView;
    NavigationBar mNavigationBar;
    String mImagePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 确保有一张图片
        mImagePath = mPhotoInfos.get(0).getFilePath();

        ViewWithNavationBar view = new ViewWithNavationBar(this);
        setContentView(view);
        mNavigationBarContainer = view.getNavigationBarContainer();
        mNavigationBar = view.getNavigationBar();
        mContentView = view.getContentView();

        initContentView(mContentView);
    }

    private void initContentView(FrameLayout contentView) {
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_single_image_preview, contentView, false);
        final SimpleDraweeView gifView = (SimpleDraweeView)view.findViewById(R.id.preview_image_view_gif);
        final PhotoView imageView = (PhotoView)view.findViewById(R.id.preview_image_view);

        if (mImagePath != null) {
            if (mImagePath.contains(GIF.toLowerCase())) {
                gifView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(
                            String id,
                            @Nullable ImageInfo imageInfo,
                            @Nullable Animatable anim) {
                        if (imageInfo == null) {
                            return;
                        }

                        float ratio = (float) imageInfo.getWidth() / (float) imageInfo.getHeight();

                        int width = IMG_WIDTH;
                        int height = (int) (width / ratio);
                        ViewGroup.LayoutParams lp = gifView.getLayoutParams();
                        lp.width = width;
                        lp.height = height;
                        gifView.setLayoutParams(lp);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                    }
                };
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setControllerListener(controllerListener)
                        .setAutoPlayAnimations(true)
                        .setUri(Uri.parse(mImagePath))
                        .build();

                gifView.setController(controller);
            }
            else {
                gifView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                imageView.setImageUrl(mImagePath, C.DEFAULT_PREVIEW_IMAGE_SIZE, C.DEFAULT_PREVIEW_IMAGE_SIZE);
            }
        }
        mContentView.addView(view);

        mNavigationBarContainer.setBackgroundResource(R.color.transparent);
        mNavigationBar.setBackgroundResource(R.color.transparent);
        mNavigationBar.setBackButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTSingleImagePreviewActivity.this.finish();
            }
        });
        mNavigationBar.setLeftBackImage(R.drawable.ic_back_arrow);
    }

    @Override
    public ArrayList<PhotoInfo> getSelectedPhotos() {
        return null;
    }
}

package com.xue.siu.module.image.imagepreview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.photoview.ImageDownloadListener;
import com.netease.photoview.PhotoView;
import com.xue.siu.R;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.module.image.imagepreview.ConstantsIP;
import com.xue.siu.module.image.imagepreview.presenter.SingleItemImagePreviewPresenter;
import java.util.ArrayList;


/**
 * Created by yzh on 2016/2/23.
 */
public class SingleItemImagePreviewActivity extends HTBaseImagePreviewActivity {
    private static final int IMG_WIDTH = ContextUtil.getInstance().getScreenWidth();
    private static final String GIF = "gif";

    private SingleItemImagePreviewPresenter mPresenter;

    private View mRoot;

    private SimpleDraweeView mGifView;

    private PhotoView mImageView;

    private ImageButton mBtnDelete;

    private String mImagePath;

    private boolean mShowDelete;

    private int mPosition;

    public static void startForResult(@NonNull Activity activity, boolean showDelete,
                                      final String photoFilePath, int position) {
        //确保传入的图片数据是有内容的
        if (TextUtils.isEmpty(photoFilePath)) {
            return;
        }

        Intent intent = new Intent(activity, SingleItemImagePreviewActivity.class);
        intent.putExtra(ConstantsIP.KEY_PHOTO_FILE_PATH, photoFilePath);
        intent.putExtra(ConstantsIP.KEY_SHOW_DELETE, showDelete);
        intent.putExtra(ConstantsIP.KEY_POSITION, position);
        activity.startActivityForResult(intent, ConstantsIP.REQUEST_CODE_SINGLE_ITEM_IMAGE_PREVIEW);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = LayoutInflater.from(this).inflate(R.layout.activity_single_item_image_preview, null);
        setContentView(mRoot);
        mPresenter = new SingleItemImagePreviewPresenter(this);
        initData();
        initContentView();
    }

    private void initData() {
        mImagePath = getIntent().getStringExtra(ConstantsIP.KEY_PHOTO_FILE_PATH);
        mShowDelete = getIntent().getBooleanExtra(ConstantsIP.KEY_SHOW_DELETE, false);
        mPosition = getIntent().getIntExtra(ConstantsIP.KEY_POSITION, -1);
    }

    private void initContentView() {
        mGifView = (SimpleDraweeView) findViewById(R.id.preview_image_view_gif);
        mImageView = (PhotoView) findViewById(R.id.preview_image_view);
        mBtnDelete = (ImageButton) findViewById(R.id.preview_delete);

        if (mImagePath != null) {
            if (mImagePath.contains(GIF.toLowerCase())) {
                mGifView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                showGif();
            } else {
                mGifView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                DialogUtil.showProgressDialog(this, false);
                mImageView.setImageUrl(mImagePath, C.DEFAULT_PREVIEW_IMAGE_SIZE, C.DEFAULT_PREVIEW_IMAGE_SIZE);
                mImageView.setImageDownloadListener(new ImageDownloadListener() {
                    @Override
                    public void onUpdate(int i) {
                        if (i == 100) {
                            DialogUtil.hideProgressDialog(SingleItemImagePreviewActivity.this);
                        }
                    }
                });
            }
        }

        mBtnDelete.setVisibility(mShowDelete ? View.VISIBLE : View.GONE);
        mGifView.setOnClickListener(mPresenter);
        mImageView.setOnViewTapListener(mPresenter);
        mBtnDelete.setOnClickListener(mPresenter);
        mRoot.setOnClickListener(mPresenter);
    }

    private void showGif() {
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
                ViewGroup.LayoutParams lp = mGifView.getLayoutParams();
                lp.width = width;
                lp.height = height;
                mGifView.setLayoutParams(lp);
                DialogUtil.hideProgressDialog(SingleItemImagePreviewActivity.this);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                DialogUtil.hideProgressDialog(SingleItemImagePreviewActivity.this);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setAutoPlayAnimations(true)
                .setUri(Uri.parse(mImagePath))
                .build();

        mGifView.setController(controller);
        DialogUtil.showProgressDialog(this, false);
    }

    public String getImagePath() {
        return mImagePath;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public ArrayList<PhotoInfo> getSelectedPhotos() {
        return null;
    }
}
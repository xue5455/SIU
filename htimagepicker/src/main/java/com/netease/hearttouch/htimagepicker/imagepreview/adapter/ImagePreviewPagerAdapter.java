package com.netease.hearttouch.htimagepicker.imagepreview.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.view.ViewPagerAdapter;
import com.netease.photoview.PhotoView;
import com.netease.photoview.PhotoViewAttacher;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zyl06 on 6/25/15.
 */
public class ImagePreviewPagerAdapter extends ViewPagerAdapter
        implements View.OnClickListener, PhotoViewAttacher.OnViewTapListener{
    static final int IMG_WIDTH = ContextUtil.getInstance().getContext().getResources().getDisplayMetrics().widthPixels;

    private Context mContext;
    private LinkedList<String> pathList;

    public ImagePreviewPagerAdapter(Context context, List<String> imagePaths) {
        mContext = context;
        pathList = new LinkedList();
        for (String path: imagePaths) {
            pathList.add(path);
        }
    }

    @Override
    protected View getView(View convertView, int position) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_img_fullscreen, null);

            viewHolder = new ViewHolder();
            viewHolder.gifDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.img_gif_fullscreen);
            viewHolder.gifDraweeView.setOnClickListener(this);

            viewHolder.photoDraweeView = (PhotoView) convertView.findViewById(R.id.img_fullscreen);
            viewHolder.photoDraweeView.setOnViewTapListener(this);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.photoDraweeView.setImageUrl(pathList.get(position), 500, 500);

        String path = pathList.get(position);
        if (path.toLowerCase().contains("gif")) {
            viewHolder.gifDraweeView.setVisibility(View.VISIBLE);
            viewHolder.photoDraweeView.setVisibility(View.GONE);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(viewHolder.mControllerListener)
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse(path))
                    .build();
            viewHolder.gifDraweeView.setController(controller);
        }
        else {
            viewHolder.gifDraweeView.setVisibility(View.GONE);
            viewHolder.photoDraweeView.setVisibility(View.VISIBLE);

            viewHolder.photoDraweeView.setImageUrl(path, 500, 500);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public void onClick(View v) {
        back();
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        back();
    }

    private void back() {
        Activity activity = (Activity) mContext;
        activity.finish();
        activity.overridePendingTransition(0,
                R.anim.anim_img_fullscreen_out);
    }

    private static class ViewHolder {
        SimpleDraweeView gifDraweeView;
        PhotoView photoDraweeView;

        ControllerListener mControllerListener = new BaseControllerListener<ImageInfo>() {
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
                ViewGroup.LayoutParams lp = gifDraweeView.getLayoutParams();
                lp.width = width;
                lp.height = height;
                gifDraweeView.setLayoutParams(lp);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
            }
        };
    }
}

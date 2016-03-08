package com.xue.siu.module.image.imagepreview.adapter;

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
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.view.ViewPagerAdapter;
import com.netease.photoview.ImageDownloadListener;
import com.netease.photoview.PhotoView;
import com.netease.photoview.PhotoViewAttacher;
import com.xue.siu.R;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.constant.C;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yzh on 2016/2/26.
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
    public Object instantiateItem(ViewGroup container, int position) {
        View view= (View) super.instantiateItem(container, position);
        view.findViewById(R.id.img_gif_fullscreen)
                .setOnClickListener(this);
        ((PhotoView) view.findViewById(R.id.img_fullscreen)).setOnViewTapListener(this);

        return view;
    }

    @Override
    protected View getView(View convertView, int position) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_img_fullscreen, null);

            viewHolder = new ViewHolder();
            viewHolder.gifDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.img_gif_fullscreen);
            viewHolder.photoDraweeView = (PhotoView) convertView.findViewById(R.id.img_fullscreen);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String path = pathList.get(position);
        if (path.toLowerCase().contains(C.FileSuffix.GIF)) {
            viewHolder.gifDraweeView.setVisibility(View.VISIBLE);
            viewHolder.photoDraweeView.setVisibility(View.GONE);
            //只有这样才能真正隐藏PhotoView
            viewHolder.photoDraweeView.setImageUrl("", 500, 500);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(viewHolder.mControllerListener)
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse(path))
                    .build();
            viewHolder.gifDraweeView.setController(controller);
        } else {
            viewHolder.gifDraweeView.setVisibility(View.GONE);
            viewHolder.photoDraweeView.setVisibility(View.VISIBLE);
            viewHolder.photoDraweeView.setImageUrl(path, 500, 500);
            viewHolder.photoDraweeView.setImageDownloadListener(new ImageDownloadListener() {
                @Override
                public void onUpdate(int i) {
                    if (i == 100) {
                        DialogUtil.hideProgressDialog((Activity) mContext);
                    }
                }
            });
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
    }

    private class ViewHolder {
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

                DialogUtil.hideProgressDialog((Activity) mContext);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                DialogUtil.hideProgressDialog((Activity) mContext);
            }
        };
    }
}

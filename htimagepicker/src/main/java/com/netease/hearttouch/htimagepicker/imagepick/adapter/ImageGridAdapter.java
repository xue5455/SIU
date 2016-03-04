package com.netease.hearttouch.htimagepicker.imagepick.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhotoInfo> mAllPhotos = new ArrayList<>();
    private List<PhotoInfo> mSelectedPhotos;
    private LayoutInflater mInflater;

    public ImageGridAdapter(Context context, AlbumInfo album,
                            @Nullable List<PhotoInfo> selectedPhotos) {
        mContext = context;

        initAllPhotoInfos(album);
        mSelectedPhotos = selectedPhotos != null ? selectedPhotos : new ArrayList<PhotoInfo>();
        mInflater = LayoutInflater.from(mContext);
    }

    public void changeAlbum(AlbumInfo album, @Nullable List<PhotoInfo> selectedPhotos) {
        initAllPhotoInfos(album);

        mSelectedPhotos = selectedPhotos != null ? selectedPhotos : new ArrayList<PhotoInfo>();
        notifyDataSetChanged();
    }

    private void initAllPhotoInfos(AlbumInfo album) {
        if (album != null && album.getPhotoList() != null) {
            mAllPhotos = album.getPhotoList();
        }
    }

    @Override
    public int getCount() {
        // 0 是拍照
        return mAllPhotos.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        // 0 是拍照
        return mAllPhotos.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (position == 0)
                convertView = mInflater.inflate(R.layout.item_pick_image_grid_view_camera, null);
            else {
                convertView = mInflater.inflate(R.layout.item_pick_image_grid_image_view, null);
                TextView orderView = ViewHolder.get(convertView, R.id.order_number);
            }
            int hMargin = (int) ContextUtil.getInstance().getDimen(R.dimen.pick_image_horizontal_margin);
            int hSpace = (int) ContextUtil.getInstance().getDimen(R.dimen.pick_image_grid_internal_space);
            int height = (ContextUtil.getInstance().getScreenWidth() - 2 * hMargin - 2 * hSpace) / 3;
            GridView.LayoutParams lp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, height);
            convertView.setLayoutParams(lp);
        }

        if (position > 0) {
            PhotoInfo photoInfo = mAllPhotos.get(position - 1);

            SimpleDraweeView photoThumbnail = ViewHolder.get(convertView, R.id.pick_image_photo);
            String thumbUrl = ThumbnailsUtil.getThumbnailWithImageID(photoInfo.getImageId(), photoInfo.getFilePath());

//            ImageUtil.setImageWithoutRotate(photoThumbnail, Uri.parse(thumbUrl));
//            ImageLoaderUtil.setImageUri(photoThumbnail, Uri.parse(thumbUrl), null);
//            photoThumbnail.setImageURI(Uri.parse(thumbUrl));

            String oldThumbUrl = (String) photoThumbnail.getTag();
            if (oldThumbUrl == null || !oldThumbUrl.equals(thumbUrl)) {
                photoThumbnail.setTag(thumbUrl);
//                ImageUtil.setImageUri(photoThumbnail, thumbUrl, 100, 100);
                Uri uri = Uri.parse(thumbUrl);
                ImageUtil.setImagePath(photoThumbnail, uri.getPath(), 100, 100, true);
            }

            ImageView pickMarkRect = ViewHolder.get(convertView, R.id.pick_image_mark_rect);
            ImageView pickMarkCorner = ViewHolder.get(convertView, R.id.pick_image_mark_corner);
            int pickOrder = mSelectedPhotos.indexOf(photoInfo);
            if (pickOrder >= 0) {
                pickMarkRect.setVisibility(View.VISIBLE);
                pickMarkCorner.setVisibility(View.VISIBLE);
            } else {
                pickMarkRect.setVisibility(View.GONE);
                pickMarkCorner.setVisibility(View.GONE);
            }

            TextView orderView = ViewHolder.get(convertView, R.id.order_number);
            String text = pickOrder >= 0 ? "" + (pickOrder + 1) : "";
            orderView.setText(text);
        }

        return convertView;
    }
}

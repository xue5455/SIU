package com.netease.hearttouch.htimagepicker.imagepick.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ImageScanUtil;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.util.ViewHolder;

import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class AlbumListAdapter extends BaseAdapter {
    //private ThumbnailsMap mThumbnailsMap;
    private List<AlbumInfo> mAlbumInfoList;
    private Context mContext;

    public AlbumListAdapter(Context context) {
        //mThumbnailsMap = ImageScanUtil.getThumbnailsMap();
        mAlbumInfoList = ImageScanUtil.getAlbumInfoList();
        mContext = context;
    }

    @Override
    public int getCount() {
        int count = 0;
        //if (mThumbnailsMap != null) {
        //    count += 1;
        //}
        if (mAlbumInfoList != null) {
            count += mAlbumInfoList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mAlbumInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pick_image_list_album, null);
        }

        AlbumInfo album = mAlbumInfoList.get(position);

        SimpleDraweeView albumThumbnail = ViewHolder.get(convertView, R.id.album_first_thumbnail);
        albumThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        PhotoInfo photoInfo = album.getLastestModifiedPhoto();
        String defaultUrl = album.getFilePath();
        if (photoInfo != null) {
            defaultUrl = photoInfo.getFilePath();
        }
        String thumbUrl = ThumbnailsUtil.getThumbnailWithImageID(album.getImageId(), defaultUrl);
//        albumThumbnail.setImageURI(Uri.parse(thumbUrl));
//        ImageUtil.setImageUri(albumThumbnail, thumbUrl, 80, 80);
        if (thumbUrl != null) {
            Uri uri = Uri.parse(thumbUrl);
            ImageUtil.setImagePath(albumThumbnail, uri.getPath(), 80, 80, true);
        }

        TextView albumName = ViewHolder.get(convertView, R.id.album_name);
        albumName.setText(album.getAlbumName());

        TextView albumImageCount = ViewHolder.get(convertView, R.id.album_images_count);
        String countUnit = ContextUtil.getInstance().getString(R.string.pick_image_count_unit);
        if (album.getPhotoList() != null) {
            albumImageCount.setText(album.getPhotoList().size() + countUnit);
        } else {
            albumImageCount.setText("0 " + countUnit);
        }

        return convertView;
    }
}

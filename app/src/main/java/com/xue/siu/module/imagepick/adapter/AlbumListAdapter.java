package com.xue.siu.module.imagepick.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.util.FrescoUtil;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;

import java.util.List;

/**
 * Created by XUE on 2016/2/23.
 */
public class AlbumListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<AlbumInfo> list;
    private int size = ResourcesUtil.getDimenPxSize(R.dimen.pia_album_iv_size);
    private float radius = ResourcesUtil.getDimenPxSize(R.dimen.radius_2dp);
    public AlbumListAdapter(Context context, List<AlbumInfo> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AlbumInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_album_pick_image_list, parent, false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        PhotoInfo info = list.get(position).getPhotoList().get(0);
        int count = list.get(position).getPhotoList().size();
        String title = list.get(position).getAlbumName();
        holder.tvTitle.setText(title);
        holder.tvCount.setText(String.valueOf(count));
        String thumbUrl = ThumbnailsUtil.getThumbnailWithImageID(info.getImageId(), info.getFilePath());
        String oldUrl = (String) holder.sdvPic.getTag();
        if (oldUrl == null || !thumbUrl.equals(oldUrl)) {
            holder.sdvPic.setTag(thumbUrl);
            FrescoUtil.setImageUri(holder.sdvPic, Uri.parse(thumbUrl), size, size);
        }
        holder.viewDivider.setVisibility(position != list.size() - 1 ? View.VISIBLE : View.GONE);
        return convertView;
    }

    static class MyViewHolder {
        SimpleDraweeView sdvPic;
        TextView tvTitle;
        TextView tvCount;
        View viewDivider;

        public MyViewHolder(View view) {
            sdvPic = (SimpleDraweeView) view.findViewById(R.id.sdv_album_pick_image);
            tvTitle = (TextView) view.findViewById(R.id.tv_title_album_pick_image);
            tvCount = (TextView) view.findViewById(R.id.tv_album_pic_count_pick_image);
            viewDivider = view.findViewById(R.id.view_divider_album_pick_image);

        }
    }
}

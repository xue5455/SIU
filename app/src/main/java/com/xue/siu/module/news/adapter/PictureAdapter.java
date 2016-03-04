package com.xue.siu.module.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.avos.avoscloud.AVFile;
import com.xue.siu.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by XUE on 2016/3/2.
 */
public class PictureAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private WeakReference<Context> mContextRef;
    private List<AVFile> mPicList;

    public PictureAdapter(Context context, List<AVFile> mPicList) {
        this.mContextRef = new WeakReference<>(context);
        this.mPicList = mPicList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPicList == null ? 0 : mPicList.size();
    }

    @Override
    public AVFile getItem(int position) {
        return mPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mContextRef.get() == null)
            return null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pic_news_fragment, null, false);
        }
        return convertView;
    }
}

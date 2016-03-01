package com.xue.siu.module.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.siu.common.util.HandleUtil;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/3/1.
 */
public class CommentAdapter extends BaseAdapter {
    private WeakReference<Context> mContextRef;

    public CommentAdapter(Context context) {
        mContextRef = new WeakReference<Context>(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (mContextRef.get() != null) {
                convertView = new TextView(mContextRef.get());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(params);
            }
        }
        return convertView;
    }
}

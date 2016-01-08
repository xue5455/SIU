package com.xue.siu.module.schedule.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.xue.siu.R;
import com.xue.siu.application.AppProfile;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;

/**
 * Created by XUE on 2016/1/8.
 */
public class CategoryAdapter extends BaseAdapter {
    int[] drawables = new int[]{R.mipmap.ic_classification_work,
            R.mipmap.ic_classification_work,
            R.mipmap.ic_classification_study,
            R.mipmap.ic_classification_fun};
    int width = (ScreenUtil.getDisplayWidth() - 2 * ResourcesUtil.getDimenPxSize(R.dimen.sf_horizontal_padding)
            - ResourcesUtil.getDimenPxSize(R.dimen.sf_gv_horizontal_space)) / 2;
    int height = width;

    @Override
    public int getCount() {
        return 4;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = new View(AppProfile.getContext());
            GridView.LayoutParams params = new GridView.LayoutParams(width, height);
            convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.view.setBackgroundResource(drawables[position]);
        return convertView;
    }

    static class ViewHolder {
        View view;
    }
}

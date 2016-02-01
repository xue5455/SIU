package com.xue.siu.module.chat.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.xue.siu.module.chat.listener.OnEmojiClickListener;
import com.xue.siu.module.chat.viewholder.FaceViewHolder;

import java.util.List;

/**
 * Created by XUE on 2016/2/1.
 */
public class FaceRvAdapter<TRealViewHolder extends TRecycleViewHolder, TDataModel> extends TRecycleViewAdapter<TRealViewHolder, TDataModel> {
    private int mHeight;
    public FaceRvAdapter(Context context, SparseArray viewHolders, List list, int height) {
        super(context, viewHolders, list);
        mHeight = height;
    }

    @Override
    public void onBindViewHolder(TRecycleViewHolder viewHolder, int position) {
        FaceViewHolder holder = (FaceViewHolder) viewHolder;
        holder.setHeight(mHeight);
        super.onBindViewHolder(viewHolder, position);
    }
}

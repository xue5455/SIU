package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.news.model.PublishEditModel;

/**
 * Created by XUE on 2016/3/4.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_publish_activity)
public class PublishEditViewHolder extends TRecycleViewHolder<PublishEditModel> {
    private EditText mEtContent;
    private HTSwipeRecyclerView mRvPhotos;
    private TRecycleViewAdapter mPhotoAdapter;
    private SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders;
    public PublishEditViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mEtContent = (EditText) view.findViewById(R.id.et_content_publish);
        mRvPhotos = (HTSwipeRecyclerView) view.findViewById(R.id.rv_photos_publish);
        mRvPhotos.setLayoutManager(new GridLayoutManager(context, 4));
    }

    @Override
    public void refresh(TAdapterItem<PublishEditModel> item) {

    }
}

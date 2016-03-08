package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.constant.C;
import com.xue.siu.module.news.model.PublishEditModel;
import com.xue.siu.module.news.viewholder.item.NewsItemType;
import com.xue.siu.module.news.viewholder.item.PublishAddViewHolderItem;
import com.xue.siu.module.news.viewholder.item.PublishPhotoViewHolderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/3/4.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_publish_activity)
public class PublishEditViewHolder extends TRecycleViewHolder<PublishEditModel> implements TextWatcher, ItemEventListener {
    private EditText mEtContent;
    private HTSwipeRecyclerView mRvPhotos;
    private TRecycleViewAdapter mPhotoAdapter;
    private SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders
            = new SparseArray<Class<? extends TRecycleViewHolder>>() {
        {
            put(NewsItemType.ITEM_PUBLISH_ADD, PublishAddViewHolder.class);
            put(NewsItemType.ITEM_PUBLISH_PHOTO, PublishPhotoViewHolder.class);
        }
    };
    private List<TAdapterItem> mItems = new ArrayList<>();
    private PublishEditModel mModel;

    public PublishEditViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mEtContent = findViewById(R.id.et_content_publish);
        mRvPhotos = findViewById(R.id.rv_photos_publish);
        mRvPhotos.setLayoutManager(new GridLayoutManager(context, 4));
        mEtContent.addTextChangedListener(this);
    }

    @Override
    public void refresh(TAdapterItem<PublishEditModel> item) {
        mModel = item.getDataModel();
        if (mPhotoAdapter == null) {
            mPhotoAdapter = new TRecycleViewAdapter(context, mViewHolders, mItems);
            mPhotoAdapter.setItemEventListener(this);
            mPhotoAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    updateRvHeight(mPhotoAdapter.getItemCount());
                }
            });
            mRvPhotos.setAdapter(mPhotoAdapter);
        }
        List<PhotoInfo> photoInfos = mModel.getLocalPicList();
        mItems.clear();
        for (PhotoInfo info : photoInfos) {
            mItems.add(new PublishPhotoViewHolderItem(info));
        }
        if (photoInfos.size() < 9)
            mItems.add(new PublishAddViewHolderItem());
        mPhotoAdapter.notifyDataSetChanged();
        mEtContent.setText(mModel.getContent() == null ? C.EMPTY : mModel.getContent());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mModel.setContent(s.toString());
    }

    public void updateRvHeight(int count) {
        ViewGroup.LayoutParams params = mRvPhotos.getRecyclerView().getLayoutParams();
        int rows = (int) Math.ceil((float) count / 4);
        params.height = PublishPhotoViewHolder.VIEW_SIZE * rows + (rows - 1) * PublishPhotoViewHolder.VERTICAL_SPACING;
        mRvPhotos.getRecyclerView().setLayoutParams(params);
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (view.getId()) {
            case R.id.btn_photo_publish:
                if (listener != null)
                    listener.onEventNotify(ItemEventListener.clickEventName, view, position);
                break;
        }
        return true;
    }
}

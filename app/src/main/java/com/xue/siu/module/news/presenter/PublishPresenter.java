package com.xue.siu.module.news.presenter;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.news.activity.PublishActivity;
import com.xue.siu.module.news.model.PublishEditModel;
import com.xue.siu.module.news.viewholder.PublishEditViewHolder;
import com.xue.siu.module.news.viewholder.item.NewsItemType;
import com.xue.siu.module.news.viewholder.item.PublishEditViewHolderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishPresenter extends BaseActivityPresenter<PublishActivity> implements
        ItemEventListener, HTPickFinishedListener, View.OnClickListener {

    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(NewsItemType.ITEM_PUBLISH_EDIT, PublishEditViewHolder.class);
                }
            };
    private List<TAdapterItem<PublishEditModel>> mItems = new ArrayList<>();
    private PublishEditModel mEditModel;
    private TRecycleViewAdapter mAdapter;
    private List<PhotoInfo> photoList = new ArrayList<>();

    public PublishPresenter(PublishActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        mEditModel = new PublishEditModel();
        mEditModel.setLocalPicList(photoList);
        mItems.add(new PublishEditViewHolderItem(mEditModel));
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mItems);
        mAdapter.setItemEventListener(this);
        mTarget.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (view.getId()) {
            case R.id.btn_photo_publish:
                mTarget.pickImage(mEditModel.getLocalPicList());
                break;
        }
        return true;
    }

    @Override
    public void onImagePickFinished(@Nullable AlbumInfo albumInfo, List<PhotoInfo> photos) {
        mEditModel.setLocalPicList(photos);
        mAdapter.notifyDataSetChanged();
        mTarget.setRightButtonEnabled(photos.size() != 0);
    }

    @Override
    public void onImagePickCanceled() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete_publish:
                break;
        }
    }
}

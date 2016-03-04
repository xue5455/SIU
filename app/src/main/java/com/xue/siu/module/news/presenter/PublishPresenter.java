package com.xue.siu.module.news.presenter;

import android.util.SparseArray;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
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
public class PublishPresenter extends BaseActivityPresenter<PublishActivity> {

    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(NewsItemType.ITEM_PUBLISH_EDIT, PublishEditViewHolder.class);
                }
            };
    private List<TAdapterItem<PublishEditModel>> mItems = new ArrayList<>();
    private PublishEditModel mEditModel;
    private TRecycleViewAdapter mAdapter;

    public PublishPresenter(PublishActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        mEditModel = new PublishEditModel();
        mItems.add(new PublishEditViewHolderItem(mEditModel));
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mItems);
        mTarget.setAdapter(mAdapter);
    }
}

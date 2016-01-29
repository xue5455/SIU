package com.xue.siu.module.chat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.EmojiUtil;
import com.xue.siu.module.chat.viewholder.FaceViewHolder;
import com.xue.siu.module.chat.viewholder.item.FaceViewHolderItem;
import com.xue.siu.module.chat.viewholder.item.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/29.
 */
public class FaceVpAdapter extends PagerAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private SparseArray<Class<? extends TRecycleViewHolder<EmojiUtil.FaceWrapper>>> mViewHolders = new SparseArray<>();
    private View[] mViews = new View[getCount()];
    private int mViewPagerHeight;

    public FaceVpAdapter(Context context, int height) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mViewHolders.put(ItemType.TYPE_FACE, FaceViewHolder.class);
        mViewPagerHeight = height;
    }

    public void setHeight(int height) {
        mViewPagerHeight = height;
    }

    @Override
    public int getCount() {
        return (int) Math.ceil(EmojiUtil.getFaceCount() / (EmojiUtil.FACE_COUNT_ONE_PAGE * 1.0f));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        HTSwipeRecyclerView recyclerView = (HTSwipeRecyclerView) mInflater
                .inflate(R.layout.item_face_pager, container, false).findViewById(R.id.rv_face);
        GridLayoutManager manager = new GridLayoutManager(mContext, 7);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new TRecycleViewAdapter(mContext, mViewHolders, getAdapterItems(position)));
        mViews[position] = recyclerView;
        container.addView(recyclerView);
        return recyclerView;
    }

    private List<TAdapterItem<EmojiUtil.FaceWrapper>> getAdapterItems(int position) {
        List<TAdapterItem<EmojiUtil.FaceWrapper>> list = new ArrayList<>();
        if (position == getCount() - 1) {
            for (int i = 0; i < EmojiUtil.getFaceCount() - position * 21; i++) {
                int realPosition = position * 21 + i;
                list.add(new FaceViewHolderItem(EmojiUtil.getWrapper(realPosition)));
            }
        } else
            for (int i = 0; i < 21; i++) {
                int realPosition = position * 21 + i;
                list.add(new FaceViewHolderItem(EmojiUtil.getWrapper(realPosition)));
            }
        return list;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews[position]);
    }
}

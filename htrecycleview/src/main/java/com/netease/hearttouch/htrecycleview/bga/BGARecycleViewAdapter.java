package com.netease.hearttouch.htrecycleview.bga;

import com.netease.hearttouch.htrecycleview.R;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.base.TBaseRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.util.LogUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zyl06 on 10/15/15.
 */
public class BGARecycleViewAdapter<TDataModel>
        extends TBaseRecycleViewAdapter<BGARecycleViewHolder, TDataModel>
        implements BGASwipeItemLayout.BGASwipeItemLayoutDelegate {

    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> openedItems = new ArrayList<>();
    private BGASwipeItemLayout.BGASwipeItemLayoutDelegate itemDelegate;

    public BGARecycleViewAdapter(Context context,
                                 SparseArray<Class<BGARecycleViewHolder>> viewHolders,
                                 List<TAdapterItem<TDataModel>> items){

        super(context, viewHolders, items);
    }

    @Override
    public void onBindViewHolder(BGARecycleViewHolder holder, int position) {
        TAdapterItem<TDataModel> item = mItems.get(position);
        holder.refresh(item);
    }

    @Override
    public BGARecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BGARecycleViewHolder recycleViewHolder = null;
        try{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            BGASwipeItemLayout swipeItemLayout = (BGASwipeItemLayout)inflater.inflate(R.layout.item_gba_recycleview_container, parent, false);

            Class<? extends BGARecycleViewHolder> viewHolder = mViewHolders.get(viewType);
            BGARecycleViewHolderAnnotation annotation = viewHolder.getAnnotation(BGARecycleViewHolderAnnotation.class);
            View leftContent = inflater.inflate(annotation.leftLayoutId(), null);
            View rightContent = inflater.inflate(annotation.rightLayoutId(), null);

            Constructor constructor = viewHolder.getConstructor(BGASwipeItemLayout.class, View.class, View.class, Context.class, RecyclerView.class);
            recycleViewHolder = (BGARecycleViewHolder) (constructor.newInstance(swipeItemLayout, leftContent, rightContent, parent.getContext(), (RecyclerView)parent));
            recycleViewHolder.setItemEventListener(this);
            recycleViewHolder.setSwipeItemDelegate(this);

            initSwipeItemLayout(swipeItemLayout, recycleViewHolder);

        } catch(Exception e) {
            LogUtil.logE(e.getMessage());
            e.printStackTrace();
        }
        return recycleViewHolder;
    }

    private void initSwipeItemLayout(BGASwipeItemLayout swipeItemLayout,
                                     BGARecycleViewHolder viewHolder) {
        swipeItemLayout.setSwipeable(viewHolder.getSwipeable());
        swipeItemLayout.setSwipeDirection(viewHolder.getSwipeDirection());
        swipeItemLayout.setBottomModel(viewHolder.getBottomModel());
        swipeItemLayout.setSpringDistance(viewHolder.getSpringDistance());

        LinearLayout leftView = (LinearLayout)swipeItemLayout.findViewById(R.id.swipeitemlayout_left_container);
        LinearLayout rightView = (LinearLayout)swipeItemLayout.findViewById(R.id.swipeitemlayout_right_container);
        initLayoutParams(leftView, rightView, viewHolder.getSwipeDirection());
    }

    private void initLayoutParams(ViewGroup leftView,
                                  ViewGroup rightView,
                                  BGASwipeItemLayout.SwipeDirection swipeDirection) {
        final int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        final int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

        if (swipeDirection.equals(BGASwipeItemLayout.SwipeDirection.Left)) {
            ViewGroup.LayoutParams leftLP = leftView.getLayoutParams();
            leftLP.width = matchParent;
            leftLP.height = matchParent;
            leftView.setLayoutParams(leftLP);

            ViewGroup.LayoutParams rightLP = rightView.getLayoutParams();
            rightLP.width = wrapContent;
            rightLP.height = matchParent;
            rightView.setLayoutParams(rightLP);
        }
        else if (swipeDirection.equals(BGASwipeItemLayout.SwipeDirection.Right)) {
            ViewGroup.LayoutParams leftLP = leftView.getLayoutParams();
            leftLP.width = wrapContent;
            leftLP.height = matchParent;
            leftView.setLayoutParams(leftLP);

            ViewGroup.LayoutParams rightLP = rightView.getLayoutParams();
            rightLP.width = matchParent;
            rightLP.height = matchParent;
            rightView.setLayoutParams(rightLP);
        }
    }

    public void setItemDelegate(BGASwipeItemLayout.BGASwipeItemLayoutDelegate delegate) {
        this.itemDelegate = delegate;
    }

    @Override
    public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout, int position) {
        openedItems.add(swipeItemLayout);
        if (itemDelegate != null) {
            itemDelegate.onBGASwipeItemLayoutOpened(swipeItemLayout, position);
        }
    }

    @Override
    public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout, int position) {
        openedItems.remove(swipeItemLayout);
        if (itemDelegate != null) {
            itemDelegate.onBGASwipeItemLayoutClosed(swipeItemLayout, position);
        }
    }

    @Override
    public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout, int position) {
        if (itemDelegate != null) {
            itemDelegate.onBGASwipeItemLayoutStartOpen(swipeItemLayout, position);
        }
    }

    public synchronized void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : openedItems) {
            sil.closeWithAnim();
        }
        openedItems.clear();
    }
}


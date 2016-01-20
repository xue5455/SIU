package com.netease.hearttouch.htswiperefreshrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stone on 15/9/23.
 */

public class HTWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 1;
    //the real Adapter
    private RecyclerView.Adapter mAdapter;
    private HTSwipeRecyclerView mHTRefreshLayout;
    private View headerView;
    private View footerView;
    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getStartCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getStartCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getStartCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headCount = getStartCount();
            notifyItemRangeChanged(fromPosition + headCount, toPosition + headCount + itemCount);
        }
    };

    public HTWrapperAdapter(RecyclerView.Adapter adapter, @NonNull HTSwipeRecyclerView htRefreshLayout) {
        setInnerAdapter(adapter);
        mHTRefreshLayout = htRefreshLayout;
        footerView = mHTRefreshLayout.getFooterView();
    }


    public void removeFooterView() {
        if (footerView != null) {
            footerView = null;
        }
    }

    /**
     * 当前布局是否为Header
     */
    public boolean isHeader(int position) {
        return position == 0 && hasHeaderView();
    }

    /**
     * 当前布局是否为Footer
     */
    public boolean isFooter(int position) {
        return position == getItemCount() - 1 && hasFooterView();
    }

    public boolean hasHeaderView() {
        return headerView != null;
    }

    public boolean hasFooterView() {
        return footerView != null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置StaggeredGridLayoutManager.LayoutParams参数,支持瀑布流布局
        if (viewType == TYPE_HEADER_VIEW) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            headerView.setLayoutParams(params);
            return new ViewHolder(headerView);
        } else if (viewType == TYPE_FOOTER_VIEW) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            footerView.setLayoutParams(params);
            return new ViewHolder(footerView);
        } else
            return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        if (isFooter(position)) {
            if (position > 1 && mHTRefreshLayout.isItemEnough()/* && mHTRefreshLayout.getLastVisibleItemPosition() <= position*/) {
                mHTRefreshLayout.changeLoadMoreFooterView(false);
            } else {
                mHTRefreshLayout.changeLoadMoreFooterView(true);
            }
            return;
        }
        int adjPosition = position - getStartCount();
        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (hasHeaderView()) itemCount++;
        if (hasFooterView()) itemCount++;
        if (mAdapter != null) {
            itemCount += mAdapter.getItemCount();
        }
        return itemCount;
    }

    public int getInnerItemCount() {

        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) { // header
            return TYPE_HEADER_VIEW;
        }
        int adjPosition = position - getStartCount();
        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_FOOTER_VIEW;
    }

    private int getStartCount() {
        return hasHeaderView() ? 1 : 0;
    }

    /**
     * 设置被包裹的Adapter（真正显示数据的Adapter）
     *
     * @param adapter 被包裹的Adapter
     */
    private void setInnerAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (mAdapter != null) {
            notifyItemRangeRemoved(getStartCount(), mAdapter.getItemCount());
            mAdapter.unregisterAdapterDataObserver(dataObserver);
        }

        this.mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(dataObserver);
        notifyItemRangeInserted(getStartCount(), mAdapter.getItemCount());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
package com.xue.siu.common.view.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.util.Log;
import android.view.View;

/**
 * Created by XUE on 2016/1/18.
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mPosition;

    public RecyclerViewDivider(Context context, Drawable dividerDrawable) {
        mDivider = dividerDrawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        int childCount = parent.getLayoutManager().getItemCount();
        if (mPosition == childCount - 1) {
            return;
        }
        drawVertical(c, parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        mPosition = itemPosition;
    }
}

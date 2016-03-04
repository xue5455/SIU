package com.xue.siu.module.imagepick.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by XUE on 2016/2/22.
 */

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int horizontalSpace;
    private int verticalSpace;
    public SpaceDecoration(int horizontalSpace,int verticalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {


        outRect.bottom = 0;
        int position = parent.getChildAdapterPosition(view);
        if (position % 4 == 0) {
            outRect.left = 0;
            outRect.right = horizontalSpace / 2;
        } else if (position % 4 == 3) {
            outRect.left = horizontalSpace / 2;
            outRect.right = 0;
        } else {
            outRect.left = horizontalSpace / 2;
            outRect.right = horizontalSpace / 2;
        }
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) < 4) {
            outRect.top = 0;
        } else {
            outRect.top = verticalSpace;
        }
    }
}


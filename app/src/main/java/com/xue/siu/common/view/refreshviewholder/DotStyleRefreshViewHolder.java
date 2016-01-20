/**
 * Copyright 2015 bingoogolapple
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xue.siu.common.view.refreshviewholder;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTRefreshViewHolder;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.view.progressbar.ArcProgressbar;

public class DotStyleRefreshViewHolder extends HTRefreshViewHolder {
    private ViewGroup mRefreshView;
    /**
     * 底部加载更多菊花控件
     */
    protected ArcProgressbar mFooterProgressBar;
    protected View vLoadding;
    protected View vNomore;

    ObjectAnimator animator;
    private int maxX = (int) ((ResourcesUtil.getDimenPxSize(R.dimen.refresh_size) - ResourcesUtil.getDimenPxSize(R.dimen.refresh_dot_size)) * 0.5);

    public DotStyleRefreshViewHolder(Context context) {
        super(context);
        setPullDistanceScale(3.f);
        setSpringDistanceScale(1.8f);
        setRefreshViewBackgroundResId(R.color.grey_f4);
    }

    @Override
    public View getRefreshHeaderView() {
        if (mRefreshHeaderView == null) {
            mRefreshHeaderView = View.inflate(mContext, R.layout.view_refresh_header, null);
            mRefreshView = (ViewGroup) mRefreshHeaderView.findViewById(R.id.refresh);
        }
        return mRefreshHeaderView;
    }

    @Override
    public void onBeginLoadingMore(boolean hasMore) {
        if (hasMore) {
            vLoadding.setVisibility(View.VISIBLE);
            vNomore.setVisibility(View.GONE);
            mFooterProgressBar.startRotate();
        } else {
            vLoadding.setVisibility(View.GONE);
            vNomore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEndLoadingMore(boolean hasMore) {
        if (hasMore) {
            vLoadding.setVisibility(View.VISIBLE);
            vNomore.setVisibility(View.GONE);

        } else {
            vLoadding.setVisibility(View.GONE);
            vNomore.setVisibility(View.VISIBLE);
        }
        mFooterProgressBar.stopRotate();
    }


    /**
     * 设置 加载中提示文案
     *
     * @param text   文案
     * @param height 高度
     */
    public void setLoadingMoreViewText(String text, int height) {
        if (mLoadMoreFooterView != null) {
            ((TextView) mLoadMoreFooterView.findViewById(R.id.tv_normal_refresh_footer_loading)).setText(text);
            if (vLoadding != null && height > 0) {
                ViewGroup.LayoutParams lp = vLoadding.getLayoutParams();
                lp.height = height;
                vLoadding.setLayoutParams(lp);
            }
        }
    }

    /**
     * 设置 没有更多提示文案
     *
     * @param text   文案
     * @param height 高度
     */
    public void setNoMoreViewText(String text, int height) {
        if (mLoadMoreFooterView != null) {
            ((TextView) mLoadMoreFooterView.findViewById(R.id.tv_normal_refresh_footer_no_more)).setText(text);
            if (vNomore != null && height > 0) {
                ViewGroup.LayoutParams lp = vNomore.getLayoutParams();
                lp.height = height;
                vNomore.setLayoutParams(lp);
            }
        }
    }

    public View getLoadMoreFooterView() {
        if (mLoadMoreFooterView == null) {
            mLoadMoreFooterView = View.inflate(mContext, R.layout.view_refresh_footer_normal, null);
            mFooterProgressBar = (ArcProgressbar) mLoadMoreFooterView.findViewById(R.id.load_progress_bar);
            vLoadding = mLoadMoreFooterView.findViewById(R.id.liner_loading);
            vNomore = mLoadMoreFooterView.findViewById(R.id.liner_no_more);
        }
        return mLoadMoreFooterView;
    }

    @Override
    public void onUIReset() {
        mRefreshView.setRotation(0);
        mFooterProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUIRefreshBegin() {

    }

    @Override
    public void onUIReleaseRefresh() {

    }

    @Override
    public void onUIRefreshing() {
        mRefreshView.setPivotX(0.5f * mRefreshView.getMeasuredWidth());
        mRefreshView.setPivotY(0.5f * mRefreshView.getMeasuredHeight());
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(mRefreshView, "rotation", 0.0f, 360.0f);
            animator.setDuration(1000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
        }
        if (animator.isRunning()) animator.cancel();
        animator.start();
    }

    @Override
    public void onUIRefreshComplete() {
        if (animator != null && animator.isRunning())
            animator.cancel();
    }

    @Override
    public void onUIPositionChange(float scale, int moveYDistance) {
        float diffY = moveYDistance + mRefreshView.getY() - mRefreshHeaderView.getMeasuredHeight();
        if (diffY > 0) {
            diffY = diffY >= maxX ? maxX : diffY;
            ViewCompat.setTranslationX(mRefreshView.getChildAt(0), -diffY);
            ViewCompat.setTranslationX(mRefreshView.getChildAt(1), diffY);
        } else {
            ViewCompat.setTranslationX(mRefreshView.getChildAt(0), 0);
            ViewCompat.setTranslationX(mRefreshView.getChildAt(1), 0);
        }
    }

}
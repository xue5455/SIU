package com.xue.siu.module.news.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.news.model.ActionVO;
import com.xue.siu.module.news.presenter.ActionPresenter;
import com.xue.siu.module.news.presenter.NewsEventListener;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/2/15.
 */
public class ActionFragment extends BaseBlankFragment<ActionPresenter> implements HTSwipeRecyclerView.OnLayoutSizeChangedListener {
    private HTSwipeRecyclerView mRvNews;
    private NewsEventListener mNewsListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootViewRef == null || mRootViewRef.get() == null) {
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            setRealContentView(R.layout.fragment_news_common);
            mRootViewRef = new WeakReference<>(rootView);
            initViews();
        } else {
            ViewGroup parent = (ViewGroup) mRootViewRef.get().getParent();
            if (parent != null) {
                parent.removeView(mRootViewRef.get());
            }
        }
        return mRootViewRef.get();
    }

    private void initViews() {
        mRvNews = findViewById(R.id.rv_news);
        mRvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvNews.setOnRefreshListener(mPresenter);
        mRvNews.setOnLoadMoreListener(mPresenter);
        mRvNews.addOnScrollListener(mPresenter);
        mRvNews.setOnLayoutSizeChangedListener(this);
    }

    public void setRefreshComplete() {
        mRvNews.setRefreshComplete(false);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActionPresenter(this);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvNews.setAdapter(adapter);
    }

    public void setCommentListener(NewsEventListener listener) {
        this.mNewsListener = listener;
    }

    public void showInput(ActionVO actionVO, AVUser to) {
        if (mNewsListener != null)
            mNewsListener.showInput(actionVO, to);
    }

    public void hideInput() {
        if (mNewsListener != null)
            mNewsListener.hideInput();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        int diff = oldH - h;
        LogUtil.i("xxj","LayoutSizeChanged " + diff);
    }
}

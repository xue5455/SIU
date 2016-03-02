package com.xue.siu.module.news.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.news.presenter.ActionPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/2/15.
 */
public class ActionFragment extends BaseBlankFragment<ActionPresenter> {
    private HTSwipeRecyclerView mRvNews;

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
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ActionPresenter(this);
    }

    public void setAdapter(TRecycleViewAdapter adapter){
        mRvNews.setAdapter(adapter);
    }
}

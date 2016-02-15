package com.xue.siu.module.news.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.news.presenter.CalendarPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/2/15.
 */
public class CalendarFragment extends BaseBlankFragment<CalendarPresenter> {
    private HTSwipeRecyclerView mRvNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootViewRef == null || mRootViewRef.get() == null) {
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            setRealContentView(R.layout.fragment_news_common);
            mRootViewRef = new WeakReference<>(rootView);

        } else {
            ViewGroup parent = (ViewGroup) mRootViewRef.get().getParent();
            if (parent != null) {
                parent.removeView(mRootViewRef.get());
            }
        }
        return mRootViewRef.get();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CalendarPresenter(this);
    }
}

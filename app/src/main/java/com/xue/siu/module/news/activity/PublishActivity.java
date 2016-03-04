package com.xue.siu.module.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.news.presenter.PublishPresenter;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishActivity extends BaseActionBarActivity<PublishPresenter> {
    private HTSwipeRecyclerView mRvContent;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PublishActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_publish);
        initViews();
        setNavigationBarBlack();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PublishPresenter(this);
    }

    private void initViews() {
        mRvContent = findView(R.id.rv_content_publish);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvContent.setAdapter(adapter);
    }
}

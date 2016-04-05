package com.xue.siu.module.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.netease.hearttouch.htimagepicker.HTImagePicker;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.news.presenter.PublishPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishActivity extends BaseActionBarActivity<PublishPresenter> {
    private HTSwipeRecyclerView mRvContent;
    private Button mBtnPublish;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PublishActivity.class);
        activity.startActivity(intent);
    }

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PublishActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_publish);
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(R.color.common_background));
        initViews();
        setNavigationBarBlack();
        setStatueBarColor(R.color.action_bar_bg);

    }

    @Override
    protected void initPresenter() {
        mPresenter = new PublishPresenter(this);
    }

    private void initViews() {
        mRvContent = findView(R.id.rv_content_publish);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        View view = LayoutInflater.from(this).
                inflate(R.layout.view_btn_publish_activity, null, false);
        mBtnPublish = (Button) view.findViewById(R.id.btn_complete_publish);
        mBtnPublish.setOnClickListener(mPresenter);
        setRightView(view);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvContent.setAdapter(adapter);
    }

    public void pickImage(List<PhotoInfo> list) {
        HTImagePicker.getDefault().start(this,
                null, (ArrayList) list,
                true, 9, false, "选择图片",
                mPresenter);
    }

    public void setRightButtonEnabled(boolean enabled) {
        mBtnPublish.setEnabled(enabled);
    }
}

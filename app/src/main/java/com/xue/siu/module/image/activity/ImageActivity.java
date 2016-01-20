package com.xue.siu.module.image.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.image.presenter.ImagePresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2016/1/19.
 */
public class ImageActivity extends BaseActionBarActivity<ImagePresenter> {
    @Bind(R.id.rv_image)
    HTSwipeRecyclerView mRvImage;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ImageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ImagePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initContentView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initContentView() {
        setNavigationBarBlack();
        setTitle(R.string.ia_title);
        navigationBar.setBackButtonClick(mPresenter);
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvImage.setLayoutManager(manager);
        mRvImage.getRecyclerView().getItemAnimator().setChangeDuration(0);

    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvImage.setAdapter(adapter);
    }

    public void addOnScrollListener(HTSwipeRecyclerView.OnScrollListener onScrollListener) {
        mRvImage.addOnScrollListener(onScrollListener);
    }
}

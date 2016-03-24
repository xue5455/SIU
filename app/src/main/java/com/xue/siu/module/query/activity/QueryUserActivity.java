package com.xue.siu.module.query.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.base.activity.BaseBlankActivity;
import com.xue.siu.module.query.presenter.QueryUserPresenter;

/**
 * Created by XUE on 2016/1/19.
 */
public class QueryUserActivity extends BaseBlankActivity<QueryUserPresenter> {

    private EditText etQuery;
    private HTSwipeRecyclerView rvResult;
    private View viewBack;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, QueryUserActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new QueryUserPresenter(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_query_user);
        initContentView();
        setStatueBarColor(R.color.action_bar_bg);
    }

    private void initContentView() {
        etQuery = findView(R.id.query_user_et);
        rvResult = findView(R.id.query_user_rv);
        viewBack = findView(R.id.query_user_back_btn);
        viewBack.setOnClickListener(mPresenter);
        etQuery.addTextChangedListener(mPresenter);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        rvResult.setAdapter(adapter);
    }
}

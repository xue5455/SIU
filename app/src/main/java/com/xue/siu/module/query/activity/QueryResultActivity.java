package com.xue.siu.module.query.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.query.presenter.QueryResultPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2016/1/21.
 */
public class QueryResultActivity extends BaseActionBarActivity<QueryResultPresenter> {
    public static final String KEY_SEARCH = "keyword";
    @Bind(R.id.rv_result)
    HTSwipeRecyclerView mRvResult;

    public static void start(Activity activity, String keyword) {
        Intent intent = new Intent(activity, QueryResultActivity.class);
        intent.putExtra(KEY_SEARCH, keyword);
        activity.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new QueryResultPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_query_result);
        ButterKnife.bind(this);
        initContentView();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    private void initContentView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvResult.setLayoutManager(manager);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvResult.setAdapter(adapter);
    }
}

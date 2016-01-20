package com.xue.siu.module.query.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.query.presenter.QueryUserPresenter;

/**
 * Created by XUE on 2016/1/19.
 */
public class QueryUserActivity extends BaseActionBarActivity<QueryUserPresenter> {
    private EditText mEtUser;
    private RecyclerView mRvResult;
    private TextView mTvEmpty;

    @Override
    protected void initPresenter() {
        mPresenter = new QueryUserPresenter(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_query_user);
        initContentView();
    }

    private void initContentView() {
        setNavigationBarBlack();
        mTvEmpty = findView(R.id.tv_empty);
        mRvResult = findView(R.id.rv_result);
        mEtUser = findView(R.id.et_query);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvResult.setLayoutManager(layoutManager);
    }

    public void notifyResult() {
        if (mRvResult.getAdapter().getItemCount() > 0) {
            mTvEmpty.setVisibility(View.GONE);
        } else {
            mTvEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRvResult.setAdapter(adapter);
    }
}

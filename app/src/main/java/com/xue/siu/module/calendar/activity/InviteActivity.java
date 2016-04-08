package com.xue.siu.module.calendar.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.calendar.presenter.InvitePresenter;

/**
 * Created by XUE on 2016/4/6.
 */
public class InviteActivity extends BaseActionBarActivity<InvitePresenter> {
    private HTSwipeRecyclerView rvUsers;
    public static final String KEY_USER = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_invite);
        initViews();
    }


    private void initViews() {
        rvUsers = findView(R.id.invite_users_rv);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setOnRefreshListener(mPresenter);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        rvUsers.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new InvitePresenter(this);
    }

    public void refreshDone(){
        rvUsers.setRefreshComplete(false);
    }
}

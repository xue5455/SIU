package com.xue.siu.module.query.presenter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.query.activity.QueryResultActivity;
import com.xue.siu.module.query.activity.QueryUserActivity;
import com.xue.siu.module.query.viewholder.QueryKeyViewHolder;
import com.xue.siu.module.query.viewholder.item.QueryKeyItemType;
import com.xue.siu.module.query.viewholder.item.QueryKeyViewHolderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/1/19.
 */
public class QueryUserPresenter extends BaseActivityPresenter<QueryUserActivity> implements TextWatcher, ItemEventListener {

    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();
    private List<TAdapterItem<String>> mAdapterItems = new ArrayList<>();
    private TRecycleViewAdapter mAdapter;

    public QueryUserPresenter(QueryUserActivity target) {
        super(target);
    }

    private String mKeyWord;

    @Override
    protected void initActivity() {
        mViewHolders.put(QueryKeyItemType.COMMON_ITEM, QueryKeyViewHolder.class);
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mAdapterItems);
        mTarget.setAdapter(mAdapter);
        mAdapter.setItemEventListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mAdapter == null)
            return;
        mKeyWord = s.toString();
        if (TextUtils.isEmpty(mKeyWord)) {
            mAdapterItems.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }
        mAdapterItems.clear();
        mAdapterItems.add(new QueryKeyViewHolderItem(mKeyWord));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        if (TextUtils.equals(eventName, ItemEventListener.clickEventName)) {
            QueryResultActivity.start(mTarget, mKeyWord);
        }
        return true;
    }
}

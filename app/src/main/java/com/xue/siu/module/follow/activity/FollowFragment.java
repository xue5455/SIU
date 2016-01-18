package com.xue.siu.module.follow.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.view.divider.RecyclerViewDivider;
import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.follow.Constants;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.presenter.FollowFragmentPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/1/18.
 */
public class FollowFragment extends BaseBlankFragment<FollowFragmentPresenter> {
    private FragmentType mType;
    private RecyclerView mFollowRV;

    @Override
    protected void initPresenter() {
        mPresenter = new FollowFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootViewRef == null || mRootViewRef.get() == null) {
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            setRealContentView(R.layout.fragment_follow);
            initType();
            initContentView();
            mRootViewRef = new WeakReference<>(rootView);
        } else {
            ViewGroup parent = (ViewGroup) mRootViewRef.get().getParent();
            if (parent != null) {
                parent.removeView(mRootViewRef.get());
            }
        }
        return mRootViewRef.get();
    }

    private void initContentView() {
        mFollowRV = findViewById(R.id.rv_follow);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mFollowRV.setLayoutManager(manager);
        mFollowRV.addItemDecoration(new RecyclerViewDivider(getActivity(), ResourcesUtil.getDrawable(R.drawable.shape_follow_list_divider)));
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mFollowRV.setAdapter(adapter);
    }

    private void initType() {
        String type = getArguments().getString(Constants.FRAGMENT_TYPE_KEY);
        if (type != null) {
            mType = FragmentType.getType(type);
        }
    }

    public FragmentType getType() {
        return mType;
    }
}

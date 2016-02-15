package com.xue.siu.module.discovery.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.module.base.activity.BaseActionBarFragment;
import com.xue.siu.module.base.activity.BaseActivity;
import com.xue.siu.module.base.activity.BaseFragment;
import com.xue.siu.module.discovery.presenter.DiscoveryPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2015/12/9.
 */
public class DiscoveryFragment extends BaseActionBarFragment<DiscoveryPresenter> {

    @Override
    protected void initPresenter() {
        mPresenter = new DiscoveryPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootViewRef == null || mRootViewRef.get() == null) {
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            setRealContentView(R.layout.fragment_discovery);
            mRootViewRef = new WeakReference<>(rootView);
            setTitle(R.string.mainpage_tab_discovery);
            initViews();
        } else {
            ViewGroup parent = (ViewGroup) mRootViewRef.get().getParent();
            if (parent != null) {
                parent.removeView(mRootViewRef.get());
            }
        }
        return mRootViewRef.get();
    }

    private void initViews() {
        int[] texts = new int[]{R.string.df_news,
                R.string.df_hot_plans, R.string.df_city_users,
                R.string.df_city_plans, R.string.df_user_board};
        int[] drawables = new int[]{R.mipmap.icon_news,
                R.mipmap.icon_hot_plans, R.mipmap.icon_city_user,
                R.mipmap.icon_city_plans, R.mipmap.icon_user_board};
        int[] ids = new int[]{R.id.fl_news,
                R.id.fl_hot_plans, R.id.fl_city_users,
                R.id.fl_city_plans, R.id.fl_user_board};
        ImageView img;
        TextView tv;
        View view;
        for (int i = 0; i < texts.length; i++) {
            view = findViewById(ids[i]);
            view.setOnClickListener(mPresenter);
            img = (ImageView) view.findViewById(R.id.iv_icon);
            tv = (TextView) view.findViewById(R.id.tv_df);
            img.setImageResource(drawables[i]);
            tv.setText(texts[i]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setStatueBarColor(R.color.black);
        }
    }
}

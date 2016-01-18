package com.xue.siu.module.userpage.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.module.base.activity.BaseActivity;
import com.xue.siu.module.base.activity.BaseBlankFragment;
import com.xue.siu.module.base.activity.BaseFragment;
import com.xue.siu.module.chat.view.BubbleInDrawable;
import com.xue.siu.module.mainpage.activity.MainPageActivity;
import com.xue.siu.module.userpage.presenter.UserPagePresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2015/12/9.
 */
public class UserPageFragment extends BaseBlankFragment<UserPagePresenter> {
    /**
     * 关注
     */
    private TextView mFolloweeView;
    /**
     * 粉丝
     */
    private TextView mFollowerView;

    @Override
    protected void initPresenter() {
        mPresenter = new UserPagePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootViewRef == null || mRootViewRef.get() == null) {
            View rootView = super.onCreateView(inflater, container, savedInstanceState);
            setRealContentView(R.layout.fragment_user_page);
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
        View view = mContentView.findViewById(R.id.followee_view);
        view.setOnClickListener(mPresenter);
        ImageView icon = (ImageView) view.findViewById(R.id.icon_view);
        icon.setImageResource(R.mipmap.ic_followee);
        TextView text = (TextView) view.findViewById(R.id.follow_view);
        text.setText(ResourcesUtil.getString(R.string.upf_followee));
        view = mContentView.findViewById(R.id.follower_view);
        view.setOnClickListener(mPresenter);
        icon = (ImageView) view.findViewById(R.id.icon_view);
        icon.setImageResource(R.mipmap.ic_follower);
        text = (TextView) view.findViewById(R.id.follow_view);
        text.setText(ResourcesUtil.getString(R.string.upf_follower));
        view = mContentView.findViewById(R.id.calendar_btn);
        view.setOnClickListener(mPresenter);
        text = (TextView) view.findViewById(R.id.button_text);
        text.setText(R.string.upf_calendar_collect);
        view = mContentView.findViewById(R.id.message_btn);
        view.setOnClickListener(mPresenter);
        text = (TextView) view.findViewById(R.id.button_text);
        text.setText(R.string.upf_message);
        view = mContentView.findViewById(R.id.setting_btn);
        view.setOnClickListener(mPresenter);
        text = (TextView) view.findViewById(R.id.button_text);
        text.setText(R.string.upf_setting);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getClass().equals(MainPageActivity.class)) {
            ((BaseActivity) getActivity()).setStatueBarColor(R.color.upf_status_bar_green);
        }
    }
}

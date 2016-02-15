package com.xue.siu.module.follow.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.follow.Constants;
import com.xue.siu.module.follow.FragmentType;
import com.xue.siu.module.follow.activity.FollowActivity;
import com.xue.siu.module.follow.activity.FollowFragment;
import com.xue.siu.module.follow.adapter.FollowPageAdapter;
import com.xue.siu.module.query.activity.QueryUserActivity;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowPresenter extends BaseActivityPresenter<FollowActivity> implements View.OnClickListener,
        ItemEventListener, ViewPager.OnPageChangeListener {

    SparseArray<Fragment> mFragmentArray = new SparseArray<>();


    public FollowPresenter(FollowActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        Fragment fragment = new FollowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_TYPE_KEY, FragmentType.FolloweeFragment.toString());
        fragment.setArguments(bundle);
        mFragmentArray.put(0, fragment);
        fragment = new FollowFragment();
        bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_TYPE_KEY, FragmentType.FollowerFragment.toString());
        fragment.setArguments(bundle);
        mFragmentArray.put(1, fragment);
        FollowPageAdapter adapter = new FollowPageAdapter(mTarget.getSupportFragmentManager(), mFragmentArray);
        mTarget.initAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_img:
                mTarget.finish();
                break;
            case R.id.tv_follower:
                mTarget.setCurrentItem(1, true);
                break;
            case R.id.tv_followee:
                mTarget.setCurrentItem(0, true);
                break;
            case R.id.iv_search:
                QueryUserActivity.start(mTarget);
                break;
        }
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        if (TextUtils.equals(eventName, ItemEventListener.clickEventName)) {

        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTarget.updateText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            //显示SideBar
            mTarget.setLetterBarVisibility(true);
        } else {
            //隐藏SideBar
            mTarget.setLetterBarVisibility(false);
        }
    }
}

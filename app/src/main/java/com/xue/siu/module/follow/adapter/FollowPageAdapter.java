package com.xue.siu.module.follow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

/**
 * Created by XUE on 2016/1/16.
 */
public class FollowPageAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> mFragmentArray;

    public FollowPageAdapter(FragmentManager fm, SparseArray<Fragment> array) {
        super(fm);
        mFragmentArray = array;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentArray.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentArray.size();
    }
}

package com.xue.siu.module.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by XUE on 2016/2/15.
 */
public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> mFragmentList;

    public NewsPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        if (mFragmentList == null)
            return 0;
        return mFragmentList.size();
    }
}

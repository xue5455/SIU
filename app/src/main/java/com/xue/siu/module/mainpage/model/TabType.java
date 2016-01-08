package com.xue.siu.module.mainpage.model;

import android.text.TextUtils;

import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;


/**
 * Created by zyl06 on 11/10/15.
 */
public enum TabType {
    Schedule(ResourcesUtil.getString(R.string.mainpage_tab_schedule)),
    Group(ResourcesUtil.getString(R.string.mainpage_tab_group)),
    Discovery(ResourcesUtil.getString(R.string.mainpage_tab_discovery)),
    UserPage(ResourcesUtil.getString(R.string.mainpage_tab_account));

    String value;
    TabType(String v) {
        value = v;
    }

    @Override
    public String toString() {
        return value;
    }

    public static int getTabPosition(String tabName) {
        if (TextUtils.equals(Schedule.toString(), tabName)) {
            return 0;
        } else if (TextUtils.equals(Group.toString(), tabName)) {
            return 1;
        } else if (TextUtils.equals(Discovery.toString(), tabName)) {
            return 2;
        } else if (TextUtils.equals(UserPage.toString(), tabName)) {
            return 3;
        }

        return -1;
    }
}

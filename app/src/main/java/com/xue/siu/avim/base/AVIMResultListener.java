package com.xue.siu.avim.base;

import com.avos.avoscloud.AVException;

/**
 * Created by XUE on 2016/3/8.
 */
public interface AVIMResultListener {
    void onLeanError(String cbName,AVException e);

    void onLeanSuccess(String cbName, Object... values);
}

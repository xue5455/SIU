package com.xue.siu.module.calendar.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.avim.base.BaseCallback;

/**
 * Created by XUE on 2016/3/25.
 */
public class CalendarSaveCallback extends BaseCallback<SaveCallback, Object> {

    public CalendarSaveCallback(AVIMResultListener listener) {
        super(listener);
    }

    @Override
    protected boolean isRelatedToActivity() {
        return false;
    }

    @Override
    protected void initCallback() {
        callback = new SaveCallback() {
            @Override
            public void done(AVException e) {
                result(null, e);
            }
        };
    }

    @Override
    protected String getCbName() {
        return getClass().getName();
    }
}

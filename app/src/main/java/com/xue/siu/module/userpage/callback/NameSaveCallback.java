package com.xue.siu.module.userpage.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.avim.base.BaseCallback;

/**
 * Created by XUE on 2016/4/5.
 */
public class NameSaveCallback extends BaseCallback<SaveCallback, Object> {
    public NameSaveCallback(AVIMResultListener listener) {
        super(listener);
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

    @Override
    protected boolean isRelatedToActivity() {
        return true;
    }
}

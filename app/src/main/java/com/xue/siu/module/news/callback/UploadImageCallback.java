package com.xue.siu.module.news.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.avim.base.BaseCallback;

/**
 * Created by XUE on 2016/3/7.
 */
public class UploadImageCallback extends BaseCallback<SaveCallback,Object> {

    @Override
    protected boolean isRelatedToActivity() {
        return true;
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

    public UploadImageCallback(AVIMResultListener listener) {
        super(listener);
    }

}

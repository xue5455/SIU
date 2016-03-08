package com.xue.siu.module.news.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.xue.siu.avim.base.BaseSaveCallback;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.module.base.presenter.BasePresenter;
import com.xue.siu.module.news.presenter.PublishPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by XUE on 2016/3/7.
 */
public class UploadImageCallback extends BaseSaveCallback<PublishPresenter> {

    @Override
    protected boolean isRelatedToActivity() {
        return true;
    }

    public UploadImageCallback(PublishPresenter presenter) {
        super(presenter);
    }

    @Override
    protected void onAVIMError(PublishPresenter presenter, AVException e) {
        presenter.hideProgressDialog();
        LogUtil.i("xxj", "upload fail");
    }

    @Override
    protected void onAVIMSuccess(PublishPresenter presenter) {
        presenter.addImageToList();
        LogUtil.i("xxj","upload success");
    }
}

package com.xue.siu.module.follow.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.avim.base.BaseCallback;


/**
 * Created by XUE on 2016/3/23.
 */
public class FriendshipCallback extends BaseCallback<AVFriendshipCallback,AVFriendship>{
    public FriendshipCallback(AVIMResultListener listener) {
        super(listener);
    }

    @Override
    protected boolean isRelatedToActivity() {
        return true;
    }

    @Override
    protected void initCallback() {
        callback = new AVFriendshipCallback() {
            @Override
            public void done(AVFriendship avFriendship, AVException e) {
                result(avFriendship,e);
            }
        };
    }

    @Override
    protected String getCbName() {
        return FriendshipCallback.class.getName();
    }
}

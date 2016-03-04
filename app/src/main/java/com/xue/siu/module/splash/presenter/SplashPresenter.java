package com.xue.siu.module.splash.presenter;

import android.text.TextUtils;

import com.xue.siu.common.util.HandleUtil;
import com.xue.siu.common.util.TextUtil;
import com.xue.siu.db.MySharePreferenceHelper;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.login.activity.LoginActivity;
import com.xue.siu.module.mainpage.activity.MainPageActivity;
import com.xue.siu.module.mainpage.model.TabType;
import com.xue.siu.module.splash.activity.SplashActivity;

/**
 * Created by XUE on 2015/12/9.
 */
public class SplashPresenter extends BaseActivityPresenter<SplashActivity> {
    private static final int WAIT_TIME = 2000;

    public SplashPresenter(SplashActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        HandleUtil.doDelay(new Runnable() {
            @Override
            public void run() {
                if (!isLogged())
                    LoginActivity.start(mTarget, null, null);
                else
                    MainPageActivity.start(mTarget, TabType.Schedule);
                mTarget.finish();
            }
        }, WAIT_TIME);
    }


    private boolean isLogged() {
        String user = SharePreferenceHelper.getGlobalString(SharePreferenceC.ACCOUNT, "");
        String pass = SharePreferenceHelper.getGlobalString(SharePreferenceC.PASSWORD, "");
        return !TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass);
    }
}

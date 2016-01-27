package com.xue.siu.module.splash.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.xue.siu.R;
import com.xue.siu.common.view.maskablelayout.MaskableLayout;
import com.xue.siu.module.base.activity.BaseBlankActivity;
import com.xue.siu.module.chat.view.BubbleDrawable;
import com.xue.siu.module.splash.presenter.SplashPresenter;

/**
 * Created by XUE on 2015/12/9.
 */
public class SplashActivity extends BaseBlankActivity<SplashPresenter> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setStatueBarColor(R.color.transparent);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }

    @Override
    public void onBackPressed() {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        //点启动页只能回到桌面，不让退出
        ActivityInfo ai = homeInfo.activityInfo;
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
        startActivity(startIntent);
    }
}

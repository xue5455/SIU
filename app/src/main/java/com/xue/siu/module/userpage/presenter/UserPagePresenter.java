package com.xue.siu.module.userpage.presenter;

import android.view.View;

import com.xue.siu.module.base.presenter.BaseFragmentPresenter;
import com.xue.siu.module.userpage.activity.UserPageFragment;

/**
 * Created by XUE on 2015/12/9.
 */
public class UserPagePresenter extends BaseFragmentPresenter<UserPageFragment> implements View.OnClickListener {


    public UserPagePresenter(UserPageFragment target) {
        super(target);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume(){
        super.onResume();

    }
}

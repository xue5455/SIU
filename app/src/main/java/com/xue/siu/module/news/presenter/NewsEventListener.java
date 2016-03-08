package com.xue.siu.module.news.presenter;

import com.avos.avoscloud.AVUser;
import com.xue.siu.module.news.model.ActionVO;

/**
 * Created by XUE on 2016/3/8.
 */
public interface NewsEventListener {
    void showInput(ActionVO actionVO, AVUser to);
    void hideInput();
}

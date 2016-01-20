package com.xue.siu.module.follow.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.module.follow.model.UserVO;

/**
 * Created by XUE on 2016/1/19.
 */
public class FollowViewHolderItem implements TAdapterItem<UserVO> {
    UserVO mUserVo;

    public FollowViewHolderItem(UserVO userVO) {
        mUserVo = userVO;
    }

    @Override
    public int getViewType() {
        return ItemType.ITEM_COMMON;
    }

    @Override
    public int getId() {
        return mUserVo.hashCode();
    }

    @Override
    public UserVO getDataModel() {
        return mUserVo;
    }
}

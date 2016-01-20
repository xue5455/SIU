package com.xue.siu.module.follow.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.avim.model.LeanUser;

/**
 * Created by XUE on 2016/1/19.
 */
public class FollowViewHolderItem implements TAdapterItem<LeanUser> {
    LeanUser mUserVo;

    public FollowViewHolderItem(LeanUser userVO) {
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
    public LeanUser getDataModel() {
        return mUserVo;
    }
}

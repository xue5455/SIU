package com.xue.siu.module.chat.viewholder.item;

import com.avos.avoscloud.AVUser;
import com.xue.siu.db.bean.SIUMessage;

/**
 * Created by XUE on 2016/1/27.
 */
public class ImageMsgInViewHolderItem extends MsgViewHolderItem {

    public ImageMsgInViewHolderItem(SIUMessage msg, AVUser user) {
        super(msg,user);
    }
    @Override
    public int getViewType() {
        return ItemType.TYPE_IMAGE_IN;
    }

}

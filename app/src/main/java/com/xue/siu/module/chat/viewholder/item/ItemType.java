package com.xue.siu.module.chat.viewholder.item;

import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.MsgType;

/**
 * Created by XUE on 2016/1/27.
 */
public interface ItemType {
    int TYPE_TEXT_IN = MsgDirection.IN.getValue()* MsgType.Text.getValue();
    int TYPE_TEXT_OUT = MsgDirection.OUT.getValue()* MsgType.Text.getValue();
    int TYPE_IMAGE_IN = MsgDirection.IN.getValue()* MsgType.Image.getValue();
    int TYPE_IMAGE_OUT = MsgDirection.OUT.getValue()* MsgType.Image.getValue();
    int TYPE_FACE = 5;
}

package com.xue.siu.module.news.viewholder.item;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.xue.siu.module.news.model.ActionVO;

/**
 * Created by XUE on 2016/3/2.
 */
public class NewsActionViewHolderItem implements TAdapterItem<ActionVO>{
    private ActionVO actionVO;

    public NewsActionViewHolderItem(ActionVO actionVO) {
        this.actionVO = actionVO;
    }

    @Override
    public int getViewType() {
        return NewsItemType.ITEM_COMMON_ACTION;
    }

    @Override
    public int getId() {
        return actionVO.hashCode();
    }

    @Override
    public ActionVO getDataModel() {
        return actionVO;
    }
}

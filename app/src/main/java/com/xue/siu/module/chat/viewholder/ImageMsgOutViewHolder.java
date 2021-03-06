package com.xue.siu.module.chat.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.TimeUtil;
import com.xue.siu.common.view.maskablelayout.MaskableLayout;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.module.chat.view.BubbleDrawable;
import com.xue.siu.module.chat.viewholder.item.MessageUserWrapper;

/**
 * Created by XUE on 2016/1/27.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_chat_send_image)
public class ImageMsgOutViewHolder extends TRecycleViewHolder<MessageUserWrapper> implements View.OnClickListener,
        View.OnLongClickListener {
    private SimpleDraweeView mSdvPortrait;
    private SimpleDraweeView mSdvContent;
    private TextView mTvName;
    private TextView mTvTime;
    private MaskableLayout mLayoutMask;

    public ImageMsgOutViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvContent = findViewById(R.id.sdv_img);
        mSdvPortrait = findViewById(R.id.sdv_portrait);
        mTvName = findViewById(R.id.tv_name);
        mTvTime = findViewById(R.id.tv_time);
        mLayoutMask = findViewById(R.id.layout_mask);
        mLayoutMask.setMask(new BubbleDrawable(MsgDirection.OUT));
        mSdvPortrait.setOnClickListener(this);
        mSdvContent.setOnClickListener(this);
        mSdvContent.setOnLongClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<MessageUserWrapper> item) {
        SIUMessage msg = item.getDataModel().getMsg();
        AVUser user = item.getDataModel().getUser();
//        mSdvPortrait.setImageURI(Uri.EMPTY);
        mSdvPortrait.setImageURI(Uri.parse(user.get("portraitUrl").toString()));
        mSdvContent.setImageURI(Uri.EMPTY);

        mTvTime.setText(TimeUtil.convertLongToString(msg.getsTime()));
        mTvName.setText(user.getUsername());
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.longClickEventName, v, getAdapterPosition());
            return true;
        }
        return false;
    }
}

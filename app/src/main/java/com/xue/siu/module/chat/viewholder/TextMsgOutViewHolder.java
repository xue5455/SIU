package com.xue.siu.module.chat.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.application.AppProfile;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.StaticLayoutManager;
import com.xue.siu.common.util.TextUtil;
import com.xue.siu.common.util.TimeUtil;
import com.xue.siu.common.view.maskablelayout.MaskableLayout;
import com.xue.siu.common.view.textview.StaticLayoutView;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.module.chat.view.BubbleDrawable;
import com.xue.siu.module.chat.viewholder.item.MessageUserWrapper;

/**
 * Created by XUE on 2016/1/27.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_chat_send_text)
public class TextMsgOutViewHolder extends TRecycleViewHolder<MessageUserWrapper> implements View.OnClickListener,
        View.OnLongClickListener {
    private StaticLayoutView mTvContent;

    private TextView mTvName;

    private TextView mTvTime;

    private SimpleDraweeView mSdvPortrait;
    private MaskableLayout mLayoutMask;

    public TextMsgOutViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mTvContent = findViewById(R.id.tv_msg);
        mTvTime = findViewById(R.id.tv_time);
        mTvName = findViewById(R.id.tv_name);
        mSdvPortrait = findViewById(R.id.sdv_portrait);
        mLayoutMask = findViewById(R.id.layout_mask);
        mLayoutMask.setMask(new BubbleDrawable(MsgDirection.OUT, ResourcesUtil.getColor(R.color.green_normal)));
        mSdvPortrait.setOnClickListener(this);
        mTvContent.setOnLongClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<MessageUserWrapper> item) {
        LogUtil.i("ChatPresenter","refresh");
        SIUMessage message = item.getDataModel().getMsg();
        AVUser user = item.getDataModel().getUser();
//        SpannableStringBuilder spannableStringBuilder = message.getSpannableStringBuilder();
//        if (spannableStringBuilder == null) {
//            spannableStringBuilder = TextUtil.generateSpannableString(AppProfile.getContext(), message.getContent());
//            message.setSpannableStringBuilder(spannableStringBuilder);
//            LogUtil.i("ChatPresenter","null");
//        }
//        mTvContent.setText(spannableStringBuilder);
        StaticLayout layout = StaticLayoutManager.getInstance().getLayout(message.getContent(), Color.WHITE);
        mTvContent.setLayout(layout);
        mTvTime.setText(TimeUtil.convertLongToString(message.getsTime()));
        mTvName.setText(user.getUsername());
        mSdvPortrait.setImageURI(Uri.parse(user.get("portraitUrl").toString()));


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

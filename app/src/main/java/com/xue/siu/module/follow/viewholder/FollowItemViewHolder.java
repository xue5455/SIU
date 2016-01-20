package com.xue.siu.module.follow.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.module.follow.model.UserVO;

/**
 * Created by XUE on 2016/1/19.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_follow_list)
public class FollowItemViewHolder extends TRecycleViewHolder<UserVO> implements View.OnClickListener {
    private SimpleDraweeView mSdvPortrait;
    private TextView mTvName;
    private View mContainer;

    public FollowItemViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvPortrait = findViewById(R.id.dv_portrait);
        mTvName = findViewById(R.id.tv_name);
        mContainer = findViewById(R.id.rl_container);
    }

    @Override
    public void refresh(TAdapterItem<UserVO> item) {
        UserVO userVO = item.getDataModel();
        mSdvPortrait.setImageURI(Uri.parse(userVO.getUrl()));
        mTvName.setText(userVO.getName());
        mContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
        }
    }
}
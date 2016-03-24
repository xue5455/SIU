package com.xue.siu.module.follow.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.module.follow.model.UserVO;

/**
 * Created by XUE on 2016/1/19.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_follow_list)
public class FollowItemViewHolder extends TRecycleViewHolder<AVUser> implements View.OnClickListener {
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
        mContainer.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<AVUser> item) {
        mSdvPortrait.setImageURI(Uri.parse(item.getDataModel().get(LeanConstants.PORTRAIT).toString()));
        String nickname = (String) item.getDataModel().get(LeanConstants.NICK_NAME);
        if (nickname == null || TextUtils.isEmpty(nickname)) {
            nickname = item.getDataModel().getUsername();
        }
        mTvName.setText(nickname);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
        }
    }
}

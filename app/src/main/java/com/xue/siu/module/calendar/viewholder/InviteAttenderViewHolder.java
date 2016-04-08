package com.xue.siu.module.calendar.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.media.FrescoUtil;

/**
 * Created by XUE on 2016/4/7.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_invite_attender_list)
public class InviteAttenderViewHolder extends TRecycleViewHolder<InviteAttenderViewHolder.UserWrapper> implements
        View.OnClickListener {
    private SimpleDraweeView sdvPortrait;
    private TextView tvName;
    private CheckBox cbSelected;
    private View viewContainer;
    private UserWrapper model;

    public InviteAttenderViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        sdvPortrait = findViewById(R.id.invite_attender_portrait_sdv);
        tvName = findViewById(R.id.invite_attender_name_tv);
        cbSelected = findViewById(R.id.invite_attender_cb);
        viewContainer = findViewById(R.id.invite_attender_cb_container);
        viewContainer.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<UserWrapper> item) {
        model = item.getDataModel();
        tvName.setText((String) item.getDataModel().getUser().get(LeanConstants.NICK_NAME));
        int size = ResourcesUtil.getDimenPxSize(R.dimen.fa_list_portrait_size);
        FrescoUtil.setImageUri(sdvPortrait, (String) item.getDataModel().getUser().get(LeanConstants.PORTRAIT), (float) size);
        cbSelected.setChecked(item.getDataModel().isSelected());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_attender_cb:
                model.setSelected(!model.isSelected());
                cbSelected.setChecked(model.isSelected());
                break;
        }
    }

    public static class UserWrapper {
        boolean selected;
        AVUser user;

        public UserWrapper(boolean selected, AVUser user) {
            this.selected = selected;
            this.user = user;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public AVUser getUser() {
            return user;
        }

        public void setUser(AVUser user) {
            this.user = user;
        }
    }
}

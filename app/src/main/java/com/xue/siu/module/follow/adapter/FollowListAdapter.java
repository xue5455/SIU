package com.xue.siu.module.follow.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.module.chat.activity.ChatActivity;
import com.xue.siu.module.follow.model.User;

import java.util.List;

/**
 * Created by XUE on 2016/1/18.
 */
public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.FollowViewHolder> implements View.OnClickListener {
    private Context mContext;
    private SparseArray<User> mUserArray;

    public FollowListAdapter(Context context, SparseArray<User> array) {
        mContext = context;
        mUserArray = array;
    }

    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FollowViewHolder holder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_follow_list, parent, false);
        holder = new FollowViewHolder(view);
        holder.parentView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowViewHolder holder, int position) {
        holder.tvName.setText(mUserArray.get(position).getName());
        holder.dvPortrait.setImageURI(Uri.parse(mUserArray.get(position).getUrl()));
        holder.parentView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mUserArray.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        ChatActivity.start((Activity) mContext, mUserArray.get(position));
    }

    static class FollowViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView dvPortrait;
        private TextView tvName;
        private View parentView;

        public FollowViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            dvPortrait = (SimpleDraweeView) itemView.findViewById(R.id.dv_portrait);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}

package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.xue.siu.R;
import com.xue.siu.module.news.adapter.CommentAdapter;

/**
 * Created by XUE on 2016/3/1.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_news_action_list)
public class NewsActionViewHolder extends TRecycleViewHolder{
    private SimpleDraweeView mSdvAvatar;
    private FrameLayout mLayoutComment;
    private TextView mTvName;
    private TextView mTvTime;
    private TextView mTvContent;
    private TextView mTvLike;
    private Button mBtnComment;
    private Button mBtnLike;
    private CommentAdapter mCommentAdapter;
    private TextView mTvLocation;
    public NewsActionViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
//        mSdvAvatar = findViewById(R.id.sdv_avatar_news_fragment);
        mSdvAvatar = (SimpleDraweeView)findViewById(R.id.sdv_avatar_news_fragment);
        mLayoutComment = (FrameLayout)findViewById(R.id.layout_comment_news_fragment);
        mTvName = (TextView)findViewById(R.id.tv_name_news_fragment);
        mTvLocation = (TextView)findViewById(R.id.tv_location_news_fragment);
        mTvTime = (TextView)findViewById(R.id.tv_time_news_fragment);
        mBtnLike = (Button)findViewById(R.id.btn_like_news_fragment);
        mBtnComment = (Button)findViewById(R.id.btn_comment_news_fragment);
        mTvLike = (TextView)findViewById(R.id.tv_likes_news_fragment);
        mTvContent= (TextView)findViewById(R.id.tv_content_news_fragment);
    }

    @Override
    public void refresh(TAdapterItem item) {

    }
}

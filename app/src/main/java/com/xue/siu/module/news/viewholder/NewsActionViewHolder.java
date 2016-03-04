package com.xue.siu.module.news.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.AdditionalKeys;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.view.asynclist.AsyncGridView;
import com.xue.siu.common.view.asynclist.AsyncListView;
import com.xue.siu.common.view.asynclist.LayoutCacheManager;
import com.xue.siu.module.news.adapter.CommentAdapter;
import com.xue.siu.module.news.adapter.PictureAdapter;
import com.xue.siu.module.news.model.ActionVO;

/**
 * Created by XUE on 2016/3/1.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_news_action_list)
public class NewsActionViewHolder extends TRecycleViewHolder<ActionVO> implements View.OnClickListener {
    private SimpleDraweeView mSdvAvatar;
    private FrameLayout mLayoutComment;
    private TextView mTvName;
    private TextView mTvTime;
    private TextView mTvContent;
    private TextView mTvLike;
    private Button mBtnComment;
    private Button mBtnLike;
    private TextView mTvLocation;
    private FrameLayout mLayoutPic;
    private static final int PIC_SPACE = ResourcesUtil.getDimenPxSize(R.dimen.nf_item_pic_space);

    public NewsActionViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvAvatar = findViewById(R.id.sdv_avatar_news_fragment);
        mLayoutComment = findViewById(R.id.layout_comment_news_fragment);
        mTvName = findViewById(R.id.tv_name_news_fragment);
        mTvLocation = findViewById(R.id.tv_location_news_fragment);
        mTvTime = findViewById(R.id.tv_time_news_fragment);
        mBtnLike = findViewById(R.id.btn_like_news_fragment);
        mBtnComment = findViewById(R.id.btn_comment_news_fragment);
        mTvLike = findViewById(R.id.tv_likes_news_fragment);
        mTvContent = findViewById(R.id.tv_content_news_fragment);
        mLayoutPic = findViewById(R.id.layout_pic_news_fragment);
        mBtnComment.setOnClickListener(this);
        mBtnLike.setOnClickListener(this);
    }

    @Override
    public void refresh(TAdapterItem<ActionVO> item) {
        ActionVO actionVO = item.getDataModel();
        String avatarUrl = (String) actionVO.getCreator().get(AdditionalKeys.KEY_AVATAR);
        Uri uri = avatarUrl == null ? Uri.EMPTY : Uri.parse(avatarUrl);
        mSdvAvatar.setImageURI(uri);
        mTvName.setText(actionVO.getCreator().getUsername());
        mTvTime.setText(String.valueOf(actionVO.getCreatedAt()));
        mTvContent.setText(actionVO.getContent());
        if (mLayoutComment.getTag() == null || !mLayoutComment.getTag().equals(actionVO)) {
            mLayoutComment.removeAllViews();
            mLayoutComment.setTag(actionVO);
            if (LayoutCacheManager.getInstance().contains(actionVO)) {
                mLayoutComment.addView(LayoutCacheManager.getInstance().get(actionVO));
            } else {
                AsyncListView listView = new AsyncListView(context);
                LayoutCacheManager.getInstance().put(actionVO, listView);
                listView.setAdapter(new CommentAdapter(context, actionVO.getCommentList()));
            }
        }
        if (mLayoutPic.getTag() == null || !mLayoutPic.getTag().equals(actionVO)) {
            mLayoutPic.removeAllViews();
            mLayoutPic.setTag(actionVO);
            if (LayoutCacheManager.getInstance().contains(getAdapterPosition())) {
                mLayoutPic.addView(LayoutCacheManager.getInstance().get(getAdapterPosition()));
            } else {
                AsyncGridView gridView = new AsyncGridView(context);
                LayoutCacheManager.getInstance().put(getAdapterPosition(), gridView);
                gridView.setHorizontalSpace(PIC_SPACE);
                gridView.setVerticalSpace(PIC_SPACE);
                gridView.setNumCol(3);
                gridView.setAdapter(new PictureAdapter(context, actionVO.getPicList()));
                mLayoutPic.addView(gridView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
    }
}

package com.xue.siu.module.news.viewholder;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.AdditionalKeys;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.util.collection.CollectionsUtil;
import com.xue.siu.common.util.media.FrescoUtil;
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
    public static final int PIC_SPACE = ResourcesUtil.getDimenPxSize(R.dimen.nf_item_pic_space);
    public static final int PIC_SIZE = (ScreenUtil.getDisplayWidth() -
            ResourcesUtil.getDimenPxSize(R.dimen.nf_item_inner_padding) -
            2 * ResourcesUtil.getDimenPxSize(R.dimen.default_content_padding) -
            2 * PIC_SPACE) / 3;

    private static final int AVATAR_SIZE = ResourcesUtil.getDimenPxSize(R.dimen.nf_avatar_size);

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
        if (avatarUrl != null)
            FrescoUtil.setImageUri(mSdvAvatar, avatarUrl, (float) AVATAR_SIZE);
        else
            mSdvAvatar.setImageURI(Uri.EMPTY);
        mTvName.setText(actionVO.getCreator().getUsername());
        mTvTime.setText(String.valueOf(actionVO.getCreatedAt()));
        mTvContent.setText(actionVO.getContent());
        if (!CollectionsUtil.isEmpty(actionVO.getCommentList()))
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
        if (!CollectionsUtil.isEmpty(actionVO.getPicList()))
            if (mLayoutPic.getTag() == null || !mLayoutPic.getTag().equals(actionVO)) {
                mLayoutPic.removeAllViews();
                mLayoutPic.setTag(actionVO);
                if (LayoutCacheManager.getInstance().contains(getAdapterPosition())) {
                    View view = LayoutCacheManager.getInstance().get(getAdapterPosition());
                    ((ViewGroup) view.getParent()).removeAllViews();
                    mLayoutPic.addView(LayoutCacheManager.getInstance().get(getAdapterPosition()));
                } else {
                    AsyncGridView gridView = new AsyncGridView(context);
                    adaptLayoutHeight(actionVO.getPicList().size(), gridView);
                    LayoutCacheManager.getInstance().put(getAdapterPosition(), gridView);
                    gridView.setHorizontalSpace(PIC_SPACE);
                    gridView.setVerticalSpace(PIC_SPACE);
                    gridView.setNumCol(3);
                    PictureAdapter adapter = new PictureAdapter(context, actionVO.getPicList());
                    adapter.setListener(listener);
                    gridView.setAdapter(adapter);
                    mLayoutPic.addView(gridView);
                }
            }
    }

    private void adaptLayoutHeight(int count, View view) {
        ViewGroup.LayoutParams params;
        if (count <= 1) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            int rows = (int) Math.ceil((float) count / 3);
            int height = rows * PIC_SIZE + (rows - 1) * PIC_SPACE;
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
        view.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
    }
}

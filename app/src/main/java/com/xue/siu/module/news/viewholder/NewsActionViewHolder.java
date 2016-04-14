package com.xue.siu.module.news.viewholder;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolderAnnotation;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.AdditionalKeys;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.util.collection.CollectionsUtil;
import com.xue.siu.common.util.media.FrescoUtil;
import com.xue.siu.common.view.asynclist.AsyncGridView;
import com.xue.siu.common.view.asynclist.AsyncListView;
import com.xue.siu.common.view.asynclist.LayoutCacheManager;
import com.xue.siu.constant.C;
import com.xue.siu.module.news.adapter.CommentAdapter;
import com.xue.siu.module.news.adapter.PictureAdapter;
import com.xue.siu.module.news.model.ActionVO;
import com.xue.siu.module.news.model.CommentVO;
import com.xue.siu.module.news.view.LinkTouchMovementMethod;
import com.xue.siu.module.news.view.TouchableSpan;

import org.w3c.dom.Comment;

import java.util.List;

/**
 * Created by XUE on 2016/3/1.
 */
@TRecycleViewHolderAnnotation(resId = R.layout.item_news_action_list)
public class NewsActionViewHolder extends TRecycleViewHolder<ActionVO> implements View.OnClickListener {
    private SimpleDraweeView mSdvAvatar;
    //    private FrameLayout mLayoutComment;
    private TextView mTvName;
    private TextView mTvTime;
    private TextView mTvContent;
    private TextView mTvLike;
    private Button mBtnComment;
    private Button mBtnLike;
    private TextView mTvLocation;
    private TextView tvComment;
    private FrameLayout mLayoutPic;
    public static final int PIC_SPACE = ResourcesUtil.getDimenPxSize(R.dimen.nf_item_pic_space);
    public static final int PIC_SIZE = (ScreenUtil.getDisplayWidth() -
            ResourcesUtil.getDimenPxSize(R.dimen.nf_item_inner_padding) -
            2 * ResourcesUtil.getDimenPxSize(R.dimen.default_content_padding) -
            2 * PIC_SPACE) / 3;

    private static final int AVATAR_SIZE = ResourcesUtil.getDimenPxSize(R.dimen.nf_avatar_size);

    private ActionVO model;

    public NewsActionViewHolder(View itemView, Context context, RecyclerView recyclerView) {
        super(itemView, context, recyclerView);
    }

    @Override
    public void inflate() {
        mSdvAvatar = findViewById(R.id.sdv_avatar_news_fragment);
//        mLayoutComment = findViewById(R.id.layout_comment_news_fragment);
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
        tvComment = findViewById(R.id.news_fragment_comment_tv);
        tvComment.setMovementMethod(new LinkTouchMovementMethod());
    }

    @Override
    public void refresh(TAdapterItem<ActionVO> item) {
        model = item.getDataModel();
        ActionVO actionVO = item.getDataModel();
        String avatarUrl = (String) actionVO.getCreator().get(AdditionalKeys.KEY_AVATAR);
        String location = actionVO.getLocation();
        mTvLocation.setText(location);
        if (avatarUrl != null)
            FrescoUtil.setImageUri(mSdvAvatar, avatarUrl, (float) AVATAR_SIZE);
        else
            mSdvAvatar.setImageURI(Uri.EMPTY);
        mTvName.setText(actionVO.getCreator().getUsername());
        mTvTime.setText(String.valueOf(actionVO.getCreatedAt()));
        mTvContent.setText(actionVO.getContent());

//        if (!CollectionsUtil.isEmpty(actionVO.getCommentList())) {
//            if (mLayoutComment.getTag() == null || !mLayoutComment.getTag().equals(actionVO)) {
//                mLayoutComment.removeAllViews();
//                mLayoutComment.setTag(actionVO);
//                if (LayoutCacheManager.getInstance().contains(actionVO)) {
//                    ViewGroup parent = (ViewGroup) LayoutCacheManager.getInstance().get(actionVO).getParent();
//                    if (parent != null)
//                        parent.removeView(LayoutCacheManager.getInstance().get(actionVO));
//                    mLayoutComment.addView(LayoutCacheManager.getInstance().get(actionVO));
//                } else {
//                    AsyncListView listView = new AsyncListView(context);
//                    listView.setListener(listener);
//                    LayoutCacheManager.getInstance().put(actionVO, listView);
//                    listView.setAdapter(new CommentAdapter(context, actionVO.getCommentList()));
//                    mLayoutComment.addView(listView);
//                }
//            }
//        } else
//            mLayoutComment.removeAllViews();
        if (!CollectionsUtil.isEmpty(actionVO.getPicList())) {
            if (mLayoutPic.getTag() == null || !mLayoutPic.getTag().equals(actionVO)) {
                mLayoutPic.removeAllViews();
                mLayoutPic.setTag(actionVO);
                if (LayoutCacheManager.getInstance().contains(getAdapterPosition())) {
                    View view = LayoutCacheManager.getInstance().get(getAdapterPosition());
                    if (view.getParent() != null)
                        ((ViewGroup) view.getParent()).removeAllViews();
                    mLayoutPic.addView(LayoutCacheManager.getInstance().get(getAdapterPosition()));
                } else {
                    mLayoutPic.addView(getGridView(actionVO.getPicList()));
                }
                adaptLayoutHeight(actionVO.getPicList().size(), mLayoutPic);
            }
        } else
            mLayoutPic.removeAllViews();
        if (!CollectionsUtil.isEmpty(actionVO.getCommentList())) {
            tvComment.setVisibility(View.VISIBLE);
            tvComment.setText(getComment(actionVO.getCommentList()));
        } else {
            tvComment.setText(C.EMPTY);
            tvComment.setVisibility(View.GONE);
        }
    }

    private View getGridView(List<AVFile> list) {
        AsyncGridView gridView = new AsyncGridView(context);
        LayoutCacheManager.getInstance().put(getAdapterPosition(), gridView);
        gridView.setHorizontalSpace(PIC_SPACE);
        gridView.setVerticalSpace(PIC_SPACE);
        gridView.setNumCol(3);
        PictureAdapter adapter = new PictureAdapter(context, list);
        adapter.setListener(listener);
        gridView.setAdapter(adapter);
        return gridView;
    }

    private void adaptLayoutHeight(int count, View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (count <= 1) {
            params.height = count == 0 ? 0 : PIC_SIZE;
        } else {
            int rows = (int) Math.ceil((float) count / 3);
            int height = rows * PIC_SIZE + (rows - 1) * PIC_SPACE;
            params.height = height;
        }
        view.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onEventNotify(ItemEventListener.clickEventName, v, getAdapterPosition());
    }

    private SpannableStringBuilder getComment(List<CommentVO> commentVOs) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < commentVOs.size(); i++) {
            CommentVO vo = commentVOs.get(i);
            StringBuilder sb = new StringBuilder();
            sb.append(vo.getFrom().get(LeanConstants.NICK_NAME).toString());
            if (vo.getTo() != null) {
                sb.append("回复");
                sb.append(vo.getTo().get(LeanConstants.NICK_NAME).toString());
            }
            sb.append(C.COLON);
            sb.append(vo.getContent());
            SpannableString ss = new SpannableString(sb);
            /* 设置点击文本事件 */
            ss.setSpan(new CommentSpan(vo), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            /* 更改回复人名字颜色 */
            ss.setSpan(new ForegroundColorSpan(ResourcesUtil.getColor(R.color.green_normal)), 0,
                    vo.getFrom().get(LeanConstants.NICK_NAME).toString().length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            /* 更改被回复人名字颜色 */
            if (vo.getTo() != null) {
                int start = vo.getFrom().get(LeanConstants.NICK_NAME).toString().length() + 2;
                int end = start + vo.getTo().get(LeanConstants.NICK_NAME).toString().length();
                ss.setSpan(new ForegroundColorSpan(
                                ResourcesUtil.getColor(R.color.green_normal)),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            ssb.append(ss);
            if (i != commentVOs.size() - 1) {
                int start = ssb.length();
                ssb.append(C.RETURN);
                ssb.append(C.RETURN);
                int end = ssb.length();
                ssb.setSpan(new AbsoluteSizeSpan(5), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ssb;
    }

    private class CommentSpan extends TouchableSpan {
        private CommentVO commentVO;

        public CommentSpan(CommentVO commentVO) {
            super(ResourcesUtil.getColor(R.color.black_alpha80), ResourcesUtil.getColor(R.color.black_alpha80),
                    ResourcesUtil.getColor(R.color.black_alpha20));
            this.commentVO = commentVO;
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onEventNotify(ItemEventListener.clickEventName, widget, getAdapterPosition(), commentVO);
            }
        }
    }

}

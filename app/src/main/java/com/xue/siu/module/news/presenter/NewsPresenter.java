package com.xue.siu.module.news.presenter;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.eventbus.HTEventBus;
import com.xue.siu.R;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.chat.adapter.FaceVpAdapter;
import com.xue.siu.module.news.activity.NewsActivity;
import com.xue.siu.module.news.activity.PublishActivity;
import com.xue.siu.module.news.callback.SaveCommentCallback;
import com.xue.siu.module.news.model.ActionVO;
import com.xue.siu.module.news.model.CommentEvent;
import com.xue.siu.module.news.model.CommentVO;

import java.util.HashMap;

/**
 * Created by XUE on 2016/2/15.
 */
public class NewsPresenter extends BaseActivityPresenter<NewsActivity> implements ViewPager.
        OnPageChangeListener, View.OnClickListener, ItemEventListener, NewsEventListener,
        ScreenObserver.OnScreenHeightChangedListener,
        AVIMResultListener {

    /* Keyboard height */
    int mKeyboardHeight = ResourcesUtil.getDimenPxSize(R.dimen.chat_menu_height);

    /* Face Adapter */
    private FaceVpAdapter mFaceVpAdapter;

    /* is emoji button clicked */
    private boolean mEmoji;
    /* is menu button clicked */
    private boolean mMenu;
    /* is list dragged */
    private boolean mDrag;
    /* is input method showing */
    private boolean mIsIMVisible = false;

    private ActionVO currentActVO;
    private AVUser toUser;

    public NewsPresenter(NewsActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        int viewPagerHeight = SharePreferenceHelper.getGlobalInt(SharePreferenceC.KB_HEIGHT, 0);
        mFaceVpAdapter = new FaceVpAdapter(mTarget, viewPagerHeight == 0
                ? getFaceVpHeight(mKeyboardHeight)
                : getFaceVpHeight(viewPagerHeight));
        mFaceVpAdapter.setItemEventListener(this);
        mTarget.setEmojiAdapter(mFaceVpAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTarget.updateText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_action:
                mTarget.setCurrentItem(0);
                break;
            case R.id.tv_calendar:
                mTarget.setCurrentItem(1);
                break;
            case R.id.btn_publish_news:
                PublishActivity.startForResult(mTarget, mTarget.REQUEST_CODE);
                break;
            case R.id.btn_emoji:
                if (!mTarget.isEmojiVisible() && !mIsIMVisible) {
                    mTarget.setEmojiVisibility(true);
                    return;
                }
                mEmoji = true;
                showOrCloseInput();
                break;
            case R.id.btn_send:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        String content = mTarget.getCommentContent();
        /* 内容为空，则不响应 */
        if (TextUtils.isEmpty(content)) {
            return;
        }
        DialogUtil.showProgressDialog(mTarget, false);
        CommentVO commentVO = new CommentVO();
        commentVO.setFrom(AVUser.getCurrentUser());
        commentVO.setTo(toUser);
        commentVO.setContent(mTarget.getCommentContent());
        commentVO.toAVObject(currentActVO.getObjectId()).saveInBackground(new
                SaveCommentCallback(this).getCallback());
    }

    private void showOrCloseInput() {
        mTarget.setInputMethodVisibility(!mIsIMVisible);
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (view.getId()) {
            case R.id.btn_photo_publish:
                break;
        }
        return false;
    }


    @Override
    public void showInput(ActionVO actionVO, AVUser to) {
        currentActVO = actionVO;
        mTarget.setInputViewVisibility(true);
        mTarget.setHint(to == null ? "" : (String) to.get(LeanConstants.NICK_NAME));
        toUser = to;
    }

    @Override
    public void hideInput() {
        mTarget.setInputViewVisibility(false);
    }

    @Override
    public void onSizeChanged(boolean bigger, int newHeight, int oldHeight) {
        mIsIMVisible = !bigger;
        if (bigger) {
            //indicates that the input method is closed,reason could be either emoji and menu are gonna showed or list is touched;
            if (mEmoji) {
                /* Emoji is gonna showed */
                mEmoji = false;
                mTarget.setEmojiVisibility(true);
            } else {
                /* list is touched,both emoji and menu should be closed */
                mTarget.setEmojiVisibility(false);
            }
        } else {
            //indicates that the input method is showed
            /* both emoji and menu should be closed */
            mTarget.setEmojiVisibility(false);
            mEmoji = false;
        }
        if (oldHeight != 0)
            updateMenuHeight(newHeight, oldHeight);
    }

    private void updateMenuHeight(int newHeight, int oldHeight) {
        int kbHeight = Math.abs(newHeight - oldHeight);
        if (kbHeight != mKeyboardHeight) {
            SharePreferenceHelper.putGlobalInt(SharePreferenceC.KB_HEIGHT, kbHeight);
            mTarget.setEmojiVpHeight(kbHeight);
            mKeyboardHeight = kbHeight;
            invalidateEmoji();
        }
    }

    public void invalidateEmoji() {
        mFaceVpAdapter.setHeight(getFaceVpHeight(mKeyboardHeight));
        mFaceVpAdapter.notifyDataSetChanged();
    }

    /**
     * @param height : this height is the height of keyboard
     * @return
     */
    private int getFaceVpHeight(int height) {
        return height - 2 * ResourcesUtil.getDimenPxSize(R.dimen.chat_vp_vertical_padding)
                - ResourcesUtil.getDimenPxSize(R.dimen.chat_indicator_margin_bottom)
                - ResourcesUtil.getDimenPxSize(R.dimen.chat_indicator_height);
    }

    @Override
    public void onLeanError(String cbName, AVException e) {
        if (TextUtils.equals(cbName, SaveCommentCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            ToastUtil.makeShortToast("提交评论失败");
        }
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        if (TextUtils.equals(cbName, SaveCommentCallback.class.getName())) {
            DialogUtil.hideProgressDialog(mTarget);
            ToastUtil.makeShortToast("评论成功");
            mTarget.setInputViewVisibility(false);
            HTEventBus.getDefault().post(new CommentEvent());
            currentActVO = null;
            toUser = null;
        }
    }
}

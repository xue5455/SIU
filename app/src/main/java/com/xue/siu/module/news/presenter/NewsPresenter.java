package com.xue.siu.module.news.presenter;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.chat.adapter.FaceVpAdapter;
import com.xue.siu.module.news.activity.NewsActivity;
import com.xue.siu.module.news.activity.PublishActivity;
import com.xue.siu.module.news.model.ActionVO;

import java.util.HashMap;

/**
 * Created by XUE on 2016/2/15.
 */
public class NewsPresenter extends BaseActivityPresenter<NewsActivity> implements ViewPager.
        OnPageChangeListener, View.OnClickListener, ItemEventListener, NewsEventListener,
        ScreenObserver.OnScreenHeightChangedListener {

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
        }
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
        mTarget.setInputViewVisibility(true);
        mTarget.setHint("回复:XXJ");
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

}

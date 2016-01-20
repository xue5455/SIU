package com.xue.siu.module.chat.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.config.UserInfo;
import com.xue.siu.db.MySharePreferenceHelper;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.chat.activity.ChatActivity;

import java.util.Arrays;


/**
 * Created by XUE on 2015/12/10.
 */
public class ChatPresenter extends BaseActivityPresenter<ChatActivity> implements View.OnClickListener,
        TextWatcher, View.OnTouchListener, ScreenObserver.OnScreenHeightChangedListener {

    private static final String TAG = "ChatPresenter";
    boolean mIsMenuClicked = false;
    boolean mIsEmojiClicked = false;
    boolean mIsIMVisible = false;
    int mKbHeight = 0;
    private String mFormat = "%1$s & %2$s";
    private String mChatName;
    private AVIMConversationCreatedCallback mAVIMCallback = new AVIMConversationCreatedCallback() {
        @Override
        public void done(AVIMConversation avimConversation, AVIMException e) {
            if (e == null) {
                mAnimConversation = avimConversation;
            } else {
                LogUtil.e(TAG, "[create conversation] fails,try to create again");
                openConversation();
            }
        }
    };
    private AVIMClientCallback mAVIMClientCallback = new AVIMClientCallback() {
        @Override
        public void done(AVIMClient avimClient, AVIMException e) {
            if (e == null) {
                avimClient.createConversation(Arrays.asList(mTarget.getUserId()),
                        mChatName, null, mAVIMCallback);
            } else {
                LogUtil.e(TAG, "[open client] fails,try to open again");
                openConversation();
            }
        }
    };
    private AVIMConversation mAnimConversation;

    public ChatPresenter(ChatActivity target) {
        super(target);
    }

    @Override
    protected void initActivity() {
        mChatName = String.format("%1$s & %2$s", new Object[]{UserInfo.userId, mTarget.getUserId()});
        openConversation();
    }

    private void openConversation() {
        AVIMClient tom = AVIMClient.getInstance(UserInfo.userId);
        tom.open(mAVIMClientCallback);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.emoji_btn:
                onEmojiClick();
                break;
            case R.id.plus_btn:
                onPlusClick();
                break;
            case R.id.send_btn:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = s.toString().length();
        if (length > 0) {
            mTarget.showSendBtn();
        } else {
            mTarget.hideSendBtn();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.msg_recycler_view:
                return onRecyclerTouch(event);
            case R.id.msg_edit:
                return onMsgEditTouch(event);
        }
        return false;
    }

    private boolean onRecyclerTouch(MotionEvent event) {
        //当点击或者移动消息列表时，关闭输入法、表情、plus菜单
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mIsIMVisible)
                mTarget.shutInputMethod();
            else if (mTarget.isEmojiVisible() || mTarget.isPMenuVisible())
                mTarget.shutMenuAndEmoji();
        }
        return false;
    }

    private boolean onMsgEditTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTarget.showInputMethod();
        }
        return false;
    }

    private void onEmojiClick() {
        mIsEmojiClicked = true;
        if (mIsIMVisible) {
            //输入法已经打开,则关闭输入法
            mTarget.shutInputMethod();
        } else {
            //输入法没打开，判断表情页面或者菜单是否打开
            if (mTarget.isEmojiVisible()) {
                //表情页面已打开，则开启输入法
                mTarget.showInputMethod();

                return;
            }
            if (mTarget.isPMenuVisible()) {
                //菜单已经打开，则开启表情页，关闭菜单
                mTarget.showEmojiMenu();
                mTarget.shutMenu();
                mIsEmojiClicked = false;
                return;
            }
            //都没打开，则打开表情
            mTarget.showEmojiMenu();
            mIsEmojiClicked = false;
        }
    }

    private void onPlusClick() {
        mIsMenuClicked = true;
        if (mIsIMVisible) {
            //输入法已经打开,则关闭输入法
            mTarget.shutInputMethod();
        } else {
            //输入法没打开，判断表情页面或者菜单是否打开
            if (mTarget.isPMenuVisible()) {
                //菜单页面已打开，则开启输入法
                mTarget.showInputMethod();
                return;
            }
            if (mTarget.isEmojiVisible()) {
                //表情页面已经打开，则开启菜单，关闭表情页面
                mTarget.showPlusMenu();
                mTarget.shutEmoji();
                mIsMenuClicked = false;
                return;
            }
            mTarget.showPlusMenu();
            mIsMenuClicked = false;
        }
    }

    private void onSendClick(){

    }
    @Override
    public void onSizedChanged(boolean bigger, int newHeight, int oldHeight) {
        mIsIMVisible = !bigger;
        if (mIsMenuClicked || mIsEmojiClicked) {
            if (mIsMenuClicked) {
                mIsMenuClicked = false;
                if (bigger) {
                    //点击plus按钮后，变大，则输入法被隐藏，此时需要打开菜单
                    mTarget.showPlusMenu();
                } else {
                    //点击plus按钮后，变小，则输入法被打开，此时需要关闭菜单
                    mTarget.shutMenu();
                }
                return;
            }
            if (mIsEmojiClicked) {
                mIsEmojiClicked = false;
                if (bigger) {
                    //点击EMOJI按钮后，变大，则输入法被隐藏，此时需要打开表情
                    mTarget.showEmojiMenu();
                } else {
                    //点击EMOJI按钮后，变小，则输入法被打开，此时需要关闭表情
                    mTarget.shutEmoji();
                }
            }
        } else {
            //界面的变化是由点击列表和输入框引起的
            LogUtil.d("xue", "caused by touch edit_Text");
            if (mTarget.isPMenuVisible() || mTarget.isEmojiVisible())
                mTarget.shutMenuAndEmoji();
        }


        int kbHeight = Math.abs(newHeight - oldHeight);
        if (kbHeight != mKbHeight && !bigger) {
            SharePreferenceHelper.putGlobalInt(SharePreferenceC.KB_HEIGHT, kbHeight);
            mTarget.updateContainerHeight(kbHeight);
            mKbHeight = kbHeight;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mKbHeight = SharePreferenceHelper.getGlobalInt(SharePreferenceC.KB_HEIGHT, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTarget.shutInputMethod();
    }
}

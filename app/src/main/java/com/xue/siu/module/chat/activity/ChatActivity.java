package com.xue.siu.module.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.util.KeyboardUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.view.viewpager.DotViewPagerIndicator;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.chat.adapter.FaceVpAdapter;
import com.xue.siu.module.chat.presenter.ChatPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2015/12/10.
 */
public class ChatActivity extends BaseActionBarActivity<ChatPresenter> {
    @Bind(R.id.rv_msg)
    HTSwipeRecyclerView mRvMsg;
    @Bind(R.id.vp_emoji)
    ViewPager mVpEmoji;
    @Bind(R.id.gv_plus)
    GridView mGvMenu;
    @Bind(R.id.btn_emoji)
    Button mBtnEmoji;
    @Bind(R.id.btn_plus)
    Button mBtnMenu;
    @Bind(R.id.btn_send)
    Button mBtnSend;
    @Bind(R.id.et_msg)
    EditText mEtMsg;
    @Bind(R.id.layout_face)
    View mContainerFace;
    @Bind(R.id.indicator_dot)
    DotViewPagerIndicator mDotIndicator;

    public static final String INTENT_KEYS_USER = "user";
    public static final String INTENT_KEYS_CONVERSATION_ID = "conversationId";


    public static void start(Activity activity, AVUser userVO) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(INTENT_KEYS_CONVERSATION_ID, "");
        intent.putExtra(INTENT_KEYS_USER, userVO);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, AVUser user, String conversationId) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(INTENT_KEYS_CONVERSATION_ID, conversationId);
        intent.putExtra(INTENT_KEYS_USER, user);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initViews();
        ScreenObserver.assistActivity(this, mPresenter);
        setStatueBarColor(R.color.action_bar_bg);
    }

    private void initViews() {
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(R.color.chat_bg));
        setNavigationBarBlack();
        setStatueBarColor(R.color.black);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvMsg.setLayoutManager(layoutManager);
        mRvMsg.addOnScrollListener(mPresenter);
        mBtnEmoji.setOnClickListener(mPresenter);
        mBtnSend.setOnClickListener(mPresenter);
        mBtnMenu.setOnClickListener(mPresenter);
        mRvMsg.setOnLayoutSizeChangedListener(mPresenter);
        mEtMsg.addTextChangedListener(mPresenter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChatPresenter(this);
    }


    public void setEmojiVisibility(boolean show) {
        mContainerFace.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setMenuVisibility(boolean show) {
        mGvMenu.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public boolean isEmojiVisible() {
        return mContainerFace.getVisibility() == View.VISIBLE;
    }

    public boolean isMenuVisible() {
        return mGvMenu.getVisibility() == View.VISIBLE;
    }

    public void setInputMethodVisibility(boolean show) {
        if (show) {
            mEtMsg.requestFocus();
            KeyboardUtil.showkeyboard(mEtMsg);
        } else
            KeyboardUtil.hidekeyboard(mEtMsg);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvMsg.setAdapter(adapter);
    }

    public void setEmojiAdapter(PagerAdapter adapter) {
        mVpEmoji.setAdapter(adapter);
        mDotIndicator.setAdapter(adapter);
        mVpEmoji.addOnPageChangeListener(mDotIndicator);
    }

    public void setMenuHeight(int height) {
        int faceHeight = height - ResourcesUtil.getDimenPxSize(R.dimen.chat_indicator_height)
                - ResourcesUtil.getDimenPxSize(R.dimen.chat_indicator_margin_bottom);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mGvMenu.getLayoutParams();
        params.height = height;
        mGvMenu.invalidate();
        LinearLayout.LayoutParams faceParams = (LinearLayout.LayoutParams) mVpEmoji.getLayoutParams();
        faceParams.height = faceHeight;
        mVpEmoji.invalidate();
    }

    public void scrollToBottom(boolean smooth) {
        if (!smooth)
            mRvMsg.getRecyclerView().scrollToPosition(mRvMsg.getRecyclerView().getLayoutManager().getItemCount() - 1);
        else
            mRvMsg.getRecyclerView().smoothScrollToPosition(mRvMsg.getRecyclerView().getLayoutManager().getItemCount() - 1);
    }

    public String getContent() {
        return mEtMsg.getText().toString();
    }

    public void setSendButtonVisibility(boolean show) {
        mBtnSend.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void clearMsg() {
        mEtMsg.setText("");
    }

    @Override
    public void onBackPressed() {
        if (isMenuVisible()) {
            setMenuVisibility(false);
            return;
        }
        if (isEmojiVisible()) {
            setEmojiVisibility(false);
            return;
        }
        super.onBackPressed();
    }

    public void addText(SpannableString spannableString) {
        int index = mEtMsg.getSelectionStart();
        Editable edit_text = mEtMsg.getEditableText();
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            edit_text.insert(index, spannableString);
        }
    }

    /**
     * 模拟退格键
     */
    public void backspace() {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
                0, KeyEvent.KEYCODE_ENDCALL);
        mEtMsg.dispatchKeyEvent(event);
    }
}

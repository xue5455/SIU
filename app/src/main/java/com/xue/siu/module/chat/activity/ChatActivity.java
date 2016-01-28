package com.xue.siu.module.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.avos.avoscloud.AVUser;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.avim.model.LeanUser;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.util.KeyboardUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.chat.presenter.ChatPresenter;
import com.xue.siu.module.chat.view.EditContainer;
import com.xue.siu.module.follow.model.UserVO;

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
    public static final String INTENT_KEYS_USER = "user";
    public static final String INTENT_KEYS_CONVERSATION_ID = "conversationId";


    public static void start(Activity activity, AVUser userVO) {
        Intent intent = new Intent(activity, ChatActivity.class);
        // intent.putExtra(INTENT_KEYS_USER, userVO.toBundle());
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

    }

    private void initViews() {
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(R.color.chat_bg));
        setNavigationBarBlack();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvMsg.setLayoutManager(layoutManager);
        mRvMsg.addOnScrollListener(mPresenter);
        mBtnEmoji.setOnClickListener(mPresenter);
        mBtnSend.setOnClickListener(mPresenter);
        mBtnMenu.setOnClickListener(mPresenter);
        mRvMsg.getRecyclerView().setOnClickListener(mPresenter);
        mEtMsg.addTextChangedListener(mPresenter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChatPresenter(this);
    }


    public void setEmojiVisibility(boolean show) {
        mVpEmoji.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setMenuVisibility(boolean show) {
        mGvMenu.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public boolean isEmojiVisible() {
        return mVpEmoji.getVisibility() == View.VISIBLE;
    }

    public boolean isMenuVisible() {
        return mGvMenu.getVisibility() == View.VISIBLE;
    }

    public void setInputMethodVisibility(boolean show) {
        if (show)
            KeyboardUtil.showkeyboard(mEtMsg);
        else
            KeyboardUtil.hidekeyboard(mEtMsg);
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvMsg.setAdapter(adapter);
    }

    public void setMenuHeight(int height) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mGvMenu.getLayoutParams();
        params.height = height;
        LogUtil.d("ChatPresenter","height is " + height);
        mGvMenu.setLayoutParams(params);
        mVpEmoji.setLayoutParams(params);
        mGvMenu.invalidate();
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
}

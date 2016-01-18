package com.xue.siu.module.chat.activity;

import android.app.ActionBar;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.util.KeyboardUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.chat.presenter.ChatPresenter;

/**
 * Created by XUE on 2015/12/10.
 */
public class ChatActivity extends BaseActionBarActivity<ChatPresenter> {
    private RecyclerView mMsgRcv;
    private Button mSendBtn;
    private Button mEmojiBtn;
    private Button mPlusBtn;
    private EditText mMsgEdit;
    private ViewGroup mEmojiContainer;//表情菜单
    private ViewGroup mPlusMenuContainer;//plus 菜单
    private ViewPager mEmojiPager;//表情的ViewPager;
    private GridView mPlusGv;//Plus菜单的GridView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_chat);
        initViews();
        ScreenObserver.assistActivity(this, mPresenter);
    }

    private void initViews() {
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(R.color.chat_bg));
        setTitle("聊天界面");
        setNavigationBarBlack();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMsgRcv = findView(R.id.msg_recycler_view);
        mMsgRcv.setLayoutManager(layoutManager);
        mMsgRcv.setOnTouchListener(mPresenter);
        mMsgEdit = findView(R.id.msg_edit);
        mMsgEdit.addTextChangedListener(mPresenter);
        mMsgEdit.setOnTouchListener(mPresenter);
        mSendBtn = findView(R.id.send_btn);
        mSendBtn.setOnClickListener(mPresenter);
        mEmojiBtn = findView(R.id.emoji_btn);
        mEmojiBtn.setOnClickListener(mPresenter);
        mPlusBtn = findView(R.id.plus_btn);
        mPlusBtn.setOnClickListener(mPresenter);
        mPlusMenuContainer = findView(R.id.chat_menu_root_view);
        mEmojiContainer = findView(R.id.chat_emoji_root_view);
        KeyboardView view = new KeyboardView(this, null);
        LogUtil.d("xue", String.valueOf(view.getHeight()));
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChatPresenter(this);
    }


    public void shutInputMethod() {

        KeyboardUtil.hidekeyboard(mMsgEdit);
    }

    public void showInputMethod() {

        mMsgEdit.requestFocus();
        KeyboardUtil.showkeyboard(mMsgEdit);
    }

    public void shutMenuAndEmoji() {
        mEmojiContainer.setVisibility(View.GONE);
        mPlusMenuContainer.setVisibility(View.GONE);
        LogUtil.d("xue","shutMenuAndEmoji");
    }

    public void shutMenu() {
        mPlusMenuContainer.setVisibility(View.GONE);
    }

    public void shutEmoji() {
        mEmojiContainer.setVisibility(View.GONE);
    }

    public void showPlusMenu() {
        mPlusMenuContainer.setVisibility(View.VISIBLE);
        mEmojiContainer.setVisibility(View.GONE);

    }

    public void showEmojiMenu() {
        mEmojiContainer.setVisibility(View.VISIBLE);
        mPlusMenuContainer.setVisibility(View.GONE);
    }

    public void showSendBtn() {
        mSendBtn.setVisibility(View.VISIBLE);
    }

    public void hideSendBtn() {
        mSendBtn.setVisibility(View.GONE);
    }

    public boolean isEmojiVisible() {
        return mEmojiContainer.getVisibility() == View.VISIBLE;
    }

    public boolean isPMenuVisible() {
        return mPlusMenuContainer.getVisibility() == View.VISIBLE;
    }

    /**
     * 更新表情框和菜单的高度
     *
     * @param height
     */
    public void updateContainerHeight(int height) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mEmojiContainer.getLayoutParams();
        params.height = height;
        mEmojiContainer.setLayoutParams(params);
        mPlusMenuContainer.setLayoutParams(params);
    }
}

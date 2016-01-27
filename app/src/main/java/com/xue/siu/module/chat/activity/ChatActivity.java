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
    @Bind(R.id.ec_view)
    EditContainer mEditContainer;
    public static final String INTENT_KEYS_USER = "user";
    public static final String INTENT_KEYS_CONVERSATION_ID = "conversationId";
    private String mUrl;//头像地址
    private String mName;//名字
    private String mUserId;//id
    private AVUser mUser;
    private String mConversationId;

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
        mUser = getIntent().getParcelableExtra(INTENT_KEYS_USER);
        mConversationId = getIntent().getStringExtra(INTENT_KEYS_CONVERSATION_ID);
        if (mUser != null) {
            setTitle(mUser.getUsername());
        }
    }

    private void initViews() {
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(R.color.chat_bg));
        setNavigationBarBlack();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvMsg.setLayoutManager(layoutManager);
        mRvMsg.getRecyclerView().setOnTouchListener(mPresenter);
        mEditContainer.setOnClickListener(mPresenter);
        mEditContainer.setOnTouchListener(mPresenter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChatPresenter(this);
    }


    public void shutInputMethod() {
        mEditContainer.hideKeyboard();
    }

    public void showInputMethod() {
        mEditContainer.showInputMethod();
    }

    public void shutMenuAndEmoji() {
        mEditContainer.hideMenuAndEmoji();
    }

    public void shutMenu() {
        mEditContainer.hideMenu();
    }

    public void shutEmoji() {
        mEditContainer.hideEmoji();
    }

    public void showPlusMenu() {
        mEditContainer.showAdditionalMenu();
    }

    public void showEmojiMenu() {
//        mEmojiContainer.setVisibility(View.VISIBLE);
//        mPlusMenuContainer.setVisibility(View.GONE);
        mEditContainer.showEmoji();
    }

    public boolean isEmojiVisible() {
        return mEditContainer.isEmojiVisible();
    }

    public boolean isPMenuVisible() {
        return mEditContainer.isMenuVisible();
    }

    /**
     * 更新表情框和菜单的高度
     *
     * @param height
     */
    public void updateContainerHeight(int height) {
        mEditContainer.updateMenuHeight(height);
    }

    public String getMsgContent() {
        return mEditContainer.getContent();
    }

    public String getUrl() {
        return mUrl;
    }

    public String getUserId() {
        return mUser.getUsername();
    }

    public String getName() {
        return mName;
    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvMsg.setAdapter(adapter);
    }

    public AVUser getUser() {
        return mUser;
    }

    public String getConversationId() {
        return mConversationId == null ? "" : mConversationId;
    }
}

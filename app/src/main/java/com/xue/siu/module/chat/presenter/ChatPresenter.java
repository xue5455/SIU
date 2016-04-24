package com.xue.siu.module.chat.presenter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.TextureView;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.application.AppProfile;
import com.xue.siu.avim.AVIMClientManager;
import com.xue.siu.avim.ActivityMessageHandler;
import com.xue.siu.avim.LeanConstants;
import com.xue.siu.common.util.EmojiUtil;
import com.xue.siu.common.util.HandleUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.MessageUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.util.StaticLayoutManager;
import com.xue.siu.common.util.TextUtil;
import com.xue.siu.common.util.ThreadUtil;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.db.bean.User;
import com.xue.siu.db.dao.SIUMessageDao;
import com.xue.siu.db.dao.UserDao;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.chat.activity.ChatActivity;
import com.xue.siu.module.chat.adapter.FaceVpAdapter;
import com.xue.siu.module.chat.listener.OnRcvMessageListener;
import com.xue.siu.module.chat.viewholder.ImageMsgInViewHolder;
import com.xue.siu.module.chat.viewholder.ImageMsgOutViewHolder;
import com.xue.siu.module.chat.viewholder.TextMsgInViewHolder;
import com.xue.siu.module.chat.viewholder.TextMsgOutViewHolder;
import com.xue.siu.module.chat.viewholder.item.MsgViewHolderItem;
import com.xue.siu.module.chat.viewholder.item.ItemType;
import com.xue.siu.module.chat.viewholder.item.MessageUserWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by XUE on 2015/12/10.
 */
public class ChatPresenter extends BaseActivityPresenter<ChatActivity> implements
        View.OnClickListener,
        OnRcvMessageListener,
        ItemEventListener, HTSwipeRecyclerView.OnScrollListener,
        ScreenObserver.OnScreenHeightChangedListener,
        TextWatcher, HTSwipeRecyclerView.OnLayoutSizeChangedListener {

    private static final String TAG = "ChatPresenter";
    /* is emoji button clicked */
    private boolean mEmoji;
    /* is menu button clicked */
    private boolean mMenu;
    /* is list dragged */
    private boolean mDrag;
    /* is input method showing */
    private boolean mIsIMVisible = false;
    /* Keyboard height */
    int mKeyboardHeight = ResourcesUtil.getDimenPxSize(R.dimen.chat_menu_height);
    private UserDao mUserDao;
    private SIUMessageDao mMsgDao;
    /* 聊天相关 */
    private AVIMConversationCreatedCallback mAVIMCallback = new AVIMConversationCreatedCallback() {
        @Override
        public void done(AVIMConversation avimConversation, AVIMException e) {
            if (e == null) {
                mAnimConversation = avimConversation;
                mUserDao.saveConversationId(mUser, avimConversation.getConversationId());
                if (!mQueried)
                    queryMessage(avimConversation.getConversationId());
                LogUtil.i(TAG, "[create conversation] success");
            } else {
                LogUtil.i(TAG, "[create conversation] fails " + e.getMessage());
                e.printStackTrace();
                openConversation();
            }
        }
    };
    /* The person i'm chatting with */
    private AVUser mUser;

    private String mConversationId;
    /* Indicates whether we have queried messages from database */
    private boolean mQueried = false;
    private AVIMConversation mAnimConversation;

    private List<TAdapterItem<MessageUserWrapper>> mList = new ArrayList<>();
    /* Message List Adapter */
    private TRecycleViewAdapter mAdapter;
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();
    /* Message handler which handles received messages */
    private AVIMMessageHandler mMsgHandler;
    /* Face Adapter */
    private FaceVpAdapter mFaceVpAdapter;

    public ChatPresenter(ChatActivity target) {
        super(target);
        mViewHolders.put(ItemType.TYPE_IMAGE_IN, ImageMsgInViewHolder.class);
        mViewHolders.put(ItemType.TYPE_IMAGE_OUT, ImageMsgOutViewHolder.class);
        mViewHolders.put(ItemType.TYPE_TEXT_OUT, TextMsgOutViewHolder.class);
        mViewHolders.put(ItemType.TYPE_TEXT_IN, TextMsgInViewHolder.class);

    }

    @Override
    protected void initActivity() {
        mUser = mTarget.getIntent().getParcelableExtra(mTarget.INTENT_KEYS_USER);
        mConversationId = mTarget.getIntent().getStringExtra(mTarget.INTENT_KEYS_CONVERSATION_ID);
        if (mUser != null) {
            mTarget.setTitle((String) mUser.get(LeanConstants.NICK_NAME));
        }
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mList);
        mAdapter.setItemEventListener(this);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mTarget.scrollToBottom(false);
            }
        });
        mTarget.setAdapter(mAdapter);
        mMsgHandler = new ActivityMessageHandler(mTarget, mUser.getUsername(), this);
        mUserDao = new UserDao(mTarget.getApplicationContext());
        mMsgDao = new SIUMessageDao(mTarget.getApplicationContext());
        openConversation();
        int viewPagerHeight = SharePreferenceHelper.getGlobalInt(SharePreferenceC.KB_HEIGHT, 0);
        mFaceVpAdapter = new FaceVpAdapter(mTarget, viewPagerHeight == 0
                ? getFaceVpHeight(mKeyboardHeight)
                : getFaceVpHeight(viewPagerHeight));
        mFaceVpAdapter.setItemEventListener(this);
        mTarget.setEmojiAdapter(mFaceVpAdapter);
    }

    private void openConversation() {
        AVIMClient client = AVIMClientManager.getInstance().getClient();
        if (!TextUtils.isEmpty(mConversationId)) {
            mAnimConversation = client.getConversation(mConversationId);
            if (mAnimConversation != null) {
                return;
            }
        } else {
            User user = mUserDao.query(mUser.getUsername());
            if (user != null) {
                mConversationId = user.getConversationId();
                mQueried = true;
                queryMessage(mConversationId);
                mAnimConversation = client.getConversation(mConversationId);
                if (mAnimConversation != null)
                    return;
            }
        }

        client.createConversation(Arrays.asList(mUser.getUsername()), "test", null, false, true, mAVIMCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_emoji:
                onEmojiClick();
                break;
            case R.id.btn_plus:
                onMenuClick();
                break;
            case R.id.btn_send:
                onSendClick();
                break;
        }
    }

    private void onSendClick() {
        String content = mTarget.getContent();
        AVIMTextMessage message = MessageUtil.convertStrToMessage(content);
        mAnimConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    LogUtil.d(TAG, "message send success");
                }
            }
        });
        SIUMessage message1 = MessageUtil.convertSiuToAVIMMsg(mAnimConversation.getConversationId(), mUser.getUsername(), message);
        addMessageToList(message1);
        addMsgToDB(message1);
        mTarget.clearMsg();
    }

    private void onEmojiClick() {
        if (mTarget.isMenuVisible()) {
            mTarget.setEmojiVisibility(true);
            mTarget.setMenuVisibility(false);
            return;
        } else if (!mTarget.isEmojiVisible() && !mIsIMVisible) {
            mTarget.setEmojiVisibility(true);
            return;
        }
        mEmoji = true;
        showOrCloseInput();
    }

    private void onMenuClick() {
        if (mTarget.isEmojiVisible()) {
            mTarget.setMenuVisibility(true);
            mTarget.setEmojiVisibility(false);
            return;
        } else if (!mTarget.isMenuVisible() && !mIsIMVisible) {
            mTarget.setMenuVisibility(true);
            return;
        }
        mMenu = true;
        showOrCloseInput();
    }

    private void showOrCloseInput() {
        mTarget.setInputMethodVisibility(!mIsIMVisible);
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
            } else if (mMenu) {
                mMenu = false;
                mTarget.setMenuVisibility(true);
            } else {
                /* list is touched,both emoji and menu should be closed */
                mTarget.setEmojiVisibility(false);
                mTarget.setMenuVisibility(false);
            }
        } else {
            //indicates that the input method is showed
            /* both emoji and menu should be closed */
            mTarget.setEmojiVisibility(false);
            mTarget.setMenuVisibility(false);
            mEmoji = false;
            mMenu = false;
        }
        if (oldHeight != 0)
            updateMenuHeight(newHeight, oldHeight);
    }

    private void updateMenuHeight(int newHeight, int oldHeight) {

        int kbHeight = Math.abs(newHeight - oldHeight);
        if (kbHeight != mKeyboardHeight) {
            SharePreferenceHelper.putGlobalInt(SharePreferenceC.KB_HEIGHT, kbHeight);
            mTarget.setMenuHeight(kbHeight);
            mKeyboardHeight = kbHeight;
            invalidateEmoji();
        }
    }

    /**
     * onResume要查询一次数据
     */
    @Override
    public void onResume() {
        super.onResume();
        int height = SharePreferenceHelper.getGlobalInt(SharePreferenceC.KB_HEIGHT, 0);
        if (height != 0) {
            mKeyboardHeight = height;
            mTarget.setMenuHeight(mKeyboardHeight);
            invalidateEmoji();
        }
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, mMsgHandler);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTarget.setInputMethodVisibility(false);
        AVIMMessageManager.unregisterMessageHandler(AVIMMessage.class, mMsgHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StaticLayoutManager.getInstance().clear();
    }

    @Override
    public void onRcvMessage(SIUMessage message) {
        addMessageToList(message);
    }

    public void addMsgToDB(SIUMessage message) {
        mMsgDao.add(message);
    }

    private void addMessageToList(SIUMessage message) {
        MsgViewHolderItem item = new MsgViewHolderItem(message, message.getDirection() == MsgDirection.IN ?
                mUser : AVUser.getCurrentUser(), message.getDirection().getValue() * message.getType().getValue());
        mList.add(item);
        mAdapter.notifyItemInserted(mList.size() - 1);
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        if (eventName.equals(ItemEventListener.clickEventName)) {
            /* 点击头像或者图片 */
            switch (view.getId()) {
                case R.id.sdv_portrait:
                    break;
                case R.id.sdv_img:
                    break;
                case R.id.sdv_face:
                    EmojiUtil.FaceWrapper wrapper = (EmojiUtil.FaceWrapper) values[0];
                    if (wrapper != null) {
                        if (wrapper.isEmoji()) {
                            SpannableString spannableString = TextUtil.replaceTextWithImage(mTarget,
                                    wrapper.getKey(), wrapper.getId());
                            mTarget.addText(spannableString);
                        } else {
                            /* 退格 */
                            mTarget.backspace();
                        }
                    }
                    break;
            }
        } else if (eventName.equals(ItemEventListener.longClickEventName)) {
            /* 长按图片或者文字 */

        }
        return true;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            if (mIsIMVisible) {
                mTarget.setInputMethodVisibility(false);
                mDrag = true;
            } else if (mTarget.isEmojiVisible() || mTarget.isMenuVisible()) {
                mTarget.setMenuVisibility(false);
                mTarget.setEmojiVisibility(false);
                mDrag = true;
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

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
        mTarget.setSendButtonVisibility(length > 0);
    }

    private void queryMessage(final String conversationId) {
        if (conversationId != null) {
            final List<SIUMessage> list = mMsgDao.query(conversationId);
            transformDataToItem(list);
        }
    }

    private void transformDataToItem(List<SIUMessage> list) {
        for (SIUMessage msg : list) {
            addMessageToList(msg);
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        if (mDrag) {
            mDrag = false;
            return;
        }
        mTarget.scrollToBottom(false);
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

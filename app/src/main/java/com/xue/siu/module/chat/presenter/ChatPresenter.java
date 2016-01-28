package com.xue.siu.module.chat.presenter;

import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

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
import com.xue.siu.avim.AVIMClientManager;
import com.xue.siu.avim.ActivityMessageHandler;
import com.xue.siu.common.util.HandleUtil;
import com.xue.siu.common.util.KeyboardUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.MessageUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.view.sizechangedlayout.OnSizeChangedListener;
import com.xue.siu.db.SharePreferenceC;
import com.xue.siu.db.SharePreferenceHelper;
import com.xue.siu.db.bean.MsgDirection;
import com.xue.siu.db.bean.MsgType;
import com.xue.siu.db.bean.SIUMessage;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.chat.activity.ChatActivity;
import com.xue.siu.module.chat.listener.OnRcvMessageListener;
import com.xue.siu.module.chat.viewholder.ImageMsgInViewHolder;
import com.xue.siu.module.chat.viewholder.ImageMsgOutViewHolder;
import com.xue.siu.module.chat.viewholder.TextMsgInViewHolder;
import com.xue.siu.module.chat.viewholder.TextMsgOutViewHolder;
import com.xue.siu.module.chat.viewholder.item.BaseMsgViewHolderItem;
import com.xue.siu.module.chat.viewholder.item.ImageMsgInViewHolderItem;
import com.xue.siu.module.chat.viewholder.item.ImageMsgOutViewHolderItem;
import com.xue.siu.module.chat.viewholder.item.ItemType;
import com.xue.siu.module.chat.viewholder.item.MessageUserWrapper;
import com.xue.siu.module.chat.viewholder.item.TextMsgInViewHolderItem;
import com.xue.siu.module.chat.viewholder.item.TextMsgOutViewHolderItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        TextWatcher {

    private static final String TAG = "ChatPresenter";
    /* is emoji button clicked */
    private boolean mEmoji;
    /* is menu button clicked */
    private boolean mMenu;
    /* is input method showing */
    private boolean mIsIMVisible = false;
    /* Keyboard height */
    int mKeyboardHeight = 0;

    /* 聊天相关 */
    private AVIMConversationCreatedCallback mAVIMCallback = new AVIMConversationCreatedCallback() {
        @Override
        public void done(AVIMConversation avimConversation, AVIMException e) {
            if (e == null) {
                mAnimConversation = avimConversation;
                LogUtil.d(TAG, "[create conversation] success");
            } else {
                LogUtil.e(TAG, "[create conversation] fails,try to create again");
                openConversation();
            }
        }
    };
    /* The person i'm chatting with */
    private AVUser mUser;

    private String mConversationId;

    private AVIMConversation mAnimConversation;

    private List<TAdapterItem<MessageUserWrapper>> mList = new ArrayList<>();
    private TRecycleViewAdapter mAdapter;
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();

    private final SparseArray<Class<? extends BaseMsgViewHolderItem>> mViewHolderItems = new SparseArray<>();
    private AVIMMessageHandler mMsgHandler;

    public ChatPresenter(ChatActivity target) {
        super(target);
        mViewHolders.put(ItemType.TYPE_IMAGE_IN, ImageMsgInViewHolder.class);
        mViewHolders.put(ItemType.TYPE_IMAGE_OUT, ImageMsgOutViewHolder.class);
        mViewHolders.put(ItemType.TYPE_TEXT_OUT, TextMsgOutViewHolder.class);
        mViewHolders.put(ItemType.TYPE_TEXT_IN, TextMsgInViewHolder.class);
        mViewHolderItems.put(MsgDirection.IN.getValue() * MsgType.Text.getValue(), TextMsgInViewHolderItem.class);
        mViewHolderItems.put(MsgDirection.OUT.getValue() * MsgType.Text.getValue(), TextMsgOutViewHolderItem.class);
        mViewHolderItems.put(MsgDirection.IN.getValue() * MsgType.Image.getValue(), ImageMsgInViewHolderItem.class);
        mViewHolderItems.put(MsgDirection.OUT.getValue() * MsgType.Image.getValue(), ImageMsgOutViewHolderItem.class);
    }

    @Override
    protected void initActivity() {
        mUser = mTarget.getIntent().getParcelableExtra(mTarget.INTENT_KEYS_USER);
        mConversationId = mTarget.getIntent().getStringExtra(mTarget.INTENT_KEYS_CONVERSATION_ID);
        if (mUser != null) {
            mTarget.setTitle(mUser.getUsername());
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
        openConversation();
    }

    private void openConversation() {
        AVIMClient client = AVIMClientManager.getInstance().getClient();
        if (!TextUtils.isEmpty(mConversationId)) {
            mAnimConversation = client.getConversation(mConversationId);
            if (mAnimConversation != null) {
                return;
            }
        }

        client.createConversation(Arrays.asList(mUser.getUsername()), "null", null, false, true, mAVIMCallback);
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
            case R.id.rv_msg:
                onScrollStateChanged(null, RecyclerView.SCROLL_STATE_DRAGGING);
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
        mTarget.clearMsg();
    }

    private void onEmojiClick() {
        if (mTarget.isMenuVisible() || !mIsIMVisible) {
            mTarget.setEmojiVisibility(true);
            mTarget.setMenuVisibility(false);
            return;
        }
        mEmoji = true;
        showOrCloseInput();
    }

    private void onMenuClick() {
        if (mTarget.isEmojiVisible() || !mIsIMVisible) {
            mTarget.setMenuVisibility(true);
            mTarget.setEmojiVisibility(false);
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
        mTarget.scrollToBottom(false);
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
            updateMenuHeight(bigger, newHeight, oldHeight);
    }

    private void updateMenuHeight(boolean bigger, int newHeight, int oldHeight) {

        int kbHeight = Math.abs(newHeight - oldHeight);
        LogUtil.d(TAG, "KeyboardHeight:" + kbHeight);
        if (kbHeight != mKeyboardHeight) {
            SharePreferenceHelper.putGlobalInt(SharePreferenceC.KB_HEIGHT, kbHeight);
            mTarget.setMenuHeight(kbHeight);
            mKeyboardHeight = kbHeight;
        }
    }

    /**
     * onResume要查询一次数据
     */
    @Override
    public void onResume() {
        super.onResume();
        mKeyboardHeight = SharePreferenceHelper.getGlobalInt(SharePreferenceC.KB_HEIGHT, 0);
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, mMsgHandler);
        /* 查找数据库获取聊天记录 */
    }

    @Override
    public void onPause() {
        super.onPause();
        mTarget.setInputMethodVisibility(false);
        AVIMMessageManager.unregisterMessageHandler(AVIMMessage.class, mMsgHandler);
    }

    @Override
    public void onRcvMessage(SIUMessage message) {
        addMessageToList(message);
    }

    private void addMessageToList(SIUMessage message) {
        Object[] objects = new Object[]{message,
                message.getDirection() == MsgDirection.IN ? mUser.getUsername() : AVUser.getCurrentUser()};
        Class<? extends BaseMsgViewHolderItem> clazz = mViewHolderItems.get(message.getDirection()
                .getValue() * message.getType().getValue());
        Class[] params = new Class[]{SIUMessage.class, AVUser.class};
        BaseMsgViewHolderItem item;
        try {
            Constructor<? extends BaseMsgViewHolderItem> constructor = clazz.getConstructor(params);
            item = constructor.newInstance(objects);
            mList.add(item);
            mAdapter.notifyItemInserted(mList.size() - 1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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
            }
        } else if (eventName.equals(ItemEventListener.longClickEventName)) {
            /* 长按图片或者文字 */

        }
        return true;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            if (mIsIMVisible)
                mTarget.setInputMethodVisibility(false);
            else if (mTarget.isEmojiVisible() || mTarget.isMenuVisible()) {
                mTarget.setMenuVisibility(false);
                mTarget.setEmojiVisibility(false);
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
}

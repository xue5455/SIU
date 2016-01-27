package com.xue.siu.module.chat.presenter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.AVIMClientManager;
import com.xue.siu.avim.ActivityMessageHandler;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.MessageUtil;
import com.xue.siu.common.util.ScreenObserver;
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
public class ChatPresenter extends BaseActivityPresenter<ChatActivity> implements View.OnClickListener,
        View.OnTouchListener, ScreenObserver.OnScreenHeightChangedListener, OnRcvMessageListener,
        ItemEventListener {

    private static final String TAG = "ChatPresenter";
    boolean mIsMenuClicked = false;
    boolean mIsEmojiClicked = false;
    boolean mIsIMVisible = false;
    int mKbHeight = 0;
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
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mList);
        mAdapter.setItemEventListener(this);
        mTarget.setAdapter(mAdapter);
        mMsgHandler = new ActivityMessageHandler(mTarget, mTarget.getUserId(), this);
        openConversation();
    }

    private void openConversation() {
        AVIMClient client = AVIMClientManager.getInstance().getClient();
        if (!TextUtils.isEmpty(mTarget.getConversationId())) {
            mAnimConversation = client.getConversation(mTarget.getConversationId());
            if (mAnimConversation != null) {
                return;
            }
        }

        client.createConversation(Arrays.asList(mTarget.getUserId()), "null", null, false, true, mAVIMCallback);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_emoji:
                onEmojiClick();
                break;
            case R.id.btn_plus:
                onPlusClick();
                break;
            case R.id.btn_send:
                onSendClick();
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.rv_msg:
                return onRecyclerTouch(event);
            case R.id.et_msg:
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

    private void onSendClick() {
        String content = mTarget.getMsgContent();
        AVIMTextMessage message = MessageUtil.convertStrToMessage(content);
        mAnimConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    LogUtil.d(TAG, "message send success");

                }
            }
        });
        SIUMessage message1 = MessageUtil.convertSiuToAVIMMsg(mAnimConversation.getConversationId(), mTarget.getUserId(), message);
        addMessageToList(message1);
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

    /**
     * onResume要查询一次数据
     */
    @Override
    public void onResume() {
        super.onResume();
        mKbHeight = SharePreferenceHelper.getGlobalInt(SharePreferenceC.KB_HEIGHT, 0);
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, mMsgHandler);
        /* 查找数据库获取聊天记录 */

    }

    @Override
    public void onPause() {
        super.onPause();
        mTarget.shutInputMethod();
        AVIMMessageManager.unregisterMessageHandler(AVIMMessage.class, mMsgHandler);
    }

    @Override
    public void onRcvMessage(SIUMessage message) {
        addMessageToList(message);
    }

    private void addMessageToList(SIUMessage message) {
        Object[] objects = new Object[]{message,
                message.getDirection() == MsgDirection.IN ? mTarget.getUser() : AVUser.getCurrentUser()};
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
}

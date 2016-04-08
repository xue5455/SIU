package com.xue.siu.module.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.siu.R;
import com.xue.siu.common.util.KeyboardUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenObserver;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.view.asynclist.LayoutCacheManager;
import com.xue.siu.common.view.viewpager.DotViewPagerIndicator;
import com.xue.siu.common.view.viewpager.LineViewPagerIndicator;
import com.xue.siu.constant.C;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.chat.adapter.FaceRvAdapter;
import com.xue.siu.module.news.adapter.NewsPagerAdapter;
import com.xue.siu.module.news.presenter.NewsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/2/15.
 */
public class NewsActivity extends BaseActionBarActivity<NewsPresenter> {
    public static final int REQUEST_CODE = 1;

    private ViewPager mPager;
    private LineViewPagerIndicator mIndicator;
    private TextView mTvAction;
    private TextView mTvCalendar;
    private int[] mTextColors = new int[]{R.color.black, R.color.green_normal};
    private ViewPager mVpEmoji;
    private View mViewInput;
    private EditText mEtContent;
    private Button mBtnEmoji;
    private Button mBtnSend;
    private DotViewPagerIndicator mEmojiIndicator;
    private View mViewEmojiContainer;
    private List<Fragment> mFragments;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NewsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_news);
        setNavigationBarBlack();
        setTitle(R.string.na_title);
        initViews();
        getWindow().getDecorView().setBackgroundColor(ResourcesUtil.getColor(android.R.color.white));
        ScreenObserver.assistActivity(this, mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setStatueBarColor(R.color.action_bar_bg);
    }

    private void initViews() {
        mEmojiIndicator = findView(R.id.view_indicator_news_fragment);
        mTvAction = findView(R.id.tv_action);
        mTvCalendar = findView(R.id.tv_calendar);
        mPager = findView(R.id.view_pager);
        mIndicator = findView(R.id.view_indicator);
        mPager.addOnPageChangeListener(mIndicator);
        mPager.addOnPageChangeListener(mPresenter);
        mFragments = new ArrayList<>();
        ActionFragment actionFragment = new ActionFragment();
        actionFragment.setCommentListener(mPresenter);
        mFragments.add(actionFragment);
        mFragments.add(new CalendarFragment());
        mPager.setAdapter(new NewsPagerAdapter(getSupportFragmentManager(), mFragments));
        mIndicator.setChildCount(2);
        mIndicator.setLineWidth(ScreenUtil.getDisplayWidth() / 2);
        View view = LayoutInflater.from(this).inflate(R.layout.view_news_right_button, null, false);
        setRightView(view);
        view.setOnClickListener(mPresenter);
        navigationBarContainer.setPadding(navigationBarContainer.getLeft(), navigationBarContainer.getTop(), 0,
                navigationBarContainer.getBottom());

        mVpEmoji = findView(R.id.vp_emoji_news_fragment);
        mViewInput = findView(R.id.view_input_news_fragment);
        mBtnEmoji = findView(R.id.btn_emoji);
        mBtnSend = findView(R.id.btn_send);
        mBtnSend.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_plus).setVisibility(View.GONE);
        mEtContent = findView(R.id.et_msg);
        mBtnEmoji.setOnClickListener(mPresenter);
        mBtnSend.setOnClickListener(mPresenter);
        mViewEmojiContainer = findView(R.id.layout_emoji_container);
    }

    public void updateText(int position) {
        mTvAction.setTextColor(ResourcesUtil.getColor(mTextColors[1 - position]));
        mTvCalendar.setTextColor(ResourcesUtil.getColor(mTextColors[position]));
    }

    @Override
    protected void initPresenter() {
        mPresenter = new NewsPresenter(this);
    }

    public void setCurrentItem(int position) {
        mPager.setCurrentItem(position);
    }

    public void setInputViewVisibility(boolean show) {
        setHint(C.EMPTY);
        mEtContent.setText(C.EMPTY);
        if (show) {
            mViewInput.setVisibility(View.VISIBLE);
            mEtContent.requestFocus();
            KeyboardUtil.showkeyboard(mEtContent);
        } else {
            mEtContent.clearFocus();
            mViewInput.setVisibility(View.GONE);
            KeyboardUtil.hidekeyboard(mEtContent);
            setEmojiVisibility(show);
        }
    }

    public void setHint(String hint) {
        mEtContent.setHint(hint);
    }

    public void setEmojiVpHeight(int height) {
        int faceHeight = height - ResourcesUtil.getDimenPxSize(R.dimen.chat_indicator_height)
                - ResourcesUtil.getDimenPxSize(R.dimen.chat_indicator_margin_bottom);
        LinearLayout.LayoutParams faceParams = (LinearLayout.LayoutParams) mVpEmoji.getLayoutParams();
        faceParams.height = faceHeight;
        mVpEmoji.invalidate();
    }

    public void setEmojiAdapter(PagerAdapter adapter) {
        mVpEmoji.setAdapter(adapter);
        mEmojiIndicator.setAdapter(adapter);
        mVpEmoji.addOnPageChangeListener(mEmojiIndicator);
    }

    public void setEmojiVisibility(boolean show) {
        mViewEmojiContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public boolean isEmojiVisible() {
        return mViewEmojiContainer.getVisibility() == View.VISIBLE;
    }

    public void setInputMethodVisibility(boolean show) {
        if (show) {
            mEtContent.requestFocus();
            KeyboardUtil.showkeyboard(mEtContent);
        } else {
            mEtContent.clearFocus();
            KeyboardUtil.hidekeyboard(mEtContent);
        }
    }


    @Override
    public void onDestroy() {
        LayoutCacheManager.getInstance().clear();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ((ActionFragment) mFragments.get(0)).refreshList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCommentContent() {
        return mEtContent.getText().toString();
    }
}

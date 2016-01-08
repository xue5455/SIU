package com.xue.siu.module.mainpage.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.module.base.activity.BaseBlankActivity;
import com.xue.siu.module.discovery.activity.DiscoveryFragment;
import com.xue.siu.module.group.activity.GroupFragment;
import com.xue.siu.module.mainpage.model.TabType;
import com.xue.siu.module.mainpage.presenter.MainPagePresenter;
import com.xue.siu.module.schedule.activity.ScheduleFragment;
import com.xue.siu.module.userpage.activity.UserPageFragment;

public class MainPageActivity extends BaseBlankActivity<MainPagePresenter> implements TabHost.OnTabChangeListener {
    public static final String FIRST_ITEM_KEY = "first item key";
    private FragmentTabHost mFragmentTabHost;
    private String mCurrentTab;
    public static String[] mTabTexts;
    private Class mFragmentClasses[] = {
            ScheduleFragment.class,
            GroupFragment.class,
            DiscoveryFragment.class,
            UserPageFragment.class
    };
    ;
    public int mTabNormalIcons[] = {
            R.mipmap.ic_menu_choice_nor,
            R.mipmap.ic_menu_sort_nor,
            R.mipmap.ic_menu_shoping_nor,
            R.mipmap.ic_menu_me_nor
    };

    public int mTabSelectedIcons[] = {
            R.mipmap.ic_menu_choice_pressed,
            R.mipmap.ic_menu_sort_pressed,
            R.mipmap.ic_menu_shoping_filling_pressed,
            R.mipmap.ic_menu_me_pressed
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatueBarColor(R.color.status_bar_color);
        setRealContentView(R.layout.activity_mainpage);
        mTabTexts = new String[]{
                TabType.Schedule.toString(),
                TabType.Group.toString(),
                TabType.Discovery.toString(),
                TabType.UserPage.toString()
        };
        initContentView();
        if (savedInstanceState != null) {
            String firstTabName = savedInstanceState.getString(FIRST_ITEM_KEY);
            int position = TabType.getTabPosition(firstTabName);
            setTabSelected(position, true);
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPagePresenter(this);
    }

    private void initContentView() {
        mFragmentTabHost = findView(R.id.tabhost);
        mFragmentTabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.realtabcontent);

        for (int i = 0; i < mFragmentClasses.length; i++) {
            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(mTabTexts[i]).setIndicator(getTabView(i));//设置指示器
            mFragmentTabHost.addTab(tabSpec, mFragmentClasses[i], null);//添加选项卡
            mFragmentTabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);//透明分割线
            mFragmentTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.bg_mainpage_tab);//设置indicator背景
        }

        mFragmentTabHost.setOnTabChangedListener(this);
        setTabSelected(0, true);

        //解决部分手机上tab顶部出现黑条问题
        TabWidget tabWidget = mFragmentTabHost.getTabWidget();
        if (tabWidget != null) {
            tabWidget.setPadding(0, 0, 0, 0);
            tabWidget.setBackgroundResource(R.color.white);
        }
    }

    private View getTabView(int index) {
        View view = mInflater.inflate(R.layout.item_tab_view, null);

        TextView text = (TextView) view.findViewById(R.id.txt_mainpage_tab_title);

        text.setText(mTabTexts[index]);
        ImageView imgIcon = (ImageView) view.findViewById(R.id.img_mainpage_tab_icon);
        ViewGroup.LayoutParams lp = imgIcon.getLayoutParams();
        lp.width = lp.height = ScreenUtil.getDisplayWidth() * 5 / 72;
        imgIcon.setLayoutParams(lp);
        imgIcon.setImageResource(mTabNormalIcons[index]);
        Pair<TextView, ImageView> pair = new Pair<>(text, imgIcon);
        view.setTag(pair);
        return view;
    }

    public static void start(@NonNull Context context, @NonNull TabType tabType) {
        Intent intent = new Intent(context, MainPageActivity.class);
        intent.putExtra(FIRST_ITEM_KEY, tabType.toString());
        context.startActivity(intent);
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTabSelected(int index, boolean bSelected) {
        View tabView = mFragmentTabHost.getTabWidget().getChildTabViewAt(index);
        if (tabView == null) return; // 避免崩溃，发生过一次
        Pair<TextView, ImageView> pair = (Pair<TextView, ImageView>) tabView.getTag();
        if (bSelected) {
            int color = ResourcesUtil.getColor(R.color.mainpage_tab_title_text_color_selected);
            pair.first.setTextColor(color);
            pair.second.setImageResource(mTabSelectedIcons[index]);

            mCurrentTab = mTabTexts[index];
        } else {
            int color = ResourcesUtil.getColor(R.color.mainpage_tab_title_text_color_normal);
            pair.first.setTextColor(color);
            pair.second.setImageResource(mTabNormalIcons[index]);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        for (int i = 0; i < mTabTexts.length; i++) {
            if (tabId.equals(mTabTexts[i])) {
                setTabSelected(i, true);
            } else {
                setTabSelected(i, false);
            }
        }

    }
}

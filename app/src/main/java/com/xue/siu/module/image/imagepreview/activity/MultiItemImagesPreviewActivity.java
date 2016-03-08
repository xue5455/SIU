package com.xue.siu.module.image.imagepreview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.netease.hearttouch.htimagepicker.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.view.PhotoViewViewPager;
import com.xue.siu.R;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.collection.CollectionsUtil;
import com.xue.siu.common.view.bannerindicator.BannerIndicatorLayout;
import com.xue.siu.module.image.imagepreview.ConstantsIP;
import com.xue.siu.module.image.imagepreview.adapter.ImagePreviewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzh on 2016/2/26.
 */
public class MultiItemImagesPreviewActivity extends HTBaseImagePreviewActivity {
    private PhotoViewViewPager mImagePager;

    private BannerIndicatorLayout mIndicatorLayout;

    private int mCurrentPosition = -1;

    private String mCurrentPath;

    private ArrayList<String> mImagePaths = null;

    private boolean mIsFirst = true;

    public static void start(Activity activity, String currentPath, ArrayList<String> paths) {
        if (CollectionsUtil.isEmpty(paths)) {
            return;
        }
        Intent intent = new Intent(activity, MultiItemImagesPreviewActivity.class);
        intent.putExtra(ConstantsIP.KEY_CURRENT_PATH, currentPath);
        intent.putStringArrayListExtra(ConstantsIP.KEY_IMAGE_PATHS, paths);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_item_images_preview);
        initData();
        initContentView();
        mIsFirst = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirst) {
            DialogUtil.showProgressDialog(this);
            mIsFirst = false;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if (intent.hasExtra(ConstantsIP.KEY_CURRENT_PATH)) {
            mCurrentPath = intent.getStringExtra(ConstantsIP.KEY_CURRENT_PATH);
        }
        if (intent.hasExtra(ConstantsIP.KEY_IMAGE_PATHS)) {
            mImagePaths = intent.getStringArrayListExtra(ConstantsIP.KEY_IMAGE_PATHS);
        }

        if (CollectionsUtil.isEmpty(mImagePaths)) {
            //图片路径集合是必须要有的
            this.finish();
        }
    }

    private void initContentView() {
        mImagePager = (PhotoViewViewPager) findViewById(R.id.image_fullscreen_pager);
        mIndicatorLayout = (BannerIndicatorLayout) findViewById(R.id.indicator_banner);

        mImagePager.setBackgroundResource(R.color.black);
        ImagePreviewPagerAdapter adapter = new ImagePreviewPagerAdapter(this, getImagePaths());
        mImagePager.setAdapter(adapter);
        mImagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mIndicatorLayout.changeIndicator(position % mImagePaths.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                    default:
                        break;
                }
            }
        });
        mImagePager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiItemImagesPreviewActivity.this.finish();
            }
        });

        if (mImagePaths.size() < 2) {
            mIndicatorLayout.setVisibility(View.GONE);
        } else {
            mIndicatorLayout.initIndicators(mImagePaths.size(), getCurrentPosition());
            mImagePager.setCurrentItem(getCurrentPosition());
        }
    }

    private int getCurrentPosition() {
        if (mCurrentPosition < 0) {
            if (TextUtils.isEmpty(mCurrentPath)) {
                return mCurrentPosition = 0;
            }
            for (int i = 0; i < mImagePaths.size(); i++) {
                if (TextUtils.equals(mCurrentPath, mImagePaths.get(i))) {
                    return mCurrentPosition = i;
                }
            }
            if (mCurrentPosition < 0) {
                //当前path不属于集合
                mCurrentPosition = 0;
            }
        }
        return mCurrentPosition;
    }

    @Override
    protected List<String> getImagePaths() {
        return mImagePaths;
    }

    @Override
    public ArrayList<PhotoInfo> getSelectedPhotos() {
        return null;
    }

}

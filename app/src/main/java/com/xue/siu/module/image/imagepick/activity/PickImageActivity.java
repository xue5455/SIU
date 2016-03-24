package com.xue.siu.module.image.imagepick.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.netease.hearttouch.htimagepicker.HTImageFrom;
import com.netease.hearttouch.htimagepicker.imagepick.activity.HTBaseImagePickActivity;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.view.PopupwindowMenu;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.nineoldandroids.view.ViewHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xue.siu.R;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.string.StringUtil;
import com.xue.siu.module.image.imagepick.adapter.AlbumListAdapter;
import com.xue.siu.module.image.imagepick.presenter.PickImagePresenter;
import com.xue.siu.module.image.imagepick.view.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/2/22.
 */
public class PickImageActivity extends HTBaseImagePickActivity {
    private HTSwipeRecyclerView rvPhotos;
    private TextView tvTitle;
    private ImageView ivTitle;
    private ImageView ivBackIcon;
    private View viewBack;
    private PickImagePresenter presenter;
    private Button btnConfirm;
    private TextView tvCount;
    private SystemBarTintManager tintManager;
    private final String COUNT_FORMAT = ResourcesUtil.getString(R.string.pia_count_format);
    /* 相册选择 */
    private PopupwindowMenu albumWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PickImagePresenter(this);
        initNavigationBar();
        initContentView();
        mNavigationBar.setShowBackButton(true);
        mNavigationBar.setSepLineVisiable(true);
        setStatueBarColor(R.color.action_bar_bg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        FrescoUtil.clearCacheBehind(true, false, null);
    }

    private void initNavigationBar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View titleView = inflater.inflate(R.layout.view_title_pick_image, null, false);
        viewBack = titleView.findViewById(R.id.nav_left_container);
        viewBack.setOnClickListener(presenter);
        ivBackIcon = (ImageView)titleView.findViewById(R.id.nav_left_img);
        ivBackIcon.setImageResource(R.drawable.selector_back_btn_navigationbar_red);
        tvTitle = (TextView) titleView.findViewById(R.id.tv_title_pick_image);
        tvTitle.setText(mTitle);
        ivTitle = (ImageView) titleView.findViewById(R.id.iv_pick_image);
        mNavigationBarContainer.removeAllViews();
        mNavigationBarContainer.addView(titleView);
        titleView = titleView.findViewById(R.id.ll_container_pick_image);
        titleView.setOnClickListener(presenter);
    }

    private void initContentView() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(R.layout.activity_pick_image, null, false);
        mContentView.addView(contentView);
        rvPhotos = (HTSwipeRecyclerView) contentView.findViewById(R.id.rv_photo_pick_image);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, 4));
        rvPhotos.addItemDecoration(new SpaceDecoration(
                ResourcesUtil.getDimenPxSize(R.dimen.pia_gv_horizontal_space),
                ResourcesUtil.getDimenPxSize(R.dimen.pia_gv_vertical_space)));
        rvPhotos.getRecyclerView().getItemAnimator().setChangeDuration(0);
        btnConfirm = (Button) contentView.findViewById(R.id.btn_complete_pick_image);
        tvCount = (TextView) contentView.findViewById(R.id.tv_count_pick_image);
        btnConfirm.setOnClickListener(presenter);
    }

    public void updateCount(int count) {
        tvCount.setText(StringUtil.format(COUNT_FORMAT, new Object[]{count, getCountLimit()}));
        btnConfirm.setEnabled(count != 0);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setPhotoAdapter(TRecycleViewAdapter adapter) {
        rvPhotos.setAdapter(adapter);
    }


    @Override
    public boolean isConfirmUse() {
        return presenter.isConfirmUse();
    }


    @Override
    public ArrayList<PhotoInfo> getSelectedPhotos() {
        return presenter.getSelectedPhotos();
    }

    @Override
    public void onPickedFromCamera(PhotoInfo photoInfo) {
        presenter.onPickedFromCamera(photoInfo);
    }

    @Override
    public void onCancelFromCamera() {
        presenter.onCancelFromCamera();
    }

    @Override
    public void onCompleteFromCrop(HTImageFrom from) {
        presenter.onCompleteFromCrop(from);
    }

    @Override
    public void onCancelFromCrop(HTImageFrom from) {
        presenter.onCompleteFromCrop(from);
    }

    @Override
    public void onUpdateSelectedPhotoInfos(ArrayList<PhotoInfo> photoInfos) {

    }

    @Override
    public void onScanComplete(ThumbnailsUtil thumbnailsMap, List<AlbumInfo> albumInfoList) {
        super.onScanComplete(thumbnailsMap, albumInfoList);
        presenter.onScanComplete(thumbnailsMap, albumInfoList);
    }


    public void setCheckBoxEnabled(boolean enabled) {
        int count = rvPhotos.getLayoutManager().getChildCount();
        for (int i = 0; i < count; i++) {
            View view = rvPhotos.getLayoutManager().getChildAt(i);
            if (view != null) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_pick_image);
                View container = view.findViewById(R.id.rl_container_pick_image);
                if (checkBox != null && container != null) {
                    checkBox.setEnabled(checkBox.isChecked() || enabled);
                    container.setEnabled(checkBox.isChecked() || enabled);
                }
            }
        }
    }

    public void showAlbumList(List<AlbumInfo> albumList) {
        if (albumWindow == null)
            initAlbumWindow(albumList);
        albumWindow.showAsDropDown(mNavigationBarContainer, 0, 0);
    }

    private void initAlbumWindow(List<AlbumInfo> albumList) {
        int lpHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (albumList != null && albumList.size() > 3) {
            lpHeight = ResourcesUtil.getDimenPxSize(R.dimen.pia_album_window_height);
        }
        albumWindow = new PopupwindowMenu(this,
                ViewGroup.LayoutParams.MATCH_PARENT,
                lpHeight,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        AlbumListAdapter adapter = new AlbumListAdapter(this, albumList);
        albumWindow.setCustomAdapter(adapter);
        albumWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    presenter.switchAlbum(position);
                } finally {
                    albumWindow.dismiss();
                }
            }
        });
        albumWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                    imageView.setImageResource(com.netease.hearttouch.htimagepicker.R.drawable.pick_image_title_album_show);
                rotateArrow(false);
            }
        });
        albumWindow.setAnimationStyle(R.style.albumWindowNoAnim);
    }

    public void completeSelection(ArrayList<PhotoInfo> photoInfos, boolean needCrop) {
        if (photoInfos.size() > 0 && needCrop) {
            PhotoInfo photoInfo = photoInfos.get(0);
            // 从本地图片选择进入裁剪
            startCropImage(photoInfo.getAbsolutePath(), HTImageFrom.FROM_LOCAL.toString(), null);
        } else {
//            Intent intent = mTarget.getIntent();
//            if (intent == null) intent = new Intent();

//            ArrayList<String> selectedImagePaths = getSelectedImagePaths(albumInfo, pickIndices);

//            intent.putExtra(Extras.EXTRA_ALBUM_INFO, albumInfo);
//            intent.putStringArrayListExtra(Extras.EXTRA_PICK_IMAGE_PATHS, selectedImagePaths);
//            intent.putIntegerArrayListExtra(Extras.EXTRA_PICK_INDICES, pickIndices);

//            mTarget.setResult(Activity.RESULT_OK, intent);

            finish();
        }
    }

    public int getCountLimit() {
        return mMutiSelectLimitSize;
    }

    public void takePhoto() {
        pickFromCamera();
    }

    public void rotateArrow(boolean open) {
        ViewHelper.setRotation(ivTitle, open ? 180 : 0);

    }

    public List<PhotoInfo> getPhotosExtra() {
        return mSelectedPhotos;
    }

    public void setStatueBarColor(@ColorRes int colorResId) {
        int color = ResourcesUtil.getColor(colorResId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        } else {
            // 创建状态栏的管理实例
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
                // 激活状态栏设置
                tintManager.setStatusBarTintEnabled(true);
            }
            tintManager.setTintColor(color);
        }
    }
}
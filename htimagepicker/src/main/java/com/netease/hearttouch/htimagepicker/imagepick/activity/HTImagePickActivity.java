package com.netease.hearttouch.htimagepicker.imagepick.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.netease.hearttouch.htimagepicker.HTImageFrom;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.imagepick.adapter.AlbumListAdapter;
import com.netease.hearttouch.htimagepicker.imagepick.adapter.ImageGridAdapter;
import com.netease.hearttouch.htimagepicker.imagepick.event.EventNotifier;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ImageScanUtil;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.view.PopupwindowMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/22/15.
 */
public class HTImagePickActivity
        extends HTBaseImagePickActivity
        implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    private static final String TAG = "HTImagePickActivity";

    private PopupwindowMenu mPopupwindowMenu;

    private GridView mImageGridView;
    private ImageGridAdapter mImageGridAdapter;
    private TextView mPreviewBtn;
    private Button mCompleteBtn;
    private TextView mTitleView;

    private AlbumInfo mAllPhotoAlbumInfo;

    private boolean mIsConfirmUse = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNavigationBar();
        initContentView();
        setStartFromScreenTop(false);

        EventNotifier.registerUpdatePickMarksListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventNotifier.unregisterUpdatePickMarksListener();
    }

    @Override
    public boolean isConfirmUse() {
        return mIsConfirmUse;
    }

    @Override
    public void onUpdateSelectedPhotoInfos(ArrayList<PhotoInfo> photoInfos) {
        mImageGridAdapter.notifyDataSetChanged();
        updateFinishButton();
    }

    //TODO ????? 找差一点的机子跑一下；会不会出现白屏
    @Override
    public void onScanComplete(ThumbnailsUtil thumbnailsMap, List<AlbumInfo> albumInfoList) {
        super.onScanComplete(thumbnailsMap, albumInfoList);

        List<AlbumInfo> albumInfos = ImageScanUtil.getAlbumInfoList();
        if (albumInfos != null && albumInfos.size() > 0) {
            mAllPhotoAlbumInfo = albumInfos.get(0);

            mImageGridAdapter = new ImageGridAdapter(this, mCurrAlbumInfo, mSelectedPhotos);
            mImageGridView.setAdapter(mImageGridAdapter);
            mTitleView.setText(mCurrAlbumInfo.getAlbumName());
        }
    }

    @Override
    public ArrayList<PhotoInfo> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    @Override
    public void onCancelFromCamera() {
        mIsConfirmUse = false;
        finish();
    }

    @Override
    public void onPickedFromCamera(PhotoInfo photoInfo) {
        if (mSelectedPhotos.size() < mMutiSelectLimitSize) {
            mSelectedPhotos.add(photoInfo);//0是拍照的index
        }
        mImageGridAdapter.notifyDataSetChanged();

        if (mNeedCrop) {
            // 从相机拍照进入
            startCropImage(photoInfo.getAbsolutePath(), HTImageFrom.FROM_CAMERA.toString(), null);
            return;
        }

        //if (mImageFrom == ImageFrom.FROM_CAMERA) {
        mIsConfirmUse = true;
        finish();
        //}
    }

    @Override
    public void onCompleteFromCrop(HTImageFrom from) {
        // 结束图片选取
        mIsConfirmUse = true;

        finish();
    }

    @Override
    public void onCancelFromCrop(HTImageFrom from) {
        mIsConfirmUse = false;
        if (from == HTImageFrom.FROM_LOCAL) {
            mSelectedPhotos.clear();
            mImageGridAdapter.notifyDataSetChanged();
        } else if (from == HTImageFrom.FROM_CAMERA) {
            pickFromCamera();
        }
    }

    private void initNavigationBar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View titleView = inflater.inflate(R.layout.view_title_image, null, false);
        mTitleView = (TextView) titleView.findViewById(R.id.title_text);
        mTitleView.setText(mTitle);

        ImageView imageView = (ImageView) titleView.findViewById(R.id.title_image);
        imageView.setImageResource(R.drawable.pick_image_title_album_show);
        titleView.setOnClickListener(this);

        mNavigationBar.setLeftBackImage(R.drawable.ic_back_arrow);
        mNavigationBar.setTitleView(titleView);
        mNavigationBar.setBackButtonClick(this);
    }

    private void initContentView() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(R.layout.view_pick_local_image, null, false);
        mContentView.addView(contentView);

        mImageGridView = (GridView) contentView.findViewById(R.id.pick_image_grid_view);
        mImageGridView.setOnItemLongClickListener(this);
        mImageGridView.setOnItemClickListener(this);

        mPreviewBtn = (TextView) contentView.findViewById(R.id.preview_button);
        mPreviewBtn.setOnClickListener(this);

        mCompleteBtn = (Button) contentView.findViewById(R.id.complete_button);
        mCompleteBtn.setOnClickListener(this);
        updateFinishButton();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            pickFromCamera();
            return;
        }

        int index = position - 1;
        PhotoInfo photoInfo = mCurrAlbumInfo.getPhotoList().get(index);

        if (mSelectedPhotos.contains(photoInfo)) {
            mSelectedPhotos.remove(photoInfo);
        } else if (mSelectedPhotos.size() >= mMutiSelectLimitSize) {
            ContextUtil.getInstance().makeToast(R.string.pick_image_max_pick_warning_with_number,
                    mMutiSelectLimitSize);
        } else {
            mSelectedPhotos.add(photoInfo);
        }

        mImageGridAdapter.notifyDataSetChanged();
        updateFinishButton();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_pick_image) {
            final ImageView imageView = (ImageView) v.findViewById(R.id.title_image);
            imageView.setImageResource(R.drawable.pick_image_title_album_hide);
            if (mPopupwindowMenu == null) {
                int lpHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                List<AlbumInfo> albumInfos = ImageScanUtil.getAlbumInfoList();
                if (albumInfos != null && albumInfos.size() > 5) {
                    lpHeight = 5 * (int) ContextUtil.getInstance().getDimen(R.dimen.pick_image_album_list_item_height);
                }
                mPopupwindowMenu = new PopupwindowMenu(HTImagePickActivity.this,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        lpHeight,
                        Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                AlbumListAdapter adapter = new AlbumListAdapter(HTImagePickActivity.this);
                mPopupwindowMenu.setCustomAdapter(adapter);
                mPopupwindowMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if (mImageGridAdapter != null) {
                                List<AlbumInfo> albumInfos = ImageScanUtil.getAlbumInfoList();
                                if (albumInfos != null && albumInfos.size() > 0) {
                                    mCurrAlbumInfo = albumInfos.get(position);
                                    mSelectedPhotos.clear();
                                    mImageGridAdapter.changeAlbum(mCurrAlbumInfo, mSelectedPhotos);
                                    mTitleView.setText(mCurrAlbumInfo.getAlbumName());
                                    updateFinishButton();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        } finally {
                            mPopupwindowMenu.dismiss();
                        }
                    }
                });
                mPopupwindowMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        imageView.setImageResource(R.drawable.pick_image_title_album_show);
                    }
                });
            }

            mPopupwindowMenu.showAsDropDown(mNavigationBar, 0, 0);
        } else if (v.getId() == R.id.nav_left_container) {
            mIsConfirmUse = false;
            finish();
        } else if (v.getId() == R.id.preview_button) {
            startPreviewMultiImages(mSelectedPhotos, null);
        } else if (v.getId() == R.id.complete_button) {
            if (mSelectedPhotos.size() > 0) {
                mIsConfirmUse = true;
                completeSelection(mSelectedPhotos, mNeedCrop);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int imagePosition = position - 1; // 第一个是拍照
        if (imagePosition < 0) return true;
        PhotoInfo photoInfo = mCurrAlbumInfo.getPhotoList().get(imagePosition);
        startPreviewSingleImage(photoInfo, null);
        return true;
    }

    private void updateFinishButton() {
        if (mSelectedPhotos.size() == 0) {
            mCompleteBtn.setBackgroundResource(R.drawable.shape_round_30dp_green_light);
            mCompleteBtn.setText(ContextUtil.getInstance().stringFormat(R.string.finish_with_number, 0));
        } else {
            mCompleteBtn.setBackgroundResource(R.drawable.shape_round_30dp_green_normal);
            mCompleteBtn.setText(ContextUtil.getInstance().stringFormat(R.string.finish_with_number, mSelectedPhotos.size()));
        }
    }

    private void completeSelection(ArrayList<PhotoInfo> photoInfos, boolean needCrop) {
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
}

package com.netease.hearttouch.htimagepicker.imagepick.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.netease.hearttouch.htimagepicker.HTImageFrom;
import com.netease.hearttouch.htimagepicker.HTImagePicker;
import com.netease.hearttouch.htimagepicker.constants.C;
import com.netease.hearttouch.htimagepicker.imagecrop.activity.ImageCropHelper;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.imagecrop.activity.HTImageCropActivity;
import com.netease.hearttouch.htimagepicker.imagepick.event.EventUpdateSelectedPhotosModel;
import com.netease.hearttouch.htimagepicker.imagepick.event.InternalUpdateSelectedPhotosListener;
import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagepick.listener.ImagePickerListenerCache;
import com.netease.hearttouch.htimagepicker.imagepreview.activity.ImagePreviewHelper;
import com.netease.hearttouch.htimagepicker.imagepreview.listener.IIntentProcess;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfoHelper;
import com.netease.hearttouch.htimagepicker.imagescan.ImageScanUtil;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htimagepicker.util.FileUtil;
import com.netease.hearttouch.htimagepicker.util.image.BitmapCache;
import com.netease.hearttouch.htimagepicker.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.util.storage.StorageUtil;
import com.netease.hearttouch.htimagepicker.view.base.BaseActionBarActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zyl06 on 2/20/16.
 */
public abstract class HTBaseImagePickActivity extends BaseActionBarActivity
        implements ImageScanUtil.IImageScanComplete,
        InternalUpdateSelectedPhotosListener,
        HTPickImageInterface {

    public static final int REQUEST_CODE_LOCAL = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CROP = 3;

    private static final int DEFAULT_SELECT_LIMIT_SIZE = 9;
    protected boolean mIsMutiMode;
    protected int mMutiSelectLimitSize;
    protected boolean mIsSupportOriginal;
    protected String mTitle;
    protected boolean mNeedCrop = false;
    protected HTImageFrom mImageFrom = HTImageFrom.FROM_LOCAL;
    protected String mFileOutPath;
    protected int mPickListenerKey;
    protected AlbumInfo mCurrAlbumInfo;

    // 当前已经选中的图片列表
    protected ArrayList<PhotoInfo> mSelectedPhotos = new ArrayList<>();

    private String mCameraFileName;

    public static void start(@NonNull Class pickImageActivityClass, Context context, HTImageFrom from,
                             AlbumInfo albumInfo, ArrayList<PhotoInfo> photoInfos,
                             boolean mutiSelectMode, int mutiSlectLimitSize, boolean isSupportOrig,
                             boolean crop, int outputX, int outputY,
                             String title, HTPickFinishedListener listener) {

        Intent intent = new Intent(context, pickImageActivityClass);
        intent.putExtra(Extras.EXTRA_FROM, from);
        intent.putExtra(Extras.EXTRA_FILE_PATH, HTImagePicker.getDefault().getConfig().getPhotoFileSavePath());
        intent.putExtra(Extras.EXTRA_MUTI_SELECT_MODE, mutiSelectMode);
        intent.putExtra(Extras.EXTRA_MUTI_SELECT_SIZE_LIMIT, mutiSlectLimitSize);
        intent.putExtra(Extras.EXTRA_SUPPORT_ORIGINAL, isSupportOrig);
        intent.putExtra(Extras.EXTRA_NEED_CROP, crop);
        intent.putExtra(Extras.EXTRA_OUTPUTX, outputX);
        intent.putExtra(Extras.EXTRA_OUTPUTY, outputY);
        intent.putExtra(Extras.EXTRA_PICK_TITLE, title);
        intent.putExtra(Extras.EXTRA_ALBUM_INFO, albumInfo);
//        intent.putIntegerArrayListExtra(Extras.EXTRA_PICK_INDICES, getPickIndices(albumInfo, photoInfos));
        intent.putParcelableArrayListExtra(Extras.EXTRA_PHOTO_INFO_LIST, photoInfos);

        int key = ImagePickerListenerCache.insertImagePickerListener(context, listener);
        intent.putExtra(Extras.EXTRA_LISTENER_KEY, key);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processExtra();
        tryStartImageScanTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPickListenerKey != ImagePickerListenerCache.INVALID_IMAGE_PICK_KEY) {
            callFinishListener(mPickListenerKey, isConfirmUse());
            // 清空cache中的图片
            BitmapCache.getDefault().clear(true, false);
            BitmapCache.getDefault().close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                processFromCamera(data, resultCode);
                break;
//            case REQUEST_CODE_LOCAL:
//                onPickedLocal(data, requestCode);
//                break;
            case REQUEST_CODE_CROP:
                processFromCrop(data, resultCode);
                break;
            default:
                break;
        }
    }

    protected void pickFromCamera() {
        try {
            // String outPath = getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
            String outPath = getCameraFileName(true);
            if (outPath == null) {
                ContextUtil.getInstance().makeToast("error file path");
                return;
            }

            if (TextUtils.isEmpty(outPath) || !StorageUtil.hasEnoughSystemSpaceForWrite(false)) {
                ContextUtil.getInstance().makeToast(R.string.sdcard_not_enough_error);
                return;
            }
            File outputFile = new File(outPath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } catch (ActivityNotFoundException e) {
            ContextUtil.getInstance().makeToast(R.string.pick_image_camera_invalid);
        } catch (Exception e) {
            ContextUtil.getInstance().makeToast(R.string.sdcard_not_enough_head_error);
        }
    }

    // 子类调用，获取照相目录
    protected String getCameraFileName(boolean getNew) {
        // 需求修改，照片保存在Gacha的temp目录下
        if (getNew || mCameraFileName == null) {
            mCameraFileName = HTImagePicker.getDefault().getConfig().getPhotoFileSavePath();
        }

        return mCameraFileName;
    }

    /**
     * 当启动选择图片控件之前，sdcard中的图片还没有被扫描过，则会触发扫描，扫描结束后则会调用该函数
     *
     * @param thumbnailsMap 缩略图信息
     * @param albumInfoList 相册列表信息
     */
    @Override
    public void onScanComplete(ThumbnailsUtil thumbnailsMap, List<AlbumInfo> albumInfoList) {
        List<AlbumInfo> albumInfos = ImageScanUtil.getAlbumInfoList();
        if (albumInfos != null && albumInfos.size() > 0) {
            if (mCurrAlbumInfo == null) {
                mCurrAlbumInfo = albumInfos.get(0);
            }
        }
    }

    @Override
    final public void onInternalUpdateSelectedPhotos(final EventUpdateSelectedPhotosModel event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<PhotoInfo> newSelectedPhotos = event.getSelectedPhotos();
                if (newSelectedPhotos != null) {
                    mSelectedPhotos.clear();
                    for (PhotoInfo newPhoto : newSelectedPhotos) {
                        PhotoInfo photoInfo = PhotoInfoHelper.getPhotoInfo(mCurrAlbumInfo, newPhoto.getImageId());
                        if (photoInfo != null) {
                            mSelectedPhotos.add(photoInfo);
                        }
                    }
                    onUpdateSelectedPhotoInfos(mSelectedPhotos);
                }
            }
        });
    }

    /**
     * 子类调用，启动预览多张图片的界面
     *
     * @param selectedPhotos 待预览的图片
     * @param intentProcess  intent的额外处理接口
     */
    public void startPreviewMultiImages(ArrayList<PhotoInfo> selectedPhotos, @Nullable IIntentProcess intentProcess) {
        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            ImagePreviewHelper.startMultiImagePreview(this, selectedPhotos, intentProcess);
        }
    }

    /**
     * 子类调用，启动预览单张图片的界面
     *
     * @param photoInfo     待预览的图片
     * @param intentProcess intent的额外处理接口
     */
    public void startPreviewSingleImage(PhotoInfo photoInfo, @Nullable IIntentProcess intentProcess) {
        if (photoInfo != null) {
            ImagePreviewHelper.startSingleImagePreview(this, photoInfo, intentProcess);
        }
    }

    /**
     * 子类调用，启动图片裁剪页面
     *
     * @param imageSrc      图片路径
     * @param from          来源，可选 HTImageFrom.FROM_LOCAL.toString()，HTImageFrom.FROM_CAMERA.toString()
     * @param intentProcess intent的额外处理接口
     */
    protected void startCropImage(String imageSrc, String from, @Nullable IIntentProcess intentProcess) {
        Intent intent = getIntent();
        int outputX = intent.getIntExtra(Extras.EXTRA_OUTPUTX, 0);
        int outputY = intent.getIntExtra(Extras.EXTRA_OUTPUTY, 0);
        String outPath = intent.getStringExtra(Extras.EXTRA_FILE_PATH);

        ImageCropHelper.startForFile(this, imageSrc, outputX, outputY, outPath, from, REQUEST_CODE_CROP, intentProcess);
    }

    private HTImageFrom getImageFrom(String from) {
        if (HTImageFrom.FROM_LOCAL.toString().equals(from)) {
            return HTImageFrom.FROM_LOCAL;
        } else if (HTImageFrom.FROM_CAMERA.toString().equals(from)) {
            return HTImageFrom.FROM_CAMERA;
        }
        return null;
    }

    private void processFromCrop(Intent data, int resultCode) {
        HTImageFrom imageFrom = null;
        if (data != null) {
            String strFrom = data.getStringExtra(Extras.EXTRA_FROM);
            imageFrom = getImageFrom(strFrom);
        }

        if (resultCode == RESULT_OK) {
            if (data == null) {
                onCompleteFromCrop(imageFrom);
            } else {
                String returnData = data.getStringExtra(Extras.EXTRA_RETURN_DATA);
                if (HTImageCropActivity.CONFIRM_CROP.equals(returnData)) {
                    // 结束图片选取
                    onCompleteFromCrop(imageFrom);
                } else { // if (HTImageCropActivity.CANCEL_CROP.equals(returnData)) {
                    onCancelFromCrop(imageFrom);
                }
            }
        } else {
            onCancelFromCrop(null);
        }
    }

    private void processFromCamera(Intent data, int resultCode) {
        try {
            if (resultCode == RESULT_OK) {
                String photoPath = pathFromCameraResult(data);
                if (photoPath != null) {
                    PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.setAbsolutePath(photoPath);
                    photoInfo.setFilePath(C.UrlPrefix.FILE + photoPath);
                    photoInfo.setLastModifyTime(System.currentTimeMillis());
                    photoInfo.setImageId(new Random().nextInt());

                    // 将图片信息写进contentprovider
                    ImageUtil.addImageToGallery(FileUtil.getFileParentPath(photoInfo.getAbsolutePath()), this);
                    insertPhotoInfoAtFirst(photoInfo);

                    onPickedFromCamera(photoInfo);
                }
            } else {
                onCancelFromCamera();
            }
        } catch (Exception e) {
            ContextUtil.getInstance().makeToast(R.string.pick_image_get_image_info_failed);
        }
    }

    private void insertPhotoInfoAtFirst(PhotoInfo photoInfo) {
        List<AlbumInfo> albumList = ImageScanUtil.getAlbumInfoList();
        if (albumList == null || albumList.isEmpty())
            return;

        insertPhotoInfoAtFirst(albumList.get(0), photoInfo);

        int size = albumList.size();
        for (int i=1; i<size; ++i) {
            AlbumInfo albumInfo = albumList.get(i);
            String albumPath = albumInfo.getAbsolutePath();
            String photoParentPath = FileUtil.getFileParentPath(photoInfo.getAbsolutePath());

            if (TextUtils.equals(albumPath, photoParentPath)) {
                insertPhotoInfoAtFirst(albumInfo, photoInfo);
                break;
            }
        }
    }

    private void insertPhotoInfoAtFirst(AlbumInfo albumInfo, PhotoInfo photoInfo) {
        if (albumInfo == null) return;

        if (albumInfo.getPhotoList() == null) {
            albumInfo.setPhotoList(new ArrayList<PhotoInfo>());
        }
        albumInfo.getPhotoList().add(0, photoInfo);
        albumInfo.setFilePath(photoInfo.getFilePath());
        albumInfo.setAbsolutePath(FileUtil.getFileParentPath(photoInfo.getAbsolutePath()));
    }

    private String pathFromCameraResult(Intent data) {
        String outPath = getCameraFileName(false); //getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
        if (data == null || data.getData() == null) {
            return outPath;
        }

        Uri uri = data.getData();
        Cursor cursor = getContentResolver()
                .query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

        if (cursor == null) {
            // miui 2.3 有可能为null
            return uri.getPath();
        } else {
            if (uri.toString().contains("content://com.android.providers.media.documents/document/image")) { // htc 某些手机
                // 获取图片地址
                String _id = null;
                String uridecode = uri.decode(uri.toString());
                int id_index = uridecode.lastIndexOf(":");
                _id = uridecode.substring(id_index + 1);
                Cursor mcursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, " _id = " + _id,
                        null, null);
                mcursor.moveToFirst();
                int column_index = mcursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = mcursor.getString(column_index);
                if (!mcursor.isClosed()) {
                    mcursor.close();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;

            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = cursor.getString(column_index);
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;
            }
        }
    }

    private void tryStartImageScanTask() {
        ImageScanUtil.scanImages(this, true);

//        final List<AlbumInfo> albumInfos = ImageScanUtil.getAlbumInfoList();
//        if (albumInfos == null || albumInfos.size() == 0) {
//            ImageScanUtil.scanImages(this, true);
//        } else {
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    onScanComplete(null, albumInfos);
//                }
//            });
//        }
    }

    private void processExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            mIsMutiMode = intent.getBooleanExtra(Extras.EXTRA_MUTI_SELECT_MODE, false);
            mMutiSelectLimitSize = intent.getIntExtra(Extras.EXTRA_MUTI_SELECT_SIZE_LIMIT, DEFAULT_SELECT_LIMIT_SIZE);
            mIsSupportOriginal = intent.getBooleanExtra(Extras.EXTRA_SUPPORT_ORIGINAL, false);
            mNeedCrop = intent.getBooleanExtra(Extras.EXTRA_NEED_CROP, false);
            mImageFrom = (HTImageFrom) intent.getSerializableExtra(Extras.EXTRA_FROM);
            mFileOutPath = intent.getStringExtra(Extras.EXTRA_FILE_PATH);
            mPickListenerKey = intent.getIntExtra(Extras.EXTRA_LISTENER_KEY, ImagePickerListenerCache.INVALID_IMAGE_PICK_KEY);

            // 如果支持裁剪的话，那只能单选
            if (mNeedCrop) mIsMutiMode = false;
            // 如果直接设置从相机中选取的话，那就能单选
            if (mImageFrom == HTImageFrom.FROM_CAMERA) mIsMutiMode = false;
            // 如果是单选模式，则直接设置mMutiSelectLimitSize为1
            if (!mIsMutiMode) mMutiSelectLimitSize = 1;

            mTitle = intent.getStringExtra(Extras.EXTRA_PICK_TITLE);

            mCurrAlbumInfo = (AlbumInfo) intent.getSerializableExtra(Extras.EXTRA_ALBUM_INFO);

            mSelectedPhotos = intent.getParcelableArrayListExtra(Extras.EXTRA_PHOTO_INFO_LIST);
            if (mSelectedPhotos == null) mSelectedPhotos = new ArrayList<>();

            // 如果是制定从摄像机中拍照，就直接开启拍照页面
            if (mImageFrom == HTImageFrom.FROM_CAMERA) {
                pickFromCamera();
            }
        }
    }

    private void callFinishListener(int pickListenerKey, boolean isConfirmUse) {
        List<PhotoInfo> selectedPhotos = getSelectedPhotos();
        if (selectedPhotos == null) {
            selectedPhotos = new ArrayList<>();
        }

        if (isConfirmUse && mNeedCrop && mFileOutPath != null) {
            PhotoInfo photoInfo = new PhotoInfo();
            String outPath = mFileOutPath;
            photoInfo.setFilePath(C.UrlPrefix.FILE + outPath);
            photoInfo.setAbsolutePath(outPath);

            ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
            photoInfos.add(photoInfo);
            ImagePickerListenerCache.callImagePickerFinishListener(pickListenerKey, mCurrAlbumInfo, photoInfos);
        } else if (isConfirmUse && selectedPhotos.size() > 0) {
            ImagePickerListenerCache.callImagePickerFinishListener(pickListenerKey, mCurrAlbumInfo, selectedPhotos);
        } else {
            ImagePickerListenerCache.callImagePickerCancelListener(pickListenerKey);
        }
    }
}

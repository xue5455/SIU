package com.xue.siu.module.imagepick.presenter;

import android.content.Intent;
import android.util.SparseArray;
import android.view.View;

import com.netease.hearttouch.htimagepicker.HTImageFrom;
import com.netease.hearttouch.htimagepicker.imagepick.activity.HTPickImageInterface;
import com.netease.hearttouch.htimagepicker.imagepreview.listener.IIntentProcess;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ImageScanUtil;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htimagepicker.imagescan.ThumbnailsUtil;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventHelper;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.module.imagepick.activity.PickImageActivity;
import com.xue.siu.module.imagepick.model.PhotoInfoWrapper;
import com.xue.siu.module.imagepick.viewholder.CameraViewHolder;
import com.xue.siu.module.imagepick.viewholder.ImageViewHolder;
import com.xue.siu.module.imagepick.viewholder.item.CameraViewHolderItem;
import com.xue.siu.module.imagepick.viewholder.item.ImageItemType;
import com.xue.siu.module.imagepick.viewholder.item.ImageViewHolderItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by XUE on 2016/2/22.
 */
public class PickImagePresenter implements View.OnClickListener,
        ImageScanUtil.IImageScanComplete,
        HTPickImageInterface,
        ItemEventListener {
    private PickImageActivity target;
    private LinkedHashMap<String, PhotoInfo> selectedMap = new LinkedHashMap<>();
    private final SparseArray<Class<? extends TRecycleViewHolder>> viewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(ImageItemType.ITEM_TYPE_COMMON, ImageViewHolder.class);
                    put(ImageItemType.ITEM_TYPE_CAMERA, CameraViewHolder.class);
                }
            };
    private List<TAdapterItem<PhotoInfoWrapper>> adapterItems = new ArrayList<>();

    private TRecycleViewAdapter photoAdapter;

    private boolean confirmUse = false;

    private List<AlbumInfo> albumList;

    private int albumPosition = 0;


    public PickImagePresenter(PickImageActivity activity) {
        target = activity;
    }

    private void initAdapter(AlbumInfo albumInfo) {
        photoAdapter = new TRecycleViewAdapter(target, viewHolders, adapterItems);
        photoAdapter.setItemEventListener(this);
        List<PhotoInfo> infos = target.getPhotosExtra();
        for (PhotoInfo info : infos) {
            selectedMap.put(info.getAbsolutePath(), info);
        }
        PhotoInfoWrapper.setEnabled(selectedMap.size() != target.getCountLimit());
        target.updateCount(selectedMap.size());
        target.setPhotoAdapter(photoAdapter);
        bindData(albumInfo);

    }

    private void bindData(AlbumInfo albumInfo) {
        List<PhotoInfo> list = new ArrayList<>();
        if (albumInfo != null) {
            target.setTitle(albumInfo.getAlbumName());
            list = albumInfo.getPhotoList();
        }
        adapterItems.clear();
        adapterItems.add(new CameraViewHolderItem());
        for (PhotoInfo info : list) {
            PhotoInfoWrapper wrapper = new PhotoInfoWrapper(info);
            String path = info.getAbsolutePath();
            wrapper.setSelected(selectedMap.containsKey(path));
            adapterItems.add(new ImageViewHolderItem(wrapper));
        }
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_container_pick_image:
                target.showAlbumList(albumList);
                target.rotateArrow(true);
                break;
            case R.id.btn_complete_pick_image:
                confirmUse = true;
                target.completeSelection(getSelectedPhotos(), false);
//                target.startPreviewMultiImages(getSelectedPhotos(), null);
                break;
            case R.id.nav_left_container:
                confirmUse = false;
                target.finish();
                break;
        }
    }

    @Override
    public boolean isConfirmUse() {
        return confirmUse;
    }

    @Override
    public ArrayList<PhotoInfo> getSelectedPhotos() {
        ArrayList<PhotoInfo> list = new ArrayList<>();
        Set<String> set = selectedMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            list.add(selectedMap.get(key));
        }
        return list;
    }

    @Override
    public void onPickedFromCamera(PhotoInfo photoInfo) {
        insertPhotoAtFirst(photoInfo);
    }

    @Override
    public void onCancelFromCamera() {

    }

    @Override
    public void onCompleteFromCrop(HTImageFrom from) {

    }

    @Override
    public void onCancelFromCrop(HTImageFrom from) {

    }

    @Override
    public void onUpdateSelectedPhotoInfos(ArrayList<PhotoInfo> photoInfos) {

    }

    @Override
    public void onScanComplete(ThumbnailsUtil thumbnailsMap, List<AlbumInfo> albumInfoList) {
        albumList = albumInfoList;
        AlbumInfo albumInfo = null;
        if (albumInfoList.size() != 0)
            albumInfo = albumInfoList.get(0);
        initAdapter(albumInfo);
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        if (ItemEventHelper.isClick(eventName)) {
            switch (view.getId()) {
                case R.id.rl_container_pick_image:
                    onImageCheckChanged(position, values);
                    break;
                case R.id.sdv_pic_pick_image:
                    //图片预览界面需要得到的参数不是photoInfo，这里需要根据该页面需要的参数来配置intent
//                    final PhotoInfo info = values == null ? null : ((PhotoInfo) values[0]);
//                    if (info == null) {
//                        break;
//                    }
//                    target.startPreviewSingleImage(info, new IIntentProcess() {
//                        @Override
//                        public void onProcessIntent(Intent intent) {
//                            if (intent != null) {
//                                intent.putExtra(ConstantsIP.KEY_PHOTO_FILE_PATH, info.getFilePath());
//                            }
//                        }
//                    });
                    break;
                case R.id.view_camera_pick_image:
                    /* 跳转到拍照界面 */
                    target.takePhoto();
                    break;
            }
        }
        return true;
    }

    private void onImageCheckChanged(int position, Object... values) {
        if (values != null) {
            boolean checked = (boolean) values[0];
            adapterItems.get(position).getDataModel().setSelected(checked);
            PhotoInfo info = adapterItems.get(position).getDataModel().getPhotoInfo();
            String url = info.getAbsolutePath();
            if (checked) {
                selectedMap.put(url, info);
            } else {
                selectedMap.remove(url);
            }
            updateCheckboxStatus(checked);
            target.updateCount(selectedMap.size());
        }


    }

    public void switchAlbum(int position) {
        if (position != albumPosition) {
            bindData(albumList.get(position));
            albumPosition = position;
        }
    }

    private void insertPhotoAtFirst(PhotoInfo photoInfo) {
        PhotoInfoWrapper wrapper = new PhotoInfoWrapper(photoInfo);
        adapterItems.add(1, new ImageViewHolderItem(wrapper));
        boolean isAdded = false;
        if (selectedMap.size() < target.getCountLimit()) {
            selectedMap.put(photoInfo.getAbsolutePath(), photoInfo);
            wrapper.setSelected(true);
            target.updateCount(selectedMap.size());
            isAdded = true;
        }
        photoAdapter.notifyDataSetChanged();
        if (isAdded)
            updateCheckboxStatus(isAdded);
    }

    private void updateCheckboxStatus(boolean checked) {
        if (checked && selectedMap.size() == target.getCountLimit()) {
            PhotoInfoWrapper.setEnabled(false);
            target.setCheckBoxEnabled(false);
        } else if (!checked && selectedMap.size() == target.getCountLimit() - 1) {
            PhotoInfoWrapper.setEnabled(true);
            target.setCheckBoxEnabled(true);
        }
    }
}

package com.xue.siu.module.news.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.netease.hearttouch.htimagepicker.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.imagescan.AlbumInfo;
import com.netease.hearttouch.htimagepicker.imagescan.PhotoInfo;
import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htrecycleview.event.ItemEventListener;
import com.xue.siu.R;
import com.xue.siu.avim.base.AVIMResultListener;
import com.xue.siu.common.util.DialogUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.TextUtil;
import com.xue.siu.common.util.ToastUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.news.activity.PublishActivity;
import com.xue.siu.module.news.callback.SavePostCallback;
import com.xue.siu.module.news.callback.UploadImageCallback;
import com.xue.siu.module.news.model.ActionVO;
import com.xue.siu.module.news.model.PublishEditModel;
import com.xue.siu.module.news.viewholder.PublishEditViewHolder;
import com.xue.siu.module.news.viewholder.item.NewsItemType;
import com.xue.siu.module.news.viewholder.item.PublishEditViewHolderItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XUE on 2016/3/4.
 */
public class PublishPresenter extends BaseActivityPresenter<PublishActivity> implements
        ItemEventListener, HTPickFinishedListener, View.OnClickListener, AVIMResultListener {

    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders =
            new SparseArray<Class<? extends TRecycleViewHolder>>() {
                {
                    put(NewsItemType.ITEM_PUBLISH_EDIT, PublishEditViewHolder.class);
                }
            };
    private List<TAdapterItem<PublishEditModel>> mItems = new ArrayList<>();
    private PublishEditModel mEditModel;
    private TRecycleViewAdapter mAdapter;
    private List<PhotoInfo> photoList = new ArrayList<>();
    private List<AVFile> mPicList = new ArrayList<>();
    private List<PhotoInfo> mQueueList = new ArrayList<>();
    private UploadImageCallback mUploadCallback;
    private AVFile mCurrentAVFile;

    public PublishPresenter(PublishActivity target) {
        super(target);
        mUploadCallback = new UploadImageCallback(this);
    }

    @Override
    protected void initActivity() {
        mEditModel = new PublishEditModel();
        mEditModel.setLocalPicList(photoList);
        mItems.add(new PublishEditViewHolderItem(mEditModel));
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mItems);
        mAdapter.setItemEventListener(this);
        mTarget.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEventNotify(String eventName, View view, int position, Object... values) {
        switch (view.getId()) {
            case R.id.btn_photo_publish:
                mTarget.pickImage(mEditModel.getLocalPicList());
                break;
        }
        return true;
    }

    @Override
    public void onImagePickFinished(@Nullable AlbumInfo albumInfo, List<PhotoInfo> photos) {
        mEditModel.setLocalPicList(photos);
        mAdapter.notifyDataSetChanged();
        mTarget.setRightButtonEnabled(photos.size() != 0);
    }

    @Override
    public void onImagePickCanceled() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete_publish:
                mQueueList.addAll(mEditModel.getLocalPicList());
                DialogUtil.showProgressDialog(mTarget, false);
                uploadPhoto();
                break;
        }
    }

    private void postContent(List<AVFile> picList) {
        String content = mEditModel.getContent();
        ActionVO actionVO = new ActionVO();
        actionVO.setCreator(AVUser.getCurrentUser());
        actionVO.setPicList(picList);
        actionVO.setContent(content);
        actionVO.setLocation("网商路599号 网易大厦");
        AVObject object = new AVObject("Post");
        object.put("content", content);
        object.put("creator", AVUser.getCurrentUser());
        object.put("picList", picList);
        object.put("location", "网商路599号 网易大厦");
        object.saveInBackground(new SavePostCallback(this).getCallback());
    }

    private void uploadPhoto() {
        if (mQueueList.size() == 0) {
            postContent(mPicList);
            return;
        }
        PhotoInfo info = mQueueList.get(0);
        File file = new File(info.getAbsolutePath());
        if (!file.exists()) {
            mQueueList.remove(0);
            uploadPhoto();
            return;
        }
        try {
            mCurrentAVFile = AVFile.withFile(AVUser.getCurrentUser().getObjectId(), file);
            mCurrentAVFile.saveInBackground(mUploadCallback.getCallback());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addImageToList() {
        mPicList.add(mCurrentAVFile);
        mCurrentAVFile = null;
        mQueueList.remove(0);
        uploadPhoto();
    }


    @Override
    public void onLeanError(String cbName, AVException e) {
        DialogUtil.hideProgressDialog(mTarget);
        ToastUtil.makeShortToast(R.string.pa_net_error);
    }

    @Override
    public void onLeanSuccess(String cbName, Object... values) {
        if (TextUtils.equals(cbName, UploadImageCallback.class.getName())) {
            addImageToList();
        } else if (TextUtils.equals(cbName, SavePostCallback.class.getName())) {
            mTarget.setResult(mTarget.RESULT_OK);
            mTarget.finish();
        }
    }
}

package com.netease.hearttouch.htimagepicker.imagecrop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.netease.hearttouch.htimagepicker.imagecrop.view.ImageCropView;
import com.netease.hearttouch.htimagepicker.imagepick.activity.Extras;
import com.netease.hearttouch.htimagepicker.util.image.ImageUtil;

/**
 * Created by zyl06 on 2/23/16.
 */
public abstract class HTBaseImageCropActivity
        extends FragmentActivity
        implements HTImageCropInterface {
    public static final String CANCEL_CROP = "HTBaseImageCropActivity_cancelCrop";
    public static final String CONFIRM_CROP = "HTBaseImageCropActivity_confirmCrop";

    protected boolean mReturnData;
    protected String mFilePath;
    protected String mFrom;
    protected String mSrcFile;
    protected int mOutputX;
    protected int mOutputY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processIntent();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        cancelCropFinish();
    }

    @Override
    public void confirmCropFinish() {
        ImageCropView imageCropView = getImageCropView();

        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_RETURN_DATA, CONFIRM_CROP);
        if (mFrom != null) intent.putExtra(Extras.EXTRA_FROM, mFrom);
        if (mReturnData) {
            byte[] data = imageCropView.getCroppedImage();
            if (data != null) {
                intent.putExtra(Extras.EXTRA_DATA, data);
                setResult(RESULT_OK, intent);
            }
            finish();
        } else {
            if (imageCropView.saveCroppedIamge(mFilePath)) {
                ImageUtil.addImageToGallery(mFilePath, this);
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }

    @Override
    public void cancelCropFinish() {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_RETURN_DATA, CANCEL_CROP);
        if (mFrom != null) intent.putExtra(Extras.EXTRA_FROM, mFrom);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void processIntent() {
        Intent intent = getIntent();
        mReturnData = intent.getBooleanExtra(Extras.EXTRA_RETURN_DATA, false);
        mFilePath = intent.getStringExtra(Extras.EXTRA_FILE_PATH);
        mFrom = intent.getStringExtra(Extras.EXTRA_FROM);

        mSrcFile = intent.getStringExtra(Extras.EXTRA_SRC_FILE);
        mOutputX = intent.getIntExtra(Extras.EXTRA_OUTPUTX, 0);
        mOutputY = intent.getIntExtra(Extras.EXTRA_OUTPUTY, 0);
    }
}

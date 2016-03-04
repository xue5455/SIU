package com.netease.hearttouch.htimagepicker.imagecrop.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.imagecrop.view.ImageCropView;
import com.netease.hearttouch.htimagepicker.HTImageFrom;
import com.netease.hearttouch.htimagepicker.util.image.BitmapDecoder;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by zyl06 on 6/25/15.
 */
public class HTImageCropActivity extends HTBaseImageCropActivity implements View.OnClickListener {

    // 显示的裁剪图片控件
    private ImageCropView mCropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        initCropImageView();
        initButtons();
    }

    @Override
    protected void onDestroy() {
        mCropImageView.clear();
        super.onDestroy();
    }

    @Override
    public ImageCropView getImageCropView() {
        return mCropImageView;
    }

    private void initCropImageView() {
        mCropImageView = (ImageCropView) findViewById(R.id.cropable_image_view);
        mCropImageView.setOutput(mOutputX, mOutputY);

        // 抛到下一个UI循环，等到我们的activity真正到了前台
        // 否则可能会获取不到openGL的最大texture size，导致解出的bitmap过大，显示不了
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Bitmap src = BitmapDecoder.decodeFile(mSrcFile, true);
                mCropImageView.setImageBitmap(src);
            }
        });
    }

    private void initButtons() {
        TextView reselectBtn = (TextView) findViewById(R.id.btn_reselect);
        TextView confirmUseBtn = (TextView) findViewById(R.id.btn_use);
        reselectBtn.setOnClickListener(this);
        confirmUseBtn.setOnClickListener(this);
        if (HTImageFrom.FROM_CAMERA.toString().equals(mFrom)) {
            reselectBtn.setText(R.string.retakephoto);
        } else {
            reselectBtn.setText(R.string.reselect);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_reselect) {
            cancelCropFinish();
        } else if (v.getId() == R.id.btn_use) {
            confirmCropFinish();
        }
    }
}

package com.xue.siu.module.image.imagepreview.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.netease.photoview.PhotoViewAttacher;
import com.xue.siu.R;
import com.xue.siu.module.image.imagepreview.ConstantsIP;
import com.xue.siu.module.image.imagepreview.activity.SingleItemImagePreviewActivity;


/**
 * Created by yzh on 2016/2/23.
 */
public class SingleItemImagePreviewPresenter implements View.OnClickListener, PhotoViewAttacher.OnViewTapListener {
    private SingleItemImagePreviewActivity mTarget;

    public SingleItemImagePreviewPresenter(SingleItemImagePreviewActivity activity) {
        mTarget = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_image_view_gif:
            case R.id.root:
                if (mTarget != null) {
                    mTarget.finish();
                }
                break;
            case R.id.preview_delete:
                if (mTarget != null) {
                    Intent intent = new Intent();
                    intent.putExtra(ConstantsIP.KEY_PHOTO_FILE_PATH, mTarget.getImagePath());
                    intent.putExtra(ConstantsIP.KEY_POSITION, mTarget.getPosition());
                    mTarget.setResult(Activity.RESULT_OK, intent);
                    mTarget.finish();
                }
                break;
        }
    }

    @Override
    public void onViewTap(View view, float v, float v1) {
        if (mTarget != null) {
            mTarget.finish();
        }
    }
}

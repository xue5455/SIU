package com.xue.siu.module.image.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.module.base.activity.BaseActionBarActivity;
import com.xue.siu.module.image.presenter.ImagePresenter;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XUE on 2016/1/19.
 */
public class ImageActivity extends BaseActionBarActivity<ImagePresenter> {
    public static final int REQUEST_GET_IMAGE = 1;
    public static final String KEY_IMAGE_PATH = "path";
    public static final String KEY_IMAGE_LIMIT = "count_limit";//数量限制

    private final int REQUEST_CROP_IMAGE = 2;

    private String mTempFilePath;
    @Bind(R.id.rv_image)
    HTSwipeRecyclerView mRvImage;

    public static void start(Activity activity, int count) {
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra(KEY_IMAGE_LIMIT, count);
        activity.startActivityForResult(intent, REQUEST_GET_IMAGE);
    }

    public static void start(Fragment fragment,int count){
        Intent intent = new Intent(fragment.getActivity(),ImageActivity.class);
        intent.putExtra(KEY_IMAGE_LIMIT,count);
        fragment.startActivityForResult(intent,REQUEST_GET_IMAGE);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ImagePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRealContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initContentView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initContentView() {
        setNavigationBarBlack();
        setTitle(R.string.ia_title);
        navigationBar.setBackButtonClick(mPresenter);
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRvImage.setLayoutManager(manager);
        mRvImage.getRecyclerView().getItemAnimator().setChangeDuration(0);

    }

    public void setAdapter(TRecycleViewAdapter adapter) {
        mRvImage.setAdapter(adapter);
    }

    public void addOnScrollListener(HTSwipeRecyclerView.OnScrollListener onScrollListener) {
        mRvImage.addOnScrollListener(onScrollListener);
    }


    public void jumpToCutImageActivity(String path) {
        File cacheDir = getCacheDir();//文件所在目录为getFilesDir();
        String cachePath = cacheDir.getAbsolutePath();
        File file = new File(cachePath);
        if(!file.exists())
            file.mkdirs();
        LogUtil.d("xxj", "cachePath is " + cachePath);
        mTempFilePath = cachePath + File.pathSeparator + (System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("crop", "true");
        //width:height
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("output", Uri.fromFile(new File(mTempFilePath)));
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == REQUEST_CROP_IMAGE){
            Intent intent  = new Intent();
            if(resultCode == RESULT_OK){
                intent.putExtra(KEY_IMAGE_PATH, mTempFilePath);
                setResult(RESULT_OK, intent);
            }else{
                setResult(RESULT_CANCELED);
            }
            finish();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}

package com.xue.siu.module.image.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.SparseArray;
import android.view.View;

import com.netease.hearttouch.htrecycleview.TAdapterItem;
import com.netease.hearttouch.htrecycleview.TRecycleViewAdapter;
import com.netease.hearttouch.htrecycleview.TRecycleViewHolder;
import com.netease.hearttouch.htswiperefreshrecyclerview.HTSwipeRecyclerView;
import com.xue.siu.R;
import com.xue.siu.common.util.HandleUtil;
import com.xue.siu.common.util.LogUtil;
import com.xue.siu.common.util.ResourcesUtil;
import com.xue.siu.common.util.ScreenUtil;
import com.xue.siu.common.util.ThreadUtil;
import com.xue.siu.module.base.presenter.BaseActivityPresenter;
import com.xue.siu.module.follow.viewholder.item.ItemType;
import com.xue.siu.module.image.activity.ImageActivity;
import com.xue.siu.module.image.viewholder.ImageViewHolder;
import com.xue.siu.module.image.viewholder.item.ImageVO;
import com.xue.siu.module.image.viewholder.item.ImageViewHolderItem;

import java.util.ArrayList;
import java.util.List;

import event.util.AsyncExecutor;

/**
 * Created by XUE on 2016/1/19.
 */
public class ImagePresenter extends BaseActivityPresenter<ImageActivity> implements View.OnClickListener, HTSwipeRecyclerView.OnScrollListener {
    private Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private ContentResolver mContentResolver;
    private final SparseArray<Class<? extends TRecycleViewHolder>> mViewHolders = new SparseArray<>();
    private List<TAdapterItem> mTAdapterItems = new ArrayList<>();
    private TRecycleViewAdapter mAdapter;
    private int mImageViewWidth;//ImageView的宽度

    public ImagePresenter(ImageActivity target) {
        super(target);
        mViewHolders.put(ItemType.ITEM_COMMON, ImageViewHolder.class);
        mImageViewWidth = (ScreenUtil.getDisplayWidth() - 4 * ResourcesUtil.getDimenPxSize(R.dimen.ia_image_padding)) / 2;
    }

    @Override
    protected void initActivity() {
        mAdapter = new TRecycleViewAdapter(mTarget, mViewHolders, mTAdapterItems);
        mTarget.setAdapter(mAdapter);
        mTarget.addOnScrollListener(this);
        mContentResolver = mTarget.getContentResolver();
        new QueryTask().execute();
    }

    @Override
    public void onClick(View v) {
        mTarget.finish();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            int count = manager.getChildCount();
            View view = manager.getChildAt(0);
            int first = manager.getPosition(view);
            mAdapter.notifyItemRangeChanged(first, count);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }


    class QueryTask extends AsyncTask<Void, ImageViewHolderItem, Void> {

        private int position = 0;

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            String schema = "file://";
            while (!cursor.isAfterLast()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.moveToNext();
                BitmapFactory.decodeFile(path, options);
                if (options.outWidth < 200 || options.outHeight < 200)
                    continue;
                LogUtil.d("xxj", "height " + options.outHeight);
                ImageVO imageVO = new ImageVO();
                imageVO.setPath(schema + path);
                imageVO.setWidth(mImageViewWidth);
                double scale = 1.0d * mImageViewWidth / options.outWidth;
                int height = (int) (options.outHeight * scale);
                if (height > 1.5f * mImageViewWidth) {
                    height = (int) (1.5f * mImageViewWidth);
                }
                imageVO.setHeight(height);
                publishProgress(new ImageViewHolderItem(imageVO));
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onProgressUpdate(ImageViewHolderItem... item) {
            mTAdapterItems.add(item[0]);
            mAdapter.notifyItemInserted(position);
            position++;
        }
    }


}

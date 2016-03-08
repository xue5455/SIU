package com.xue.siu.common.view.asynclist;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVFile;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xue.siu.common.util.media.FrescoUtil;

import org.xml.sax.helpers.ParserAdapter;

/**
 * Created by XUE on 2016/3/2.
 */
public class AsyncGridView extends ViewGroup {
    private BaseAdapter mAdapter;
    private int mVerticalSpace = 0;
    private int mHorizontalSpace = 0;
    private int mNumCol = 1;

    public AsyncGridView(Context context) {
        super(context);
    }

    public AsyncGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        fillLayout();
    }

    public int getVerticalSpace() {
        return mVerticalSpace;
    }

    public void setVerticalSpace(int verticalSpace) {
        this.mVerticalSpace = verticalSpace;
    }

    public int getHorizontalSpace() {
        return mHorizontalSpace;
    }

    public void setHorizontalSpace(int horizontalSpace) {
        this.mHorizontalSpace = horizontalSpace;
    }

    public int getNumCol() {
        return mNumCol;
    }

    public void setNumCol(int numCol) {
        this.mNumCol = numCol;
    }

    private void fillLayout() {
        removeAllViews();
        new FillLayoutTask(this, mAdapter).execute();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);//获取宽度
        int childWidthSpec = MeasureSpec.makeMeasureSpec((sizeWidth - 2 * mHorizontalSpace) / 3, MeasureSpec.EXACTLY);
        // 计算出所有的childView的宽和高

        measureChildren(childWidthSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int height = 0;

        int cCount = getChildCount();

        if (cCount == 0) {
            height = 0;
        } else if (cCount == 1) {
            height = getChildAt(0).getMeasuredHeight();
        } else {
            int rows = (int) Math.ceil((float) cCount / getNumCol());
            int sizeHeight = getChildAt(0).getLayoutParams().height;
            height = rows * sizeHeight + (rows - 1) * mVerticalSpace;
        }
        setMeasuredDimension(sizeWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int count = getChildCount();
        if (count == 1) {
            View view = getChildAt(0);
            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
        } else {
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
                left += (view.getMeasuredWidth() + mHorizontalSpace);
                if (i % mNumCol == mNumCol - 1) {
                    top += (view.getMeasuredHeight() + mVerticalSpace);
                    left = 0;
                }
            }
        }

    }

    private static class FillLayoutTask extends AsyncTask<Void, Object, Void> {
        private ViewGroup layout;
        private BaseAdapter adapter;

        public FillLayoutTask(ViewGroup layout, BaseAdapter adapter) {
            this.layout = layout;
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, layout);
                if (view == null)
                    return null;
                String picSrc = ((AVFile) adapter.getItem(i)).getUrl();
                publishProgress(view, picSrc);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects) {
            SimpleDraweeView sdvPic = (SimpleDraweeView) objects[0];
            layout.addView(sdvPic);
            String picUrl = (String) objects[1];
            LayoutParams params = sdvPic.getLayoutParams();
            FrescoUtil.setImageUri(sdvPic,picUrl,params.width,params.height);
        }
    }
}

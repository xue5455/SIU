package com.xue.siu.common.view.asynclist;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.avos.avoscloud.AVFile;
import com.facebook.drawee.view.SimpleDraweeView;

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
        int count = getChildCount();
        int width = 0;
        int height = 0;
        if (count == 1) {
            measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
            width = getChildAt(0).getMeasuredWidth();
            height = getChildAt(0).getMeasuredHeight();
        } else {
            measure(widthMeasureSpec, heightMeasureSpec);
            width = getMeasuredWidth();
            int childW = (width - (mNumCol - 1) * mHorizontalSpace) / mNumCol;
            int childH = childW;
            for (int i = 0; i < count; i++) {
                LayoutParams params = getChildAt(i).getLayoutParams();
                params.width = childW;
                params.height = childH;
                getChildAt(i).setLayoutParams(params);
            }
            int rows = (int) Math.ceil((float) count / mNumCol);
            height = rows * childH + (rows - 1) * childH;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int count = getChildCount();
        if (count == 1) {
            View view = getChildAt(0);
            view.layout(left, top, view.getMeasuredWidth(), view.getMeasuredHeight());
        } else {
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                view.layout(left, top, view.getMeasuredWidth(), view.getMeasuredHeight());
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
                publishProgress(new Object[]{view, picSrc});
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects) {
            SimpleDraweeView sdvPic = (SimpleDraweeView) objects[0];
            layout.addView(sdvPic);
            String picUrl = (String) objects[1];
            sdvPic.setImageURI(Uri.parse(picUrl));

        }
    }
}

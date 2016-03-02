package com.xue.siu.common.view.asynclist;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.siu.common.util.HandleUtil;
import com.xue.siu.module.news.model.CommentVO;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by XUE on 2016/3/1.
 */
public class AsyncListView extends LinearLayout {
    private BaseAdapter mAdapter;

    public AsyncListView(Context context) {
        this(context, null);
    }

    public AsyncListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        setOrientation(VERTICAL);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        fillLayout();
    }

    private void fillLayout() {
        new FillLayoutTask(this, mAdapter).execute();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private static class FillLayoutTask extends AsyncTask<Void, Object, Void> {
        private LinearLayout layout;
        private BaseAdapter adapter;

        public FillLayoutTask(LinearLayout layout, BaseAdapter adapter) {
            this.layout = layout;
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < adapter.getCount(); i++) {
                final View view = adapter.getView(i, null, layout);
                if (view == null) {
                    return null;
                }
                publishProgress(new Object[]{view,adapter.getItem(i)});
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            View view = (View)values[0];
            CommentVO text = (CommentVO)values[1];
            if (view.getParent() == null) {
                ((TextView)view).setText(text.getContent());
                layout.addView(view);
            }
        }
    }
}

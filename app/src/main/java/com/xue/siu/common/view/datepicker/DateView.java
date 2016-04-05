package com.xue.siu.common.view.datepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.xue.siu.R;


/**
 * Created by Administrator on 2015/6/15.
 */
public class DateView extends android.widget.FrameLayout implements OnCalendarClickHandler {
    public static final int MODE_WEEK = 1;
    public static final int MODE_MONTH = 2;

    private TextView mTextView;
    private DateScrollView mDateScrollView;
    private int mTextViewHeight;

    private OnCalendarClickHandler mCalendarHandler;

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    private void init() {
        mTextViewHeight = getResources().getDimensionPixelSize(R.dimen.sf_picker_row_height);
        mTextView = new TextView(getContext());
        mTextView.setTextSize(15);
        mTextView.setTextColor(getResources().getColor(R.color.sf_picker_date_text_color));
        mTextView.getPaint().setFakeBoldText(true);

        mTextView.setText(getString(TimeUtil.getYear(), TimeUtil.getMonth()));
        mTextView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mTextViewHeight);
        addView(mTextView, params);
        mDateScrollView = new DateScrollView(getContext());
        mDateScrollView.setHorizontalScrollBarEnabled(false);
        mDateScrollView.setSuccessor(this);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mDateScrollView, params);
    }


    private String getString(int year, int month) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(year);
//        sb.append("年").append(month).append("月");
        String format = "%1$d年%2$d月";
        return String.format(format,new Object[]{year,month});
    }

    public void toggle() {

        mDateScrollView.changeMode();
    }

    protected void showTextView() {
        mTextView.setVisibility(View.VISIBLE);
    }

    protected void hideTextView() {
        mTextView.setVisibility(View.GONE);
    }

    protected void setTextViewAlpha(float alpha) {
        ViewHelper.setAlpha(mTextView, alpha);
    }

    @Override
    public void setSuccessor(OnCalendarClickHandler handler) {
        mCalendarHandler = handler;
    }

    @Override
    public void handleCalendarClick(DateBean dateBean) {

        mTextView.setText(getString(dateBean.y, dateBean.m));
        if (mCalendarHandler != null)
            mCalendarHandler.handleCalendarClick(dateBean);
    }


    public interface OnAnimationListener {
        void onAnimationStart();

        void onAnimationEnd();
    }

    public void setAnimationListener(OnAnimationListener on) {
        mDateScrollView.setAnimationListener(on);
    }


}

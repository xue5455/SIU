package com.xue.siu.common.view.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xue.siu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/15.
 */
class DatePicker extends View implements View.OnTouchListener, View.OnClickListener, OnCalendarClickHandler {
    private OnCalendarClickHandler mCalendarHandler;
    private OnMonthModeClickListener mMonthModeClickListener;
    public static final String[] WEEK_TEXT = {"周日", "周一", "周二", "周三", "周四",
            "周五", "周六"};
    private static int mSystemYear = TimeUtil.getYear(), mSystemMonth = TimeUtil.getMonth(), mSystemDay = TimeUtil.getDay();
    private static int mMode;
    private static float mTextSize;
    private static float mTopTextSize;
    private static boolean mShowMark = true;
    private static final Paint mGreyPaint = new Paint();
    private static final Paint mBluePaint = new Paint();
    private static final Paint mBlackPaint = new Paint();
    private static final Paint mRedPaint = new Paint();
    private static final Paint mWhitePaint = new Paint();
    private static final Paint mTopTextPaint = new Paint();
    private static int mWeekTextColor;
    private static int mMonthTextColor;
    private static boolean mOnCreate = true;
    private static int mRowHeight;
    private static int mColWidth;
    //日期相关
    private List<DateBean> mDayList = new ArrayList<DateBean>();// 记录七天
    private List<MonthDay> mMonthDayList = new ArrayList<MonthDay>();// 记录42天
    private DateBean mDateBean;
    private int mCurrentPosition = -1;
    private int mSelectedPosition = -1;
    private int mTouchX, mTouchY;
    private int mTouchRowNum, mTouchColNum;
    private MyLayout mParent;
    public static final DateBean SELECTEDDAY = new DateBean(mSystemYear, mSystemMonth, mSystemDay);


    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    @Override
    public void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        mColWidth = getMeasuredWidth() / 7;
        mParent = (MyLayout) getParent();
        setOnTouchListener(this);
        setOnClickListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawWeekText(canvas);
        switch (mMode) {
            case DateView.MODE_MONTH:
                drawMonthText(canvas);
                drawMonthContent(canvas);
                break;
            case DateView.MODE_WEEK:
                drawWeekContent(canvas);
                break;
        }
        if (mShowMark) {
            markCurrentDate(canvas);
            markSelectedDay(canvas);
        }
    }


    private void drawMonthText(Canvas canvas) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(mDateBean.y));
        sb.append("年");
        sb.append(String.valueOf(mDateBean.m));
        sb.append("月");
        float x = getMeasuredWidth() / 2.0f
                - mBlackPaint.measureText(sb.toString())
                / 2.0f;
        float y = 0.5f * mRowHeight
                - (mBlackPaint.ascent() + mBlackPaint.descent()) / 2.0f;

        ;

        canvas.drawText(sb.toString(), x, y, mTopTextPaint);

    }

    private void drawMonthContent(Canvas canvas) {
        int height = getMeasuredHeight() / 8;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++) {
                MonthDay day = mMonthDayList.get(i * 7 + j);
                String s = String.valueOf(day.day);
                float x = mColWidth / 2.0f
                        - mGreyPaint.measureText(s) / 2.0f
                        + mColWidth * j;
                float y = 2.5f * height
                        - (mGreyPaint.ascent() + mGreyPaint.descent()) / 2.0f
                        + height * i;
                y = (float) Math.max(2.5 * mRowHeight, y);
                if (day.isCurrentMonth)
                    canvas.drawText(s, x, y, mBlackPaint);
                else
                    canvas.drawText(s, x, y, mGreyPaint);
            }

    }

    private void drawWeekText(Canvas canvas) {
        float x = 0;
        float y = 1.5f * mRowHeight;
        if (mMode == DateView.MODE_MONTH)
            y = 1.5f * mRowHeight;
        int j = 0;
        y -= (mGreyPaint.ascent() + mGreyPaint.descent()) / 2.0f;
        for (String str : WEEK_TEXT) {
            x = mColWidth * (j + 0.5f) - mGreyPaint.measureText("周一") / 2.0f;
            canvas.drawText(str, x, y, mGreyPaint);
            j++;
        }
    }

    private void drawWeekContent(Canvas canvas) {
        int height = getMeasuredHeight() / 3;
        for (int i = 0; i < 7; i++) {
            DateBean d = mDayList.get(i);
            int j = TimeUtil.getWeek(d.y, d.m, d.d);
            float x = (j - 0.5f) * mColWidth
                    - mBlackPaint.measureText(String.valueOf(d.d)) / 2.0f;
            float y = 2.5f * height
                    - (mBlackPaint.ascent() + mBlackPaint.descent()) / 2.0f;
            canvas.drawText(String.valueOf(d.d), x, y, mBlackPaint);
        }
    }

    private void markCurrentDate(Canvas canvas) {

        float radius = mBlackPaint.getTextSize();
        DateBean systemDate = new DateBean(mSystemYear, mSystemMonth, mSystemDay);
        if (mMode == DateView.MODE_WEEK) {
            DateBean d;
            for (int i = 0; i < 7; i++) {
                d = mDayList.get(i);
                if (d.equals(systemDate)) {
                    if (SELECTEDDAY != null
                            && !SELECTEDDAY.equals(systemDate)) {

                        int j = TimeUtil.getWeek(mSystemYear, mSystemMonth, mSystemDay);
                        float x = (j - 0.5f) * mColWidth;
                        float y = 2.5f * mRowHeight;

                        canvas.drawCircle(x, y, radius, mBluePaint);
                        //  blackPaint.setColor(WHITE);
                        String str = String.valueOf(d.d);
                        x = mColWidth * (j - 0.5f)
                                - mWhitePaint.measureText(str) / 2.0f;
                        y = 2.5f * mRowHeight
                                - (mWhitePaint.ascent() + mWhitePaint.descent())
                                / 2.0f;
                        canvas.drawText(str, x, y, mWhitePaint);

                    }
                }
                if (i == mDayList.size() - 1)
                    return;
            }
        } else {
            MonthDay d = null;
            if (SELECTEDDAY.equals(systemDate)) {
                return;
            }


            for (int i = 0; i < mMonthDayList.size(); i++) {
                d = mMonthDayList.get(i);

                if (d.year == mSystemYear && d.month == mSystemMonth
                        && d.day == mSystemDay) {

                    if (SELECTEDDAY != null
                            && !SELECTEDDAY.equals(systemDate)) {
                        mCurrentPosition = i;
                        break;
                    }

                }
                if (i == mMonthDayList.size() - 1)
                    return;
            }


            int y = mCurrentPosition / 7;
            int x = mCurrentPosition % 7;
            float xx = mColWidth * (x + 0.5f);
            float yy = (2.5f + y) * mRowHeight;
            canvas.drawCircle(xx, yy, radius, mBluePaint);
            String str = String.valueOf(mSystemDay);
            xx = mColWidth * (x + 0.5f)
                    - mWhitePaint.measureText(str) / 2.0f;
            yy = (2.5f + y) * mRowHeight
                    - (mWhitePaint.ascent() + mWhitePaint.descent()) / 2.0f;
            canvas.drawText(str, xx, yy, mWhitePaint);
        }
    }

    private void markSelectedDay(Canvas cns) {
        float radius = mBlackPaint.getTextSize();
        if (mMode == DateView.MODE_WEEK) {
            int i = TimeUtil.getWeek(SELECTEDDAY.y,
                    SELECTEDDAY.m, SELECTEDDAY.d);
            int j = TimeUtil.getWeek(mDateBean.y, mDateBean.m, mDateBean.d);
            DateBean d;
            if (i > j) {
                d = mDayList.get(i - j);

            } else if (i < j) {
                d = mDayList.get(7 - i);
            } else {
                d = mDayList.get(0);
            }
            float x = mColWidth * (i - 0.5f);
            float y = 2.5f * mRowHeight;

            cns.drawCircle(x, y, radius, mRedPaint);
            String str = String.valueOf(d.d);
            x = mColWidth * (i - 0.5f) - mWhitePaint.measureText(str)
                    / 2.0f;
            y = 2.5f * mRowHeight - (mWhitePaint.ascent() + mWhitePaint.descent())
                    / 2.0f;
            cns.drawText(str, x, y, mWhitePaint);
            return;
        } else {
            for (int i = 0; i < mMonthDayList.size(); i++) {
                MonthDay day = mMonthDayList.get(i);
                if (SELECTEDDAY.y == day.year
                        && SELECTEDDAY.m == day.month
                        && SELECTEDDAY.d == day.day) {
                    mSelectedPosition = i;
                    break;

                }
                if (i == mMonthDayList.size() - 1)
                    return;
            }
        }

        int y = mSelectedPosition / 7;
        int x = mSelectedPosition % 7;
        float xx = mColWidth * (x + 0.5f);
        float yy = (2.5f + y) * mRowHeight;

        cns.drawCircle(xx, yy, radius, mRedPaint);
        String str = String.valueOf(SELECTEDDAY.d);
        xx = mColWidth * (x + 0.5f)
                - mWhitePaint.measureText(str)
                / 2.0f;
        yy = (2.5f + y) * mRowHeight
                - (mWhitePaint.ascent() + mWhitePaint.descent()) / 2.0f;
        cns.drawText(str, xx, yy, mWhitePaint);
    }

    protected void setMode(int mode) {
        mMode = mode;
    }

    private void init() {
        if (mOnCreate) {
            mOnCreate = false;
            mRowHeight = getResources().getDimensionPixelSize(R.dimen.sf_picker_row_height);
            mTextSize = getResources().getDimensionPixelSize(R.dimen.sf_picker_text_size);
            mTopTextSize = getResources().getDimensionPixelSize(R.dimen.sf_picker_top_text_size);
            initPaint();
        }
    }

    private void initPaint() {
        mWeekTextColor = getResources().getColor(R.color.sf_picker_week_text_color);
        mMonthTextColor = getResources().getColor(R.color.sf_picker_date_text_color);
        mGreyPaint.setAntiAlias(true);
        mGreyPaint.setColor(mWeekTextColor);
        mGreyPaint.setTextSize(mTextSize);
        mBluePaint.setAntiAlias(true);
        mBluePaint.setColor(getResources().getColor(R.color.sf_picker_blue));
        mBlackPaint.setAntiAlias(true);
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setTextSize(mTextSize);
        mRedPaint.setAntiAlias(true);
        mRedPaint.setColor(getResources().getColor(R.color.sf_picker_red));
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setTextSize(mTextSize);
        mTopTextPaint.setColor(mMonthTextColor);
        mTopTextPaint.setTextSize(mTopTextSize);
        mTopTextPaint.setAntiAlias(true);
        mTopTextPaint.setFakeBoldText(true);
    }

    /**
     * 周模式部分
     */
    private void initWeekList() {
        mDayList = new ArrayList<DateBean>();
        int d = mDateBean.d;
        int y = mDateBean.y;
        int m = mDateBean.m;
        int weekDayOfNow = TimeUtil.getWeek(y, m, d);

        mDayList.add(new DateBean(y, m, d));
        for (int i = 0; i < 7 - weekDayOfNow; i++) {
            d++;
            if (d > TimeUtil.getDaysOfMonth(y, m)) {
                d = 1;
                m = m == 12 ? 1 : m + 1;
                y = m == 1 ? y + 1 : y;
            }

            mDayList.add(new DateBean(y, m, d));
        }
        y = mDateBean.y;
        m = mDateBean.m;
        d = mDateBean.d;
        for (int i = 0; i < weekDayOfNow - 1; i++) {
            d--;
            if (d == 0) {
                m = m == 1 ? 12 : m - 1;
                y = m == 12 ? y - 1 : y;
                d = TimeUtil.getDaysOfMonth(y, m);
            }
            mDayList.add(new DateBean(y, m, d));
        }
    }

    private void initMonthList() {
        mMonthDayList = new ArrayList<MonthDay>();
        int year = mDateBean.y;
        int month = mDateBean.m;
        int week = TimeUtil.getWeek(year, month);
        int lastYear = month == 1 ? year - 1 : year;
        int lastMonth = month == 1 ? 12 : month - 1;
        int nextYear = month == 12 ? year + 1 : year;
        int nextMonth = month == 12 ? 1 : month + 1;
        int daysOfCurrentMonth = TimeUtil.getDaysOfMonth(year, month);
        int daysOfLastMonth = TimeUtil.getDaysOfMonth(lastYear, lastMonth);
        if (week == 1) {
            /* 这个月第一天为周日 */
            int k = daysOfLastMonth - 6;
            /* 初始化第一行 */
            for (int i = 0; i < 7; i++) {
                MonthDay day = new MonthDay(false, lastYear, lastMonth, k);
                mMonthDayList.add(day);
                k++;
            }
            /* 初始化剩余5行 */
            /* 本月 */
            k = 1;
            for (int j = 0; j < daysOfCurrentMonth; j++) {
                MonthDay day = new MonthDay(true, year, month, k);
                mMonthDayList.add(day);
                k++;
            }
            /* 下个月 */
            k = 1;
            int j = mMonthDayList.size();

            for (int i = 0; i < 42 - j; i++) {
                MonthDay day = new MonthDay(false, nextYear, nextMonth, k);
                mMonthDayList.add(day);
                k++;
            }
        } else {
            /* 本月第一天不是周日 */
            /* 上个月 */
            int j = week - 1;
            int k = daysOfLastMonth - j + 1;
            for (int i = 0; i < j; i++) {
                MonthDay day = new MonthDay(false, lastYear, lastMonth, k);
                mMonthDayList.add(day);
                k++;
            }
            /* 这个月 */
            k = 1;
            for (int i = 0; i < daysOfCurrentMonth; i++) {
                MonthDay day = new MonthDay(true, year, month, k);
                mMonthDayList.add(day);
                k++;
            }
            /* 下个月 */
            j = mMonthDayList.size();
            k = 1;
            for (int i = 0; i < 42 - j; j++) {
                MonthDay day = new MonthDay(false, nextYear, nextMonth, k);
                mMonthDayList.add(day);
                k++;
            }
        }
    }

    public void setDate(DateBean dateBean) {
        mDateBean = dateBean;
        mMonthDayList = null;
        mDayList = null;
        switch (mMode) {
            case DateView.MODE_WEEK:
                initWeekList();
                break;
            case DateView.MODE_MONTH:
                initMonthList();
                break;
        }
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = (int) ev.getX();
                mTouchY = (int) ev.getY();

                break;

        }
        return false;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        getSelectedDay(mTouchX, mTouchY);

        switch (mMode) {
            case DateView.MODE_MONTH:

                if (mTouchRowNum <= 1)
                    return;
                int k = (mTouchRowNum - 2) * 7 + mTouchColNum - 1;
                MonthDay monthDay = mMonthDayList.get(k);
                DateBean d = new DateBean(monthDay.year, monthDay.month, monthDay.day);
                SELECTEDDAY.setDay(d);

                break;
            case DateView.MODE_WEEK:
                int j = TimeUtil.getWeek(mDateBean.y, mDateBean.m, mDateBean.d);
                if (mTouchRowNum <= 1)
                    return;

                if (mTouchColNum > j) {
                    SELECTEDDAY.setDay(mDayList.get(mTouchColNum - j));

                } else if (mTouchColNum < j) {
                    SELECTEDDAY.setDay(mDayList.get(7 - mTouchColNum));
                } else {
                    SELECTEDDAY.setDay(mDayList.get(0));
                }

                break;
        }

        handleCalendarClick(SELECTEDDAY);
        mParent.updateChild();
        if (mMode == DateView.MODE_MONTH && mMonthModeClickListener != null)
            mMonthModeClickListener.onMonthModeClick();

    }

    private void getSelectedDay(int x, int y) {
        mTouchRowNum = y / mRowHeight;
        mTouchColNum = x / mColWidth + 1;

    }


    @Override
    public void setSuccessor(OnCalendarClickHandler handler) {
        mCalendarHandler = handler;
    }

    @Override
    public void handleCalendarClick(DateBean dateBean) {
        if (mCalendarHandler != null)
            mCalendarHandler.handleCalendarClick(dateBean);
    }


    public void setSelectedDay(DateBean dateBean) {
        SELECTEDDAY.setDay(dateBean);
    }

    public DateBean getSelectedDay() {
        return SELECTEDDAY;
    }

    protected void setShowMark(boolean bool) {
        mShowMark = bool;
    }

    private class MonthDay {
        public boolean isCurrentMonth;
        public int day;
        public int year;
        public int month;

        public MonthDay(boolean arg0, int y, int m, int d) {
            isCurrentMonth = arg0;
            day = d;
            year = y;
            month = m;
        }
    }

    protected void setMonthModeListener(OnMonthModeClickListener on) {
        mMonthModeClickListener = on;
    }

    protected interface OnMonthModeClickListener {
        public void onMonthModeClick();
    }
}

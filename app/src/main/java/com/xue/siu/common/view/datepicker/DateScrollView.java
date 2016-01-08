package com.xue.siu.common.view.datepicker;


import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import com.nineoldandroids.view.ViewHelper;
import com.xue.siu.R;


/**
 * Created by Administrator on 2015/6/15.
 */
class DateScrollView extends android.widget.HorizontalScrollView implements
        LayoutChangeListener, OnCalendarClickHandler, DatePicker.OnMonthModeClickListener {
    private boolean mTouchable = true;
    private DateView.OnAnimationListener mAnimationListener;
    //日期相关
    /**
     * 定义一些与日期有关的变量
     */
    /*
    protected static DateBean mLastSelectedDate;
    protected static DateBean mCurrentSelectedDate;
    */
    private int c_year;// 中间页面的年份
    private int c_month;// 中间页面的月份
    private int c_day;// 中间页面的日期 ：周模式专用
    private int l_year;// 左边一页的年份
    private int l_month;// 左边一页的月份
    private int l_day;// 左边一页的日期:周模式专用
    private int r_year;// 右边一页的年份
    private int r_month;// 右边一页的月份
    private int r_day;// 右边一页的日:周模式专用
    private int sys_year = TimeUtil.getYear();// 系统当前年份
    private int sys_month = TimeUtil.getMonth();// 系统当前月份
    private int sys_day = TimeUtil.getDay();// 系统当前日期

    /**
     * 速度获取器
     */
    private VelocityTracker mVelocityTracker;
    private static final float MINOR_VELOCITY = 300.0f;
    private int mMinorVelocity;
    private static final int VELOCITY_UNITS = 1000;
    private int mVelocityUnits;
    private int focusX1;
    private int focusX2;
    private int mMode = DateView.MODE_WEEK;

    private ViewGroup mContainer;
    private int mRowHeight;
    private MyScroller mScroller;
    private int mWeekModeHeight;
    private int mMonthModeHeight;
    private boolean mOnCreate = true;
    private View mCurrentView;
    private DateView mParent;
    private OnCalendarClickHandler mCalendarHandler;

    public DateScrollView(android.content.Context context) {
        this(context, null);

    }

    public DateScrollView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mRowHeight = getResources().getDimensionPixelSize(R.dimen.sf_picker_row_height);
        mWeekModeHeight = 3 * mRowHeight;
        mMonthModeHeight = 8 * mRowHeight;
        mScroller = new MyScroller(getContext());
        mContainer = new MyLayout(getContext());
        ((MyLayout) (mContainer)).addOnLayoutChangeListener(this);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        final float fDensity = getResources().getDisplayMetrics().density;
        mVelocityUnits = (int) (VELOCITY_UNITS * fDensity + 0.5f);
        mMinorVelocity = (int) (MINOR_VELOCITY * fDensity + 0.5f);
        initDate();
    }

    private void initDate() {
        c_year = sys_year;
        c_month = sys_month;
        c_day = sys_day;
        // mCurrentSelectedDate = new DateBean(c_year, c_month, c_day);
        //   mLastSelectedDate = new DateBean(c_year, c_month, c_day);
        initDates();
    }

    private void initDates() {
        if (mMode == DateView.MODE_WEEK) {
            if (c_day + 7 > TimeUtil.getDaysOfMonth(c_year, c_month)) {
                r_month = c_month == 12 ? 1 : c_month + 1;
                r_year = c_month == 12 ? c_year + 1 : c_year;
                r_day = c_day + 7 - TimeUtil.getDaysOfMonth(c_year, c_month);
            } else {
                r_month = c_month;
                r_year = c_year;
                r_day = c_day + 7;
            }
            if (c_day - 7 <= 0) {
                l_month = c_month == 1 ? 12 : c_month - 1;
                l_year = c_month == 1 ? c_year - 1 : c_year;
                l_day = TimeUtil.getDaysOfMonth(l_year, l_month) + c_day - 7;
            } else {
                l_month = c_month;
                l_year = c_year;
                l_day = c_day - 7;
            }

        } else {
            l_month = c_month == 1 ? 12 : c_month - 1;
            l_year = l_month == 12 ? c_year - 1 : c_year;
            r_month = c_month == 12 ? 1 : c_month + 1;
            r_year = r_month == 1 ? c_year + 1 : c_year;

        }
    }

    @Override
    public void onMeasure(int width, int height) {

        super.onMeasure(width, height);
        if (mOnCreate) {
            mOnCreate = false;
            mParent = (DateView) getParent();
            addRight(newView(l_year, l_month, l_day));
            addRight(newView(c_year, c_month, c_day));
            addRight(newView(r_year, r_month, r_day));
            mCurrentView = mContainer.getChildAt(1);
        }
    }


    protected void setMode(int mode) {
        mMode = mode;
    }

    @Override
    public void computeScroll() {
        scrollUpdate();
        super.computeScroll();
    }

    private void scrollUpdate() {
        if (mScroller.computeScrollOffset()) {
            ViewGroup.LayoutParams params = mCurrentView.getLayoutParams();
            params.height = mScroller.getCurrY();
            mCurrentView.setLayoutParams(params);
            float alpha;
            if (mScroller.getStartY() == mWeekModeHeight) {
                int deltaY = mScroller.getCurrY() - mScroller.getStartY();
                alpha = 1.0f - 1.0f * deltaY / (mScroller.getFinalY() - mScroller.getStartY());

            } else {
                int deltaY = mScroller.getCurrY() - mScroller.getFinalY();
                alpha = 1.0f * deltaY / (mScroller.getStartY() - mScroller.getFinalY());
            }
            ViewHelper.setAlpha(mCurrentView, alpha);

            mParent.setTextViewAlpha(alpha);
        }
    }

    @Override
    public void onLayoutChange() {
        toPage(1);
    }

    /**
     * 跳转到指定的页面
     *
     * @param index 跳转的页码
     * @return
     */
    public void toPage(int index) {

        if (index < 0 || index >= mContainer.getChildCount())
            return;
        smoothScrollTo(getChildLeft(index), 0);

    }


    /**
     * 向右边添加View
     *
     * @param view 需要添加的View
     * @return true添加成功|false添加失败
     */
    public void addRight(View view) {

        mContainer.addView(view);

    }

    /**
     * 向左边添加View
     *
     * @param view 需要添加的View
     * @return true添加成功|false添加失败
     */
    public void addLeft(View view) {
        mContainer.addView(view, 0);
        int tmpwidth = view.getLayoutParams().width;
        if (tmpwidth == 0)
            tmpwidth = getMeasuredWidth();
        this.scrollTo(this.getScrollX() + tmpwidth, 0);


    }

    private View newView(int year, int month, int day) {
        android.widget.LinearLayout.LayoutParams params = null;
        DatePicker d = new DatePicker(getContext());
        switch (mMode) {
            case DateView.MODE_WEEK:
                params = new android.widget.LinearLayout.LayoutParams(
                        getMeasuredWidth(), mWeekModeHeight);

                d.setMode(DateView.MODE_WEEK);
                break;
            case DateView.MODE_MONTH:
                params = new android.widget.LinearLayout.LayoutParams(
                        getMeasuredWidth(), mMonthModeHeight);

                d.setMode(DateView.MODE_MONTH);
                break;
        }
        d.setSuccessor(this);
        d.setDate(new DateBean(year, month, day));
        d.setLayoutParams(params);
        d.setMonthModeListener(this);
        return d;
    }

    /**
     * 左翻页
     */
    private void prevPage() {
        if (mMode == DateView.MODE_WEEK) {

            setPreSelectedDay();

        }
        handleCalendarClick(getSelectedDay());
        setPrePage();
        addLeft(newView(l_year, l_month, l_day));
        removeRight();
        mCurrentView = mContainer.getChildAt(1);
        /*
        d1 = (DayPicker) container.getChildAt(1);
        d2 = (DayPicker) container.getChildAt(0);
        d3 = (DayPicker) container.getChildAt(2);
        d1.invalidate();
        d2.invalidate();
        d3.invalidate();
        */
        //L.i("selectedDay:"+selectedDay.getYear()+"-"+selectedDay.getMonth()+"-"+selectedDay.getDay());
    }

    /**
     * 右翻页
     */
    private void nextPage() {
        if (mMode == DateView.MODE_WEEK) {
            //  mLastSelectedDate.y = mCurrentSelectedDate.y;
            //   mLastSelectedDate.m = mCurrentSelectedDate.m;
            //   mLastSelectedDate.d = mCurrentSelectedDate.d;
            setNextSelectedDay();
            //  if (mOnDayChange != null)
            //    mOnDayChange.onChange(mCurrentSelectedDate);
        }
        handleCalendarClick(getSelectedDay());
        setNextPage();
        addRight(newView(r_year, r_month, r_day));
        removeLeft();
        mCurrentView = mContainer.getChildAt(1);
        /*
        d1 = (DayPicker) container.getChildAt(1);
        d2 = (DayPicker) container.getChildAt(0);
        d3 = (DayPicker) container.getChildAt(2);
        d1.invalidate();
        d2.invalidate();
        d3.invalidate();
        */
        //L.i("selectedDay:"+selectedDay.getYear()+"-"+selectedDay.getMonth()+"-"+selectedDay.getDay());
    }

    private void setNextSelectedDay() {
        int day = getSelectedDay().d;
        int month = getSelectedDay().m;
        int year = getSelectedDay().y;
        if (day + 7 > TimeUtil.getDaysOfMonth(year, month)) {
            day = day + 7 - TimeUtil.getDaysOfMonth(year, month);
            month = month == 12 ? 1 : month + 1;
            year = month == 1 ? year + 1 : year;
        } else {
            day = day + 7;
        }
        setSelectedDay(new DateBean(year, month, day));
        // mCurrentSelectedDate.d = day;
        //  mCurrentSelectedDate.y = year;
        // mCurrentSelectedDate.m = month;

    }

    private void setPreSelectedDay() {
        int day = getSelectedDay().d;
        int month = getSelectedDay().m;
        int year = getSelectedDay().y;
        int l_m = month == 1 ? 12 : month - 1;
        int l_y = month == 12 ? year - 1 : year;
        if (day - 7 <= 0) {
            day = day - 7 + TimeUtil.getDaysOfMonth(l_y, l_m);
            month = l_m;
            year = l_y;
        } else {
            day = day - 7;
        }
        //     mCurrentSelectedDate.d = day;
        //    mCurrentSelectedDate.y = year;
        //    mCurrentSelectedDate.m = month;
        setSelectedDay(new DateBean(year, month, day));
    }

    /**
     * 计算前一页的日期
     */
    private void setPrePage() {
        if (mMode == DateView.MODE_WEEK) {
            l_month = c_month == 1 ? 12 : c_month - 1;
            l_year = c_year == 1 ? c_year - 1 : c_year;
            if (c_day - 7 <= 0) {
                c_month = l_month;
                c_year = l_year;
                c_day = TimeUtil.getDaysOfMonth(l_year, l_month) + c_day - 7;
                l_day = c_day - 7;
            } else {
                c_day = c_day - 7;
                if (c_day - 7 <= 0) {
                    l_day = TimeUtil.getDaysOfMonth(l_year, l_month) + c_day
                            - 7;
                } else {
                    l_day = c_day - 7;
                    l_year = c_year;
                    l_month = c_month;
                }
            }
        } else {

            c_month = c_month == 1 ? 12 : c_month - 1;
            c_year = c_month == 12 ? c_year - 1 : c_year;
            l_month = c_month == 1 ? 12 : c_month - 1;
            l_year = l_month == 12 ? c_year - 1 : c_year;

        }
    }

    /**
     * 计算下一页的日期
     */
    private void setNextPage() {
        if (mMode == DateView.MODE_WEEK) {
            r_month = c_month == 12 ? 1 : c_month + 1;
            r_year = c_month == 12 ? c_year + 1 : c_year;
            r_day = 0;
            if (c_day + 7 > TimeUtil.getDaysOfMonth(c_year, c_month)) {
                c_day = c_day + 7 - TimeUtil.getDaysOfMonth(c_year, c_month);
                r_day = c_day + 7;
                c_month = r_month;
                c_year = r_year;
            } else {
                c_day += 7;
                if (c_day + 7 > TimeUtil.getDaysOfMonth(c_year, c_month)) {
                    r_day = c_day + 7
                            - TimeUtil.getDaysOfMonth(c_year, c_month);
                } else {
                    r_month = c_month;
                    r_year = c_year;
                    r_day = c_day + 7;
                }
            }
        } else {
            c_month = c_month == 12 ? 1 : c_month + 1;
            c_year = c_month == 1 ? c_year + 1 : c_year;
            r_month = c_month == 12 ? 1 : c_month + 1;
            r_year = r_month == 1 ? c_year + 1 : c_year;
        }
    }

    /**
     * 删除右边的View
     *
     * @return true成功|false失败
     */
    public void removeRight() {

        mContainer.removeViewAt(mContainer.getChildCount() - 1);

    }

    /**
     * 删除左边的View
     *
     * @return true成功|false失败
     */
    public void removeLeft() {
        /* 因为在左边删除了View，因此所有View的x坐标都会减少，因此需要让ScrollView也跟着移动。 */
        // 如果pageNo
        int tmpWidth = mContainer.getChildAt(0).getWidth();
        mContainer.removeViewAt(0);
        this.scrollTo(this.getScrollX() - tmpWidth, 0);


    }

    /**
     * 跳转到指定的页面
     *
     * @param index 跳转的页码
     * @return
     */
    public void scrollToPage(int index) {

        if (index < 0 || index >= mContainer.getChildCount())
            return;
        smoothScrollTo(getChildLeft(index), 0);
    }

    /* 获取某个孩子的X坐标 */
    private int getChildLeft(int index) {
        if (index >= 0 && mContainer != null) {
            if (index < mContainer.getChildCount())
                return mContainer.getChildAt(index).getLeft();
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!mTouchable)
            return true;
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //L.i("count:" + container.getChildCount());
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(mVelocityUnits);
                int velocityX = (int) velocityTracker.getXVelocity();
                if (velocityX >= mMinorVelocity) {
                    prevPage();
                } else if (velocityX <= -mMinorVelocity) {
                    nextPage();
                } else {
                    focusX2 = (int) ev.getX();
                    int dx = focusX1 - focusX2;
                    if (Math.abs(dx) > 0.4 * getMeasuredWidth()) {
                        if (dx < 0) {
                        /* 向左 */
                            prevPage();
                        } else {
                        /* 向右 */
                            nextPage();
                        }
                    } else {
                    /* 不变 */

                        scrollToPage(1);
                    }
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                return true;

        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            focusX1 = (int) ev.getX();
        return super.dispatchTouchEvent(ev);
    }


    protected void changeMode() {
        if (!mScroller.isFinished())
            mScroller.abortAnimation();

        switch (mMode) {
            case DateView.MODE_WEEK:
                enlarge();
                break;
            case DateView.MODE_MONTH:
                diminish();
                break;
        }
    }

    private void diminish() {

        View v = mContainer.getChildAt(0);
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = mWeekModeHeight;
        v.setLayoutParams(params);
        v = mContainer.getChildAt(2);
        params = v.getLayoutParams();
        params.height = mWeekModeHeight;
        v.setLayoutParams(params);

        mScroller.setOnScrollerListener(new MyScroller.ScrollerListener() {
            @Override
            public void onScrollerEnd() {
                mTouchable = true;
                setShowMark(true);
                ViewHelper.setAlpha(mCurrentView, 1.0f);
                mParent.setTextViewAlpha(1.0f);
                mParent.showTextView();
                mMode = DateView.MODE_WEEK;
                updateCurrentDate();
                initDates();

                DatePicker d = (DatePicker) mContainer.getChildAt(0);
                d.setMode(mMode);
                d.setDate(new DateBean(l_year, l_month, l_day));

                d = (DatePicker) mContainer.getChildAt(1);
                d.setMode(mMode);
                d.setDate(new DateBean(c_year, c_month, c_day));

                d = (DatePicker) mContainer.getChildAt(2);
                d.setMode(mMode);
                d.setDate(new DateBean(r_year, r_month, r_day));

                if (mAnimationListener != null)
                    mAnimationListener.onAnimationEnd();
            }

            @Override
            public void onScrollStart() {
                if (mAnimationListener != null)
                    mAnimationListener.onAnimationStart();
                setShowMark(false);
                mTouchable = false;
            }
        });
        mScroller.startScroll(0, mMonthModeHeight, 0, mWeekModeHeight - mMonthModeHeight, 350);
        invalidate();
    }

    protected void enlarge() {


        mScroller.setOnScrollerListener(new MyScroller.ScrollerListener() {
            @Override
            public void onScrollerEnd() {
                mTouchable = true;
                setShowMark(true);
                ViewHelper.setAlpha(mCurrentView, 1.0f);
                mParent.hideTextView();
                mMode = DateView.MODE_MONTH;

                updateCurrentDate();
                initDates();
                ViewGroup.LayoutParams params;
                //中间
                DatePicker d = (DatePicker) mContainer.getChildAt(1);
                d.setMode(mMode);
                d.setDate(new DateBean(c_year, c_month, c_day));

                //左边
                d = (DatePicker) mContainer.getChildAt(0);
                params = d.getLayoutParams();
                params.height = mMonthModeHeight;
                d.setMode(mMode);
                d.setDate(new DateBean(l_year, l_month, l_day));

                //右边
                d = (DatePicker) mContainer.getChildAt(2);
                d.setLayoutParams(params);
                d.setMode(mMode);
                d.setDate(new DateBean(r_year, r_month, r_day));


                if (mAnimationListener != null)
                    mAnimationListener.onAnimationEnd();
            }

            @Override
            public void onScrollStart() {
                setShowMark(false);
                mTouchable = false;
                if (mAnimationListener != null)
                    mAnimationListener.onAnimationStart();
            }
        });
        mScroller.startScroll(0, mWeekModeHeight, 0, mMonthModeHeight - mWeekModeHeight, 350);
        invalidate();
    }


    protected void setAnimationListener(DateView.OnAnimationListener listener) {
        mAnimationListener = listener;
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

    private DateBean getSelectedDay() {
        return ((DatePicker) mContainer.getChildAt(1)).getSelectedDay();
    }

    private void setSelectedDay(DateBean dateBean) {
        ((DatePicker) mContainer.getChildAt(1)).setSelectedDay(dateBean);
    }

    private void updateCurrentDate() {
        c_year = getSelectedDay().y;
        c_month = getSelectedDay().m;
        c_day = getSelectedDay().d;
    }

    private void setShowMark(boolean arg0) {
        ((DatePicker) mContainer.getChildAt(1)).setShowMark(arg0);
    }

    @Override
    public void onMonthModeClick() {
        changeMode();
    }
}

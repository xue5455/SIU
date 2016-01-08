package com.xue.siu.common.view.progressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xue.siu.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DING on 15/12/7.
 */
public class ArcProgressbar extends View {
    /**
     * 圆环半径
     */
    private int mRadius = 115; // Diameter英文为直径，该常量表示小圆直径的dp值
    /**
     * 圆环的宽度
     */
    private int mTrokeWidth = 15;
    /**
     * 起始角度
     */
    private int mStartAngle = 135;
    /**
     * 进度条进度颜色
     */
    private int mArcColor;

    private Paint mPaint;
    private int mProgress;// 表示进度
    private RectF mRect;

    private ObjectAnimator rotateAnimator;
    private Timer timer;
    private int startAngle = 0;
    private int progress = 0;
    private boolean isPositive = true;
    private int ADD_ON_DEGRESS = 2;
    private int MAX_DEGREE = 280;

    public ArcProgressbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgressbar, defStyle, 0);
        int num = ta.getIndexCount();
        for (int i = 0; i < num; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
//                case R.styleable.ArcProgressbar_startAngle:
//                    mStartAngle = ta.getInt(attr, 135);
//                    break;
                case R.styleable.ArcProgressbar_arcColor:
                    mArcColor = ta.getColor(attr, Color.parseColor("#eed306"));
                    break;
                case R.styleable.ArcProgressbar_trokeWidth:
                    mTrokeWidth = ta.getDimensionPixelSize(attr, 15);
                    break;
                case R.styleable.ArcProgressbar_radius:
                    mRadius = ta.getDimensionPixelSize(attr, 115);
                    break;
            }
        }
        init();
    }

    public ArcProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressbar(Context context) {
        this(context, null);
    }

    private void init() {
        Resources res = getResources();
        // getDisplayMetrics()返回当前展示的metrics.
        // TypedValue.applyDimension(int unit, float value, DisplayMetrics
        // metrics)
        // 该方法中unit表示要转换成的单位，value表示数值，metrics表示当前的度量方式
        // DIAMETER是常量0x1E,十进制为30，下面语句就表示tmp的值为30dp换算成的像素数值
        // ceil函数表示向上取整
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        //抗锯齿
        mPaint.setAntiAlias(true);
        // setStrokeWidth()设置画笔宽度
        mPaint.setStrokeWidth(mTrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mArcColor);

        mRect = new RectF(mTrokeWidth, mTrokeWidth, mTrokeWidth + mRadius, mTrokeWidth + mRadius);
        mProgress = 0;
    }

    protected boolean clear = false;

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        // 如果mProgress<360,则圆形进度条还未旋转完成，则用0x7f的透明度绘制一个完整的圆形作为进度条背景
        // 注意要先绘制背景条，再绘制进度条，因为后绘制的会覆盖在先绘制的上面
        /*
         * if (mProgress < 360) { paint.setAlpha(0x7f);
		 * paint.setColor(defaultColor); canvas.drawArc(mRect, 135, 270, false,
		 * paint); }
		 */
        if (clear) {
            mPaint.setColor(Color.TRANSPARENT);
            clear = false;
            canvas.drawArc(mRect, 0, 0, false, mPaint);
            return;
        }
        if (mProgress != 0) {
            Paint paint = mPaint;
            paint.setColor(mArcColor);
            float degree = 360.0f * mProgress / 360;
            paint.setAlpha(0xff);
            paint.setColor(mArcColor);
            canvas.drawArc(mRect, mStartAngle, degree, false, paint);
        }
    }

    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // mDiameter表示小圆直径，mWidth表示圆环宽度的2倍，所以meas表示大圆直径
        // 所以View的hight，width都为meas
        final int meas = mRadius + mTrokeWidth * 2;
        setMeasuredDimension(meas, meas);
    }

    public void setProgress(int p, int startAngle) {
        mProgress = p;
        mStartAngle = startAngle;
        invalidate();
    }

    public void postProgress(final int p, final int startAngle) {
        post(new Runnable() {
            @Override
            public void run() {
                setProgress(p, startAngle);
            }
        });
    }

    public void setmArcColor(int mArcColor) {
        this.mArcColor = mArcColor;
    }

    public void reset() {
        clear = true;
        postProgress(0, 0);
        setRotation(0);
    }


    public void startRotate() {
        startAngle = 0;
        progress = 0;
        isPositive = true;
        setPivotX(0.5f * mRadius + mTrokeWidth);
        setPivotY(0.5f * mRadius + mTrokeWidth);
        if (rotateAnimator == null) {
            rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0.0f, 360.0f);
            rotateAnimator.setDuration(2000);
            rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
            rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
            rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        rotateAnimator.start();
        if (timer != null) {//保证上一次的定时器任务被释放了
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                postProgress(progress, startAngle);
                if (progress > MAX_DEGREE) {
                    progress = MAX_DEGREE;
                }
                if (progress < 0) {
                    progress = 0;
                }

                if (isPositive && progress == MAX_DEGREE || !isPositive && progress == 0) {
                    isPositive = !isPositive;
                    startAngle = startAngle % 360;
                }
                if (isPositive) {
                    progress += ADD_ON_DEGRESS;
                } else {
                    startAngle += ADD_ON_DEGRESS;
                    progress -= ADD_ON_DEGRESS;
                }
            }
        }, 0, 5);
    }

    public void stopRotate() {
        if (rotateAnimator != null) rotateAnimator.cancel();
        reset();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

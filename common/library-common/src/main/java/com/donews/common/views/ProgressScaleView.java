package com.donews.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.donews.common.R;

/**
 * @author by SnowDragon
 * Date on 2021/1/22
 * Description:
 */
public class ProgressScaleView extends View {
    private static String TAG = "ProgressScaleView";
    private float PADDING = 5f;
    private int MAX_PROGRESS = 10;
    private int INTERVAL_WIDTH = 5;
    private String BACk_GROUND_COLOR = "#ff2533a1";


    private Paint paint = new Paint();

    private float paddingVal = 0f;

    private int progressMax = MAX_PROGRESS;
    private int proBackgroundColor = 0;

    //默认进度颜色值
    private int progressDefaultColor = 0;

    //进度颜色值
    private int progressColor = 0;

    //进度条高度
    private float progressHeight = 0f;

    //是否显示刻度
    private boolean showScale = false;

    //刻度比
    int scaleThan = 1;


    //刻度字体大小
    private float scaleFontSize = 14f;

    //刻度字体颜色
    private int scaleFontColor = Color.GREEN;

    //刻度距离进度条间距
    private float scaleDistanceProgress = 0f;

    //进度之间间隔
    private int interval = 0;

    int progress = 0;


    public ProgressScaleView(Context context) {
        this(context, null);
    }

    public ProgressScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressScaleView(Context context, @Nullable AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.ProgressScaleView);
        paddingVal = a.getDimension(R.styleable.ProgressScaleView_proPadding, PADDING);
        progressMax = a.getInt(R.styleable.ProgressScaleView_progressMax, MAX_PROGRESS);
        progress = a.getInt(R.styleable.ProgressScaleView_progress, 0);
        interval = (int) a.getDimension(R.styleable.ProgressScaleView_proInterval, INTERVAL_WIDTH);
        proBackgroundColor = a.getColor(R.styleable.ProgressScaleView_proBackgroundColor, Color.parseColor(BACk_GROUND_COLOR));
        progressDefaultColor = a.getColor(R.styleable.ProgressScaleView_progressDefaultColor, Color.WHITE);
        progressColor = a.getColor(R.styleable.ProgressScaleView_progressColor, Color.GREEN);

        progressHeight = a.getDimension(R.styleable.ProgressScaleView_progressHeight, dp2px(context, 20f));

        scaleDistanceProgress = a.getDimension(R.styleable.ProgressScaleView_scaleDistance, 5f);
        scaleFontSize = a.getDimension(R.styleable.ProgressScaleView_scaleFontSize, 14f);
        scaleFontColor = a.getColor(R.styleable.ProgressScaleView_scaleFontColor, Color.GREEN);
        showScale = a.getBoolean(R.styleable.ProgressScaleView_proShowScale, false);
        scaleThan = a.getInt(R.styleable.ProgressScaleView_scaleThan, 1);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int withSpec = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        //定义为wrap_content的时候
        if (heightSpec == MeasureSpec.AT_MOST) {
            int height = (int) (progressHeight + paddingVal);
            if (showScale) {
                height = (int) (measureTextHeight() + scaleDistanceProgress + paddingVal + progressHeight);
            }
            setMeasuredDimension(withSize, Math.min(heightSize, height));
        }
    }


    /**
     * 测量文字宽度
     *
     * @return float
     */
    private Float measureTextWith(String text) {
        paint.setTextSize(scaleFontSize);
        return paint.measureText(text);
    }

    /**
     * 测量文字的高度
     *
     * @return 文字高度
     */
    private int measureTextHeight() {
        paint.setTextSize(scaleFontSize);
        Rect rectFont = new Rect();
        paint.getTextBounds("0", 0, "0".length(), rectFont);

        return rectFont.height();

    }


    float blockWidth = 0f;
    float blockBottom = 0f;

    //进度块顶部位置
    private float progressBlockTop = 0f;

    //进度条顶部位置
    private float progressTop = 0f;
    private float leftScale = 0f;
    RectF bigRectF = new RectF();
    RectF innerRectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制背景色
        //canvas.drawARGB(255, 255, 232, 0)
        if (showScale) {
            progressTop = measureTextHeight() + scaleDistanceProgress;
        }
        progressBlockTop = progressTop + paddingVal;

        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(proBackgroundColor);

        bigRectF.set(0, progressTop, getWidth(), getHeight());
        //绘制背景
        canvas.drawRoundRect(bigRectF, bigRectF.width() / 2, bigRectF.width() / 2, paint);

        float left = 0f + paddingVal;
        float right = 0f;

        //进度块宽度
        blockWidth = (getWidth() - paddingVal * 2 - (progressMax - 1) * interval) / progressMax;
        blockBottom = getHeight() - paddingVal;

        paint.setColor(Color.WHITE);
        for (int i = 1; i <= progressMax; i++) {
            right = left + blockWidth;

            paint.setColor(progress >= i ? progressColor : progressDefaultColor);

            if (i == 1) {
                //绘制第一个进度格
                drawFirstBlock(canvas, left, right);
            } else if (i == progressMax) {
                //绘制第最后一个进度格
                drawEndBlock(canvas, left, right);
            } else {
                innerRectF.set(left, progressBlockTop, right, blockBottom);
                canvas.drawRect(innerRectF, paint);
            }

            left += blockWidth + interval;

            if (progress >= i) {
                leftScale = left;
            }
        }

        //绘制刻度
        if (showScale && progress > 0) {
            paint.setTextSize(scaleFontSize);
            paint.setColor(scaleFontColor);


            //val text = (progress * scaleThan).toString();
            String text = progress + "/" + progressMax;

            //如果不是最后一格进度，居中显示，如果是最后一格，按文字显示
            float leftScaleLast = 0;
            if (progress < progressMax) {
                leftScaleLast = leftScale - measureTextWith(text) / 2;
            } else {
                leftScaleLast = getWidth() - measureTextWith(text);
            }


            canvas.drawText(text, leftScaleLast, measureTextHeight(), paint);

        }


    }

    /**
     * 绘制第一个进度块
     */
    private void drawFirstBlock(Canvas canvas, float left, float right) {
        RectF rectF = new RectF(left, progressBlockTop, right + blockWidth, blockBottom);
        RectF xRectF = new RectF(left + blockWidth, progressBlockTop, right + blockWidth, blockBottom);
        drawXBlock(rectF, xRectF, canvas);

    }

    /**
     * 绘制最后一个进度块
     */
    private void drawEndBlock(Canvas canvas, float left, float right) {
        RectF rectF = new RectF(left - blockWidth, progressBlockTop, right, blockBottom);
        RectF xRectF = new RectF(left - blockWidth, progressBlockTop, right - blockWidth, blockBottom);
        drawXBlock(rectF, xRectF, canvas);
    }

    /*
     * 绘制进度块，剪切RectF
     * */
    private void drawXBlock(RectF rectF, RectF xRectF, Canvas canvas) {
        int layerId = createLayer(canvas);
        canvas.drawRoundRect(rectF, rectF.height() / 2, rectF.height() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawRect(xRectF, paint);
        paint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    /**
     * 创建绘制层
     */
    private int createLayer(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return canvas.saveLayer(0f, 0f, getWidth(), getHeight(), paint);
        } else {
            return canvas.saveLayer(0f, 0f, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        }
    }

    public void setScaleThan(int scaleThan) {
        this.scaleThan = scaleThan;
        invalidate();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return pxVal
     */
    private Float dp2px(Context context, float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getContext().getResources().getDisplayMetrics());
    }

    private Float sp2px(Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getContext().getResources().getDisplayMetrics());
    }


}

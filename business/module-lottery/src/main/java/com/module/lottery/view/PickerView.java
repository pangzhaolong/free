package com.module.lottery.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.module.lottery.dialog.GenerateCodeDialog;
import com.module_lottery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

/**
 * 滚动选择器
 *
 * @author hegai
 */
public class PickerView extends View {
    public static final String TAG = "PickerView";
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 2.8f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;
    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变mDataList
     */
    private int mCurrentSelected;
    private Paint mPaint;
    private float mMinTextSize = 40;
    private float mLastDownY;
    private int mMaxVelocity = 3000;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private String selectValue = "0";
    private float tstMode = 0;
    private boolean direction;
    private ValueAnimator valueAnimator = new ValueAnimator();
    private VelocityTracker mVelocityTracker;
    private int directionLocation = -1;
    private MotionEvent mMotionEvent;
    private PickerViewTimerTask mTask;
    private Timer timer;
    private PickerViewHandler mPickerViewHand = new PickerViewHandler(this);


    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see android.view.VelocityTracker#obtain()
     * @see android.view.VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    @Override
    protected void onDetachedFromWindow() {
        cleanTimerTask();
        cleanAnimator();
        super.onDetachedFromWindow();
    }

    @SuppressLint("LongLogTag")
    private void doMove(float eventy, boolean type) {
        mMoveLen += (eventy - mLastDownY);
        if (type) {
            if (directionLocation == 1) {
                // 往下滑超过离开距离
                if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                    // 往下滑超过离开距离
                    moveTailToHead();
                    mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
                    directionLocation = 1;
                }
            } else {
                if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                    // 往上滑超过离开距离
                    moveHeadToTail();
                    mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
                    directionLocation = 2;
                }
            }

        } else {
            if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                // 往下滑超过离开距离
                moveTailToHead();
                direction = true;
                mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
                directionLocation = 1;
            } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                // 往上滑超过离开距离
                moveHeadToTail();
                direction = false;
                mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
                directionLocation = 2;
            }
        }
        mLastDownY = eventy;
        invalidate();
    }

    Bitmap bitmap;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }
    public String getPerformSelectValue() {
        return selectValue;
    }
    public void setData() {
        mDataList = new ArrayList<>();
        mDataList.add("0");
        mDataList.add("1");
        mDataList.add("2");
        mDataList.add("3");
        mDataList.add("4");
        mDataList.add("5");
        mDataList.add("6");
        mDataList.add("7");
        mDataList.add("8");
        mDataList.add("9");
        Collections.shuffle(mDataList);
        mCurrentSelected = mDataList.size() / 2;
        invalidate();
    }

    public void setSelected(int selected) {
        mCurrentSelected = selected;
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(bitmap.getWidth(), hSpecSize);
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(bitmap.getWidth(), hSpecSize);
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(bitmap.getWidth(), hSpecSize);
        }

    }


    private void init() {
        timer = new Timer();
        setData();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(Color.WHITE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.number_bg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        mPaint.setTextSize(mMinTextSize);
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        Log.d("=====mMoveLendrawData", mMoveLen + "");
        float y = (float) (getHeight() / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawBitmap(bitmap, 0, y - (bitmap.getHeight() / 2), new Paint());
        selectValue = mDataList.get(mCurrentSelected);
        canvas.drawText(selectValue, getWidth() / 2, baseline, mPaint);

        StringBuffer buffer1 = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            String name_ = drawOtherText(canvas, i, -1);
            buffer1.append(name_);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            String name_2 = drawOtherText(canvas, i, 1);
            buffer2.append(name_2);
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private String drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLen);
        mPaint.setTextSize(mMinTextSize);
        float y = (float) (getHeight() / 2.0 + type * d);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.save();
        canvas.drawBitmap(bitmap, 0, y - (bitmap.getHeight() / 2), new Paint());

        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                getWidth() / 2, baseline, mPaint);
        return mDataList.get(mCurrentSelected + type * position);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        final VelocityTracker verTracker = mVelocityTracker;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                cleanAnimator();
                tstMode = 0;
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //求伪瞬时速度
                verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                doMove(event.getY(), false);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("&&&&&&&&&Event", event.getY() + "");
                doUp(event);
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 释放VelocityTracker
     *
     * @see android.view.VelocityTracker#clear()
     * @see android.view.VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void doDown(MotionEvent event) {
        mLastDownY = event.getY();
    }


    private void doUp(MotionEvent event) {
        mMotionEvent = event;
        int speed = (int) mVelocityTracker.getYVelocity();
        Log.i(TAG, "speed " + speed);
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mMotionEvent != null && tstMode == 0) {
            tstMode = mMotionEvent.getY();
            mMotionEvent = null;
        }
        Log.e(TAG, tstMode + "");
        if (Math.abs(speed) < 300) {
            startTimerTask();
        } else {
            int speedRefer = 10;
            if (Math.abs(speed) >= mMaxVelocity) {
                speedRefer = 20;
            }
            if (directionLocation == 1) {
                startValueAnimator(speedRefer);
            } else {
                startValueAnimator(speedRefer);
            }
        }

    }

    private void cleanTimerTask() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }

    }

    private void startTimerTask() {
        cleanTimerTask();
        mTask = new PickerViewTimerTask(mPickerViewHand);
        timer.schedule(mTask, 0, 10);
    }


    //direction true 上
    private void startValueAnimator(int start) {
        cleanAnimator();
        //动画时长
        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setIntValues(start, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                Log.e(TAG, "AnimatedValue " + value);
                if (direction) {
                    tstMode = tstMode + value;
                } else {
                    tstMode = tstMode - value;
                }
                doMove(tstMode, true);
            }

        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startTimerTask();
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private void cleanAnimator() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.removeAllListeners();
        }
    }

    class PickerViewTimerTask extends TimerTask {
        Handler handler;

        public PickerViewTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface onSelectListener {
        void onSelect(String text);
    }


    private static class PickerViewHandler extends Handler {
        private WeakReference<PickerView> reference;   //

        PickerViewHandler(PickerView context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (Math.abs(reference.get().mMoveLen) < SPEED) {
                reference.get().mMoveLen = 0;
                if (reference.get().mTask != null) {
                    reference.get().mTask.cancel();
                    reference.get().mTask = null;
                    reference.get().performSelect();
                }
            } else
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                reference.get().mMoveLen = reference.get().mMoveLen - reference.get().mMoveLen / Math.abs(reference.get().mMoveLen) * SPEED;
            reference.get().invalidate();
        }
    }


}
package com.donews.turntable.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.donews.turntable.R;
import com.donews.turntable.bean.TurntablePrize;
import com.donews.turntable.dialog.RuleDialog;
import com.donews.turntable.interfaceUtils.IturntableAnimator;
import com.donews.turntable.utils.AnimatorUtils;
import com.donews.turntable.utils.ClickDoubleUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TurntableView extends View {


    private static long CONSTANT_1200 = 1200;


    //边框的圆圈
    private Bitmap bgBitmap;
    //三角形的宽度
    private int mTriangleWidth;
    private ITurntableResultListener mTurntableResult;
    private List<TurntablePrize> mItemBitmap = new ArrayList<>();
    private Paint mPaintText;

    public TurntableView(Context context) {
        this(context, null);
    }

    public TurntableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private WeakTurntableView weakTurntableView = new WeakTurntableView(this);
    private float unitAngle;
    private float mNumAngle = 360f;
    private Resources mResources;
    TypedArray typedArray;
    private int defaultColor = Color.parseColor("#000000");
    private float mTextSize;
    private int mTextColor;
    private float mTextMarginTod;
    //三角形距离顶部的距离
    private float mTriangleTop;
    private int number = 8;
    private Context mContext;
    private ObjectAnimator objectAnimator;
    private boolean mClickable = true;

    public TurntableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //自定义属性部分
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.TurntableView);
        mTextSize = typedArray.getDimension(R.styleable.TurntableView_text_size, 40);
        mTextMarginTod = typedArray.getDimension(R.styleable.TurntableView_margin_tod, 40);
        mTextColor = typedArray.getColor(R.styleable.TurntableView_text_color, defaultColor);
        mTriangleTop = typedArray.getDimension(R.styleable.TurntableView_padding_content_top, 70);
        mPaintText = new Paint();
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(mTextColor);
        mResources = getResources();
        unitAngle = mNumAngle / number;
        typedArray.recycle();
        setInitBitmap(null);
    }

    //边框的圆圈
    public void setInitBitmap(List<TurntablePrize> itemBitmap) {
        bgBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.turntable_border)).getBitmap();
        if (itemBitmap == null) {
            //奖item
            for (int i = 0; i < 8; i++) {
                TurntablePrize turntable = new TurntablePrize();
                String name = "item_0" + (i + 1) + "";
                Bitmap item = ((BitmapDrawable) mContext.getDrawable(getResources().getIdentifier(name, "mipmap", mContext.getPackageName()))).getBitmap();
                turntable.setBitmap(item);
                mItemBitmap.add(turntable);
            }
        } else {
            number = itemBitmap.size();
            mItemBitmap.addAll(itemBitmap);
        }

        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItemBitmap != null) {
            if (mTriangleWidth == 0) {
                initParameter();
            }
            canvas.drawBitmap(bgBitmap, 0, 0, mPaintText);
            drawTriangleBitmap(canvas);
        }
    }


    private void initParameter() {
        int s_w = (int) (Math.sin(Math.toRadians(unitAngle / 2)) * ((getHeight() - (mTriangleTop * 2)) / 2));
        s_w = s_w + s_w;
        mTriangleWidth = s_w;
        for (int i = 0; i < mItemBitmap.size(); i++) {
            mItemBitmap.get(i).setBitmap(imageScale(mItemBitmap.get(i).getBitmap(), s_w, (int) (getHeight() / 2 - mTriangleTop)));
        }
        bgBitmap = imageScale(bgBitmap, getWidth(), getHeight());
        //设置icon的宽高
    }


    public void startAnimator() {
        if (objectAnimator != null && objectAnimator.isRunning() && !ClickDoubleUtil.isFastClick() || !mClickable) {
            return;
        }
        mClickable = false;
        objectAnimator = AnimatorUtils.singleton().getRotateValueAnimator(this, new IturntableAnimator() {
            TurntablePrize bean;

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mTurntableResult != null) {
                    mTurntableResult.onResult(bean);
                }
                if (weakTurntableView != null) {
                    weakTurntableView.removeMessages(0);
                    weakTurntableView.removeCallbacksAndMessages(null);
                    weakTurntableView.sendEmptyMessageDelayed(0, CONSTANT_1200);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onLocationAngle(String value) {
                //返回的是生成的度数
                float angle = Float.parseFloat(value);
                int index = (int) (angle / unitAngle);
                TurntablePrize effect = mItemBitmap.get((mItemBitmap.size() - 1) - index);
                Log.d("抽奖结果    ", effect.getName() + "");
                this.bean = effect;
            }
        });
        objectAnimator.setDuration(3000);
        objectAnimator.start();
    }


    public interface ITurntableResultListener {

        public void onResult(TurntablePrize prize);

    }

    public void setTurntableResultListener(ITurntableResultListener iTurntableResultListener) {
        mTurntableResult = iTurntableResultListener;
    }


    private void drawTriangleBitmap(Canvas canvas) {
        canvas.save();
        canvas.rotate(unitAngle / 2, getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < mItemBitmap.size(); i++) {
            Paint p = new Paint();
            Matrix matrix = new Matrix();
            matrix.postTranslate(getWidth() / 2 - (mTriangleWidth / 2), mTriangleTop);
            canvas.drawBitmap(mItemBitmap.get(i).getBitmap(), matrix, p);
            canvas.rotate(unitAngle, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();
    }

    /**
     * 调整图片大小
     *
     * @param bitmap 源
     * @param dst_w  输出宽度
     * @param dst_h  输出高度
     * @return
     */
    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;

    }


    private static class WeakTurntableView extends Handler {
        private WeakReference<TurntableView> reference;   //

        WeakTurntableView(TurntableView context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (reference.get() != null) {
                        reference.get().mClickable = true;
                    }
                    break;
            }
        }
    }
}

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
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.donews.middle.bean.globle.TurntableBean;
import com.donews.turntable.R;
import com.donews.turntable.bean.RewardedBean;
import com.donews.turntable.bean.TurntablePrize;
import com.donews.turntable.dialog.RuleDialog;
import com.donews.turntable.interfaceUtils.IturntableAnimator;
import com.donews.turntable.utils.AnimatorUtils;
import com.donews.turntable.utils.ClickDoubleUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TurntableView extends View {


    private static long CONSTANT_1200 = 1200;


    //边框的圆圈
    private Bitmap bgBitmap;
    //三角形的宽度
    private int mTriangleWidth;
    private ITurntableResultListener mTurntableResult;
    private List<TurntableBean.ItemsDTO> mItemDataList = new ArrayList<>();
    private Paint mPaintText;

    public TurntableView(Context context) {
        this(context, null);
    }

    public TurntableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private WeakTurntableView weakTurntableView = new WeakTurntableView(this);
    //单位角度
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
    public void setInitBitmap(List<TurntableBean.ItemsDTO> itemBitmap) {
        bgBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.turntable_border)).getBitmap();
        if (itemBitmap == null) {
//            //奖item
//            for (int i = 0; i < 8; i++) {
//                TurntableBean.ItemsDTO turntable = new TurntableBean.ItemsDTO();
//                String name = "item_0" + (i + 1) + "";
//                Bitmap item = ((BitmapDrawable) mContext.getDrawable(getResources().getIdentifier(name, "mipmap", mContext.getPackageName()))).getBitmap();
//                mItemDataList.add(turntable);
//            }
        } else {
            number = itemBitmap.size();
            mItemDataList.addAll(itemBitmap);
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
        if (mItemDataList != null) {
            if (mTriangleWidth == 0) {
                initParameter();
            }
            canvas.drawBitmap(bgBitmap, 0, 0, mPaintText);
            if (loadingFinished()) {
                drawTriangleBitmap(canvas);
            }
        }
    }


    //所有奖励图片初始化完成
    private boolean loadingFinished() {
        for (int i = 0; i < mItemDataList.size(); i++) {
            if (mItemDataList.get(i).getBitmap() == null) {
                return false;
            }
        }
        return true;
    }

    private void initParameter() {
        int s_w = (int) (Math.sin(Math.toRadians(unitAngle / 2)) * ((getHeight() - (mTriangleTop * 2)) / 2));
        s_w = s_w + s_w;
        //每一张卡片的宽度
        mTriangleWidth = s_w;
        for (int i = 0; i < mItemDataList.size(); i++) {
            ImageView imageView = new ImageView(mContext);
//            imageView.setColorFilter(Color.parseColor("#000000"));
//            imageView.setImageResource(R.mipmap.item_01);
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.width = 100;
//            layoutParams.height = 100;
//            imageView.setLayoutParams(layoutParams);
//            imageView.setDrawingCacheEnabled(true);
//            BitmapDrawable bitmapDrawable= (BitmapDrawable) imageView.getDrawable();
//            mItemDataList.get(i).setBitmap(imageScale(bitmapDrawable.getBitmap(), s_w, (int) (getHeight() / 2 - mTriangleTop)));
            setImager(i, imageView);
        }
        bgBitmap = imageScale(bgBitmap, getWidth(), getHeight());
        //设置icon的宽高
    }

    private void setImager(int i, ImageView imageView) {
        Glide.with(mContext).
                load(mItemDataList.get(i).getItem_icon()).
                listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        BitmapDrawable mresource = (BitmapDrawable) resource;
                        mItemDataList.get(i).setBitmap(imageScale(mresource.getBitmap(), mTriangleWidth, (int) (getHeight() / 2 - mTriangleTop)));
                        postInvalidate();
                        return false;
                    }
                }).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView);

    }


    public void startAnimator(RewardedBean recommendBean) {
        if (recommendBean != null && objectAnimator != null && objectAnimator.isRunning() && !ClickDoubleUtil.isFastClick() || !mClickable && loadingFinished()) {
            return;
        }
        //最大角度
        float endAngle = unitAngle * recommendBean.getId();
        //起始角度
        float startAngle = endAngle - unitAngle;
        Random rand = new Random();
        int angle = rand.nextInt(((int) (endAngle - (int) startAngle)) - 5) + ((int) startAngle + 5);
        mClickable = false;
        objectAnimator = AnimatorUtils.singleton().getRotateValueAnimator(this, new IturntableAnimator() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mTurntableResult != null) {
                    int id = recommendBean.getId() - 1;
                    if (id < 0) {
                        id = 0;
                    }
                    mTurntableResult.onResult(mItemDataList.get(id));
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

        }, angle);
        objectAnimator.setDuration(3000);
        objectAnimator.start();
    }


    public interface ITurntableResultListener {

        public void onResult(TurntableBean.ItemsDTO itemsDTO);

    }

    public void setTurntableResultListener(ITurntableResultListener iTurntableResultListener) {
        mTurntableResult = iTurntableResultListener;
    }


    private void drawTriangleBitmap(Canvas canvas) {
        canvas.save();
        canvas.rotate(unitAngle / 2, getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < mItemDataList.size(); i++) {
            Paint p = new Paint();
            Matrix matrix = new Matrix();
            matrix.postTranslate(getWidth() / 2 - (mTriangleWidth / 2), mTriangleTop);
            canvas.drawBitmap(mItemDataList.get(i).getBitmap(), matrix, p);
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

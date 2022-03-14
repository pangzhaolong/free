package com.donews.common.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 *  金币动画，
 */

public class DongHuaImageView extends RelativeLayout {
    private static final String TAG = "DongHuaImageView";

    private int mTop;

    public DongHuaImageView(Context context) {
        super(context);
        init();
    }

    public DongHuaImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DongHuaImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 30, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dx = (int) animation.getAnimatedValue();
                setTop(mTop - dx);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {

            private int curImgIndex = 0;
            private int imgCount = 3;

            @Override
            public void onAnimationStart(Animator animation) {
//                setImageResource(R.mipmap.glod_logo_bg);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                curImgIndex++;
                switch (curImgIndex % imgCount) {
                    case 0:
//                        setImageResource(R.drawable.icon_mobile);
                        break;
                    case 1:
//                        setImageResource(R.drawable.loading);
                        break;
                    case 2:
//                        setImageResource(R.drawable.scan_circle);
                        break;
                }
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        int time = (int) (Math.random() * (500) + 2000);
        Log.d(TAG, "init: time:" + time);
        animator.setDuration(time);
        animator.start();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTop = top;
    }
}
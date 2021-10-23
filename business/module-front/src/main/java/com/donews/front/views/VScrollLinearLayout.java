package com.donews.front.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.donews.utilslibrary.utils.LogUtil;

public class VScrollLinearLayout extends LinearLayout {
    private final LinearLayout mLinearLayout;
    private ValueAnimator mValueAnimator;
    private LayoutParams mLayoutParams;
    private int mLeftMargin = 10001;

    public VScrollLinearLayout(Context context) {
        super(context);
        mLinearLayout = this;
    }

    public VScrollLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLinearLayout = this;
    }

    public void startLoop() {
        loop();
    }

    public void stopLoop() {
        if (mValueAnimator == null) {
            return;
        }

        mValueAnimator.cancel();
        mValueAnimator.removeAllListeners();
        mValueAnimator = null;
    }

    @SuppressLint("WrongConstant")
    private void loop() {
        mValueAnimator = ValueAnimator.ofInt(0, 400);
        mValueAnimator.addUpdateListener(animation -> {
            mLayoutParams = (LayoutParams) mLinearLayout.getLayoutParams();
            if (mLeftMargin > 1000) {
                mLeftMargin = mLayoutParams.leftMargin;
                LogUtil.e("xx xx:" + mLayoutParams.leftMargin);
            }
            mLayoutParams.leftMargin = mLeftMargin - (int) animation.getAnimatedValue();
            mLinearLayout.setLayoutParams(mLayoutParams);
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLayoutParams = (LayoutParams) mLinearLayout.getLayoutParams();
                mLayoutParams.leftMargin = mLeftMargin + mLayoutParams.width;
                mLinearLayout.setLayoutParams(mLayoutParams);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(4 * 1000);
        mValueAnimator.setRepeatMode(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.start();
    }
}

package com.donews.turntable.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.donews.turntable.interfaceUtils.IturntableAnimator;

public class AnimatorUtils {


    private static AnimatorUtils singleton;

    private AnimatorUtils() {
    }

    public static AnimatorUtils singleton() {
        if (singleton == null) {
            singleton = new AnimatorUtils();
        }
        return singleton;
    }

    float ring = 10f * 360f;//圈

    public ObjectAnimator getRotateValueAnimator(View view, IturntableAnimator animatorListener) {
        float angle = (float) (360f * Math.random());//生成随机数
        Log.d("随机值    ", angle + "");
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, ring + angle);
        animatorListener.onLocationAngle(angle + "");
        valueAnimator.addListener(animatorListener);
        valueAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        return valueAnimator;
    }

}

package com.module.lottery.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.module_lottery.R;

public class LotteryAnimationUtils {


    //缩放动画
    public static ScaleAnimation setScaleAnimation(int time) {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1.1f, 0.88f, 1.1f, 0.88f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setInterpolator(new LinearInterpolator());
        mScaleAnimation.setRepeatMode(Animation.REVERSE);
        mScaleAnimation.setRepeatCount(Animation.INFINITE);
        mScaleAnimation.setDuration(time);
        return mScaleAnimation;

    }



    //位移动画
    public static Animation setTranslateAnimation(Context context ) {
        Animation animation= AnimationUtils.loadAnimation(context, R.anim.translate);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;

    }

}

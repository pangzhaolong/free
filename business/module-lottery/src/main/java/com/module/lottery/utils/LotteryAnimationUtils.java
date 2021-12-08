package com.module.lottery.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

public class LotteryAnimationUtils {



    public  static   ScaleAnimation setScaleAnimation(int time){
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1.1f, 0.88f, 1.1f, 0.88f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setInterpolator(new LinearInterpolator());
        mScaleAnimation.setRepeatMode(Animation.REVERSE);
        mScaleAnimation.setRepeatCount(Animation.INFINITE);
        mScaleAnimation.setDuration(time);
        return mScaleAnimation;

    }


}

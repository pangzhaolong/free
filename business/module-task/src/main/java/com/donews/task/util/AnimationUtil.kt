package com.donews.task.util

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import com.airbnb.lottie.LottieAnimationView

/**
 *  make in st
 *  on 2022/5/9 15:16
 */
object AnimationUtil {

    /**
     * 抖动
     */
    fun startShakeAnimation(view: View?, shakeFactor: Float = 1f): ObjectAnimator {
        val pvhScaleX = PropertyValuesHolder.ofKeyframe(
            View.SCALE_X,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f))
        val pvhScaleY = PropertyValuesHolder.ofKeyframe(
            View.SCALE_Y,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f)
        )
        val pvhRotate = PropertyValuesHolder.ofKeyframe(
            View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.1f, -3f * shakeFactor),
            Keyframe.ofFloat(.2f, -3f * shakeFactor),
            Keyframe.ofFloat(.3f, 3f * shakeFactor),
            Keyframe.ofFloat(.4f, -3f * shakeFactor),
            Keyframe.ofFloat(.5f, 3f * shakeFactor),
            Keyframe.ofFloat(.6f, -3f * shakeFactor),
            Keyframe.ofFloat(.7f, 3f * shakeFactor),
            Keyframe.ofFloat(.8f, -3f * shakeFactor),
            Keyframe.ofFloat(.9f, 3f * shakeFactor),
            Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate)
            .setDuration(2000).apply {
                repeatCount = ValueAnimator.INFINITE
                start()
            }
    }

    fun startTaskFingerAnimation(view: LottieAnimationView?){
       view?.let {
           it.imageAssetsFolder = "images"
           it.setAnimation("task_finger.json")
           it.loop(true)
           it.playAnimation()
        }
    }

    fun cancelFingerAnimation(view: LottieAnimationView?){
        view?.cancelAnimation()
    }

}
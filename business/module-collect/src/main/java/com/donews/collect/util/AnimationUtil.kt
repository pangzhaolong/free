package com.donews.collect.util

import android.animation.*
import android.view.animation.LinearInterpolator

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import kotlin.math.sqrt


/**
 *  make in st
 *  on 2022/5/17 14:16
 */
object AnimationUtil {

    /**
     * 气泡漂浮动画
     * @param view
     * @param duration  动画运行时间
     * @param offset    动画运行幅度
     * @param repeatCount   动画运行次数
     * @return
     */
    fun bubbleFloat(view: View?, duration: Int, offset: Float, repeatCount: Int): ObjectAnimator {
        val path = (sqrt(3.0) / 2 * offset).toFloat()
        val translateX: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_X,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(1 / 12f, offset / 2),
            Keyframe.ofFloat(2 / 12f, path),
            Keyframe.ofFloat(3 / 12f, offset),
            Keyframe.ofFloat(4 / 12f, path),
            Keyframe.ofFloat(5 / 12f, offset / 2),
            Keyframe.ofFloat(6 / 12f, 0f),
            Keyframe.ofFloat(7 / 12f, -offset / 2),
            Keyframe.ofFloat(8 / 12f, -path),
            Keyframe.ofFloat(9 / 12f, -offset),
            Keyframe.ofFloat(10 / 12f, -path),
            Keyframe.ofFloat(11 / 12f, -offset / 2),
            Keyframe.ofFloat(1f, 0f)
        )
        val translateY: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_Y,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(1 / 12f, offset - path),
            Keyframe.ofFloat(2 / 12f, offset / 2),
            Keyframe.ofFloat(3 / 12f, offset),
            Keyframe.ofFloat(4 / 12f, offset * 3 / 2),
            Keyframe.ofFloat(5 / 12f, offset + path),
            Keyframe.ofFloat(6 / 12f, offset * 2),
            Keyframe.ofFloat(7 / 12f, offset + path),
            Keyframe.ofFloat(8 / 12f, offset * 3 / 2),
            Keyframe.ofFloat(9 / 12f, offset),
            Keyframe.ofFloat(10 / 12f, offset / 2),
            Keyframe.ofFloat(11 / 12f, offset - path),
            Keyframe.ofFloat(1f, 0f)
        )
        val rotateX: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.ROTATION_X,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(1 / 12f, offset / 2),
            Keyframe.ofFloat(2 / 12f, path),
            Keyframe.ofFloat(3 / 12f, offset),
            Keyframe.ofFloat(4 / 12f, path),
            Keyframe.ofFloat(5 / 12f, offset / 2),
            Keyframe.ofFloat(6 / 12f, 0f),
            Keyframe.ofFloat(7 / 12f, -offset / 2),
            Keyframe.ofFloat(8 / 12f, -path),
            Keyframe.ofFloat(9 / 12f, -offset),
            Keyframe.ofFloat(10 / 12f, -path),
            Keyframe.ofFloat(11 / 12f, -offset / 2),
            Keyframe.ofFloat(1f, 0f)
        )
        val rotateY: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.ROTATION_Y,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(1 / 12f, offset / 2),
            Keyframe.ofFloat(2 / 12f, path),
            Keyframe.ofFloat(3 / 12f, offset),
            Keyframe.ofFloat(4 / 12f, path),
            Keyframe.ofFloat(5 / 12f, offset / 2),
            Keyframe.ofFloat(6 / 12f, 0f),
            Keyframe.ofFloat(7 / 12f, -offset / 2),
            Keyframe.ofFloat(8 / 12f, -path),
            Keyframe.ofFloat(9 / 12f, -offset),
            Keyframe.ofFloat(10 / 12f, -path),
            Keyframe.ofFloat(11 / 12f, -offset / 2),
            Keyframe.ofFloat(1f, 0f)
        )
        val animator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(view, translateX, translateY, rotateX, rotateY)
                .setDuration(
                    duration.toLong()
                )
        animator.repeatCount = repeatCount
        animator.interpolator = LinearInterpolator()
        return animator
    }

    fun rotate(view: View?) : ObjectAnimator{
         return ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).apply {
            duration = 8000
            interpolator = LinearInterpolator()
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }

    fun startFingerAnimation(view: LottieAnimationView?){
        view?.let {
            it.imageAssetsFolder = "images"
            it.clearAnimation()
            it.setAnimation("task_finger.json")
            it.loop(true)
            it.playAnimation()
        }
    }

    fun startLottieAnimation(view: LottieAnimationView?,animationEnd: ()->Unit = {}){
        view?.let {
            it.imageAssetsFolder = "images"
            it.setAnimation("collect_step_one.json")
            it.loop(false)
            it.addAnimatorListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    animationEnd.invoke()
                    animation?.cancel()
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationRepeat(animation: Animator?) {}

            })
            it.playAnimation()
        }
    }

    fun cancelLottieAnimation(view: LottieAnimationView?){
        view?.cancelAnimation()
    }

    fun clickPressAnimation(view: View?,animationEndCall:()->Unit={}){
        view?.let {
            val animatorSet = AnimatorSet()
            val animatorA = ObjectAnimator.ofFloat(view, "translationY", 0f, 15f).setDuration(500)
            val animatorB = ObjectAnimator.ofFloat(view, "translationY", 15f, 0f).setDuration(500)
            animatorSet.play(animatorA).before(animatorB)
            animatorSet.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    animatorA.cancel()
                    animatorB.cancel()
                    animatorSet.cancel()
                    animationEndCall.invoke()
                }
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
            animatorSet.start()
        }
    }

    /**
     * 抖动
     */
    fun startShakeAnimation(view: View?, shakeFactor: Float = 1f,mRepeatCount: Int = ValueAnimator.INFINITE,mDuration:Long = 500L,animationEndCall:()->Unit={}):ObjectAnimator {
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
            .setDuration(mDuration).apply {
                repeatCount = mRepeatCount
                addListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        cancel()
                        animationEndCall.invoke()
                    }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })
                start()
            }
    }

}
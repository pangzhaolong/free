package com.donews.task.util

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.donews.task.R
import kotlin.math.sqrt

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
           it.visibility = View.VISIBLE
           it.imageAssetsFolder = "images"
           it.cancelAnimation()
           it.setAnimation("task_finger.json")
           it.loop(true)
           it.playAnimation()
        }
    }

    fun cancelFingerAnimation(view: LottieAnimationView?){
        view?.let {
            it.cancelAnimation()
            it.visibility = View.GONE
        }
    }

    fun coinGifStart(context: Fragment,imageView: ImageView?) {
        try {
            if (imageView != null){
                Glide.with(context)
                    .asGif()
                    .load(R.drawable.task_coin_gif)
                    .addListener(object :
                        RequestListener<GifDrawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                        override fun onResourceReady(
                            resource: GifDrawable?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.setLoopCount(1)
                            return false
                        }
                    })
                    .into(imageView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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

}
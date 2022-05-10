package com.donews.task.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import com.donews.task.R


/**
 * make in st
 * on 2022/5/7 15:52
 * 自定义圆形图片冷却倒计时
 */
@SuppressLint("AppCompatCustomView,CustomViewStyleable")
class ColdDownTimerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?,
    defStyle: Int = 0
) : ImageView(context, attrs, defStyle) {
    private val mContext: Context
    private val SCALE_TYPE = ScaleType.CENTER_CROP
    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    private val COLORDRAWABLE_DIMENSION = 1
    private val mDrawableRect = RectF()
    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mArcPaint = Paint()
    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0
    private var mDrawableRadius = 0f
    private val mReady: Boolean
    private var mSetupPending = false

    /**
     * 冷却中提示
     */
    private var waitHint: String? = "冷却中..."

    /**
     * 已过时间百分比
     */
    private var timePercent = 0f

    /**
     * 当前冷却时间
     */
    private var curCountDownTime = 0

    /**
     * 总冷却时间(默认5秒)
     */
    private var countdownTime = 5

    /**
     * 动画持续时间
     */
    private var animDuration = 0
    private var isCountDownOver = true
    private var countDownTimeListener: CountDownTimeListener? = null
    private var mHandler = Handler()

    override fun getScaleType(): ScaleType {
        return SCALE_TYPE
    }

    fun getIsCountDownOver() = isCountDownOver

    /**
     * 设置当前冷却时间
     *
     * @param time
     * @throws Exception
     */
    fun setCurCountTime(time: Int) {
        animDuration = time
        curCountDownTime = animDuration
        require(curCountDownTime <= countdownTime) { "当前冷却时间大于总冷却时间" }
    }

    /**
     * 设置总冷却时间
     *
     * @param time
     * @throws Exception
     */
    fun setCountTime(time: Int) {
        countdownTime = time
        require(curCountDownTime <= countdownTime) { "当前冷却时间大于总冷却时间" }
    }

    /**
     * 设置冷却中提示
     *
     * @param waitHint
     */
    fun setWaitHint(waitHint: String?) {
        this.waitHint = waitHint
    }

    /**
     * 设置冷却监听器
     *
     * @param countDownTimeListener
     */
    fun setOnCountDownTimeListener(
        countDownTimeListener: CountDownTimeListener?
    ) {
        this.countDownTimeListener = countDownTimeListener
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            return
        }
        canvas.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(), mDrawableRadius,
            mBitmapPaint
        )
        if (!isCountDownOver) {
            canvas.drawArc(
                mDrawableRect, 270f, -(360 * (1 - timePercent)),
                true, mArcPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    COLORDRAWABLE_DIMENSION,
                    COLORDRAWABLE_DIMENSION, BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight, BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
            null
        }
    }

    private fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }
        if (mBitmap == null) {
            return
        }
        mBitmapShader = BitmapShader(
            mBitmap!!, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader
        mArcPaint.isAntiAlias = true
        mArcPaint.style = Paint.Style.FILL_AND_STROKE
        mArcPaint.alpha = 122
        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width
        mDrawableRect[0f, 0f, width.toFloat()] = height.toFloat()
        mDrawableRadius = (mDrawableRect.height() / 2).coerceAtMost(mDrawableRect.width() / 2)
        updateShaderMatrix()
        invalidate()
    }

    private fun updateShaderMatrix() {
        mShaderMatrix.set(null)
        val scale = if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
            * mBitmapHeight
        ) {
            mDrawableRect.height() / mBitmapHeight.toFloat()
        } else {
            mDrawableRect.width() / mBitmapWidth.toFloat()
        }
        mShaderMatrix.setScale(scale, scale)
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    /**
     * 开始倒计时
     */
    fun startCountdown() {
        if (!isCountDownOver) {
            Toast.makeText(mContext, waitHint, Toast.LENGTH_SHORT).show()
        } else {
            if (curCountDownTime == 0) {
                animDuration = countdownTime
                curCountDownTime = animDuration
            }
            isCountDownOver = false
            val valueA = getValueAnimator()
            valueA.addUpdateListener { animation ->
                timePercent = animation.animatedValue as Float
                invalidate()
            }
            valueA.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (curCountDownTime > 0) {
                        isCountDownOver = false
                    } else {
                        if (null != countDownTimeListener) {
                            countDownTimeListener!!.countDownFinish()
                        }
                        curCountDownTime = animDuration
                        isCountDownOver = true
                        mHandler.removeCallbacks(runnable)
                    }
                }
            })
            valueA.start()
            mHandler.post(runnable)
        }
    }

    /**
     * 获取值动画
     *
     * @return
     */

    private fun getValueAnimator() : ValueAnimator {
        return ValueAnimator.ofFloat(
            1 - curCountDownTime.toFloat() / countdownTime, 1f
        ).apply {
            duration = (animDuration * 1000).toLong()
            interpolator = LinearInterpolator()
            repeatCount = 0
        }
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            if (null != countDownTimeListener) {
                countDownTimeListener!!.getCurCountDownTime(curCountDownTime)
            }
            curCountDownTime--
            mHandler.postDelayed(this, 1000)
        }
    }

    interface CountDownTimeListener {
        /**
         * 获取当前冷却时间
         * @param time
         */
        fun getCurCountDownTime(time: Int)

        /**
         * 冷却计时结束
         */
        fun countDownFinish()
    }

    init {
        super.setScaleType(SCALE_TYPE)
        mContext = context
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.ColdDownTimer, defStyle, 0
        )
        waitHint = a.getString(R.styleable.ColdDownTimer_waitHint)
        a.recycle()
        mReady = true
        if (mSetupPending) {
            setup()
            mSetupPending = false
        }
    }
}
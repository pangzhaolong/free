package com.donews.mine.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * @author lcl
 * Date on 2021/9/10
 * Description:
 */
class SectionCornerMessageLayout : RelativeLayout {
    companion object {
        /** 不圆角 */
        const val MODE_NONE = 0
        /** 全部圆角 */
        const val MODE_ALL = 1
        /** 左边圆角 */
        const val MODE_LEFT = 2
        /** 顶部圆角 */
        const val MODE_TOP = 3
        /** 右边圆角 */
        const val MODE_RIGHT = 4
        /** 底部圆角 */
        const val MODE_BOTTOM = 5

        //单个圆角的处理。只圆角某一个
        /** 左边底部圆角 */
        const val MODE_LEFT_BOTTOM = 6
    }

    private var mPath: Path? = null
    private var mRadius = 0

    private var mWidth = 0
    private var mHeight = 0
    private var mLastRadius = 0

    private var mRoundMode = MODE_NONE

    init {
//        setBackgroundDrawable(ColorDrawable(0x33ff0000))
        mPath = Path()
        mPath!!.fillType = Path.FillType.EVEN_ODD
        setCornerRadius(5)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    /**
     * 设置左边圆角
     */
    fun setLeftCornerMode() {
        setRoundMode(MODE_LEFT)
    }

    /**
     * 设置左边圆角
     */
    fun setLeftBottomCornerMode() {
        setRoundMode(MODE_LEFT_BOTTOM)
    }

    /**
     * 设置顶部圆角
     */
    fun setTopCornerMode() {
        setRoundMode(MODE_TOP)
    }

    /**
     * 设置右边圆角
     */
    fun setRightCornerMode() {
        setRoundMode(MODE_RIGHT)
    }

    /**
     * 设置无圆角
     */
    fun setNoneCornerMode() {
        setRoundMode(MODE_NONE)
    }

    /**
     * 设置全部圆角
     */
    fun setAllCornerMode() {
        setRoundMode(MODE_ALL)
    }

    /**
     * 设置是否圆角裁边
     * @param roundMode
     */
    fun setRoundMode(roundMode: Int) {
//        if (mRoundMode == roundMode && mWidth > 0 && mHeight > 0) {
//            return
//        }
        mRoundMode = roundMode
        checkPathChanged()
        invalidate()
    }

    /**
     * 设置圆角半径
     * @param radius
     */
    fun setCornerRadius(radius: Int) {
        mRadius = radius
        checkPathChanged()
        invalidate()
    }

    /**
     * 获取当前的圆角
     * @return Int
     */
    fun getCornerRadius():Int{
        return mRadius
    }

    override fun draw(canvas: Canvas) {
        if (mRoundMode != MODE_NONE) {
            val saveCount: Int = canvas.save()
            checkPathChanged()
            canvas.clipPath(mPath!!)
            super.draw(canvas)
            canvas.restoreToCount(saveCount)
        } else {
            super.draw(canvas)
        }
    }

    private fun checkPathChanged() {
        mWidth = width
        mHeight = height
        mLastRadius = mRadius
        mPath!!.reset()
        when (mRoundMode) {
            MODE_ALL -> mPath!!.addRoundRect(
                RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat()),
                mRadius.toFloat(),
                mRadius.toFloat(),
                Path.Direction.CW
            )
            MODE_LEFT -> mPath!!.addRoundRect(
                RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat()),
                floatArrayOf(
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    0f,
                    0f,
                    0f,
                    0f,
                    mRadius.toFloat(),
                    mRadius.toFloat()
                ),
                Path.Direction.CW
            )
            MODE_TOP -> mPath!!.addRoundRect(
                RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat()),
                floatArrayOf(
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    0f,
                    0f,
                    0f,
                    0f
                ),
                Path.Direction.CW
            )
            MODE_RIGHT -> mPath!!.addRoundRect(
                RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat()),
                floatArrayOf(
                    0f,
                    0f,
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    0f,
                    0f
                ),
                Path.Direction.CW
            )
            MODE_BOTTOM -> mPath!!.addRoundRect(
                RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat()),
                floatArrayOf(
                    0f,
                    0f,
                    0f,
                    0f,
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    mRadius.toFloat(),
                    mRadius.toFloat()
                ),
                Path.Direction.CW
            )
            //左边底部圆角
            MODE_LEFT_BOTTOM -> mPath!!.addRoundRect(
                RectF(0F, 0F, mWidth.toFloat(), mHeight.toFloat()),
                floatArrayOf(
                    0F,
                    0F,
                    0f,
                    0f,
                    0f,
                    0f,
                    mRadius.toFloat(),
                    mRadius.toFloat()
                ),
                Path.Direction.CW
            )
        }
    }


}
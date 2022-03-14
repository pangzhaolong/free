package com.donews.mail.views

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.util.AttributeSet
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 * @author lcl
 * Date on 2021/10/15
 * Description:
 */
class MailClassicsHeader : ClassicsHeader {

    private val mAnimating = true
    private var mTranslateTitle = 0F
    private var mTranslateDesc = 0F

    //顶部下拉刷新的标题文字颜色线性渲染
    private var mRefesehHeadTitleReLinearGradient: LinearGradient? = null
    private var mGradientTitleMatrix: Matrix = Matrix()

    //顶部下拉刷新的文字描述颜色线性渲染
    private var mRefesehHeadDescReLinearGradient: LinearGradient? = null
    private var mGradientDescMatrix: Matrix = Matrix()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawTextTitleGradient()
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        super.onStateChanged(refreshLayout, oldState, newState)
        drawTextTitleGradient()
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        drawTextTitleGradient()
        return super.onFinish(layout, success)
    }

    //绘制渐变内容(标题)
    private fun drawTextTitleGradient() {
        if (mTranslateTitle == 0F || mTranslateTitle != mTitleText.width.toFloat()) {
            mTranslateTitle = mTitleText.width.toFloat()
            //绘制一个线性渐变。位置为文本框的 -width 的位置到 当前位置x的位置，也就和文本框通宽度。但是再当前空间的左侧
            mRefesehHeadTitleReLinearGradient = LinearGradient(
                -mTranslateTitle, 0F, 0F, 0F,
                intArrayOf(0xFFFFFF99.toInt(),0xFFFFFFFF.toInt()),
//                intArrayOf(0xFFFF0000.toInt(), 0xFFFFFFFF.toInt()),
                floatArrayOf(0F, 1F),
                Shader.TileMode.CLAMP
            )
            mTitleText.paint.shader = mRefesehHeadTitleReLinearGradient!!
        }
        if (mAnimating && mRefesehHeadTitleReLinearGradient != null) {
            //将线性颜色块。x上平移控件的宽度
            mGradientTitleMatrix.setTranslate(mTranslateTitle, 0F)
            //将矩阵的变化设置给线性布局
            mRefesehHeadTitleReLinearGradient!!.setLocalMatrix(mGradientTitleMatrix)
        }
        //更新描述区域
        drawTextDescGradient()
    }

    //绘制渐变内容(描述)
    private fun drawTextDescGradient() {
        if (mTranslateDesc == 0F || mTranslateDesc != mLastUpdateText.width.toFloat()) {
            mTranslateDesc = mLastUpdateText.width.toFloat()
            //绘制一个线性渐变。位置为文本框的 -width 的位置到 当前位置x的位置，也就和文本框通宽度。但是再当前空间的左侧
            mRefesehHeadDescReLinearGradient = LinearGradient(
                -mTranslateDesc, 0F, 0F, 0F,
                intArrayOf(0xFFFFFF99.toInt(),0xFFFFFFFF.toInt()),
//                intArrayOf(0xFFFF0000.toInt(), 0xFFFFFFFF.toInt()),
                floatArrayOf(0F, 1F),
                Shader.TileMode.CLAMP
            )
            mLastUpdateText.paint.shader = mRefesehHeadDescReLinearGradient!!
        }
        if (mAnimating && mRefesehHeadDescReLinearGradient != null) {
            //将线性颜色块。x上平移控件的宽度
            mGradientDescMatrix.setTranslate(mTranslateDesc, 0F)
            //将矩阵的变化设置给线性布局
            mRefesehHeadDescReLinearGradient!!.setLocalMatrix(mGradientDescMatrix)
        }
    }

}
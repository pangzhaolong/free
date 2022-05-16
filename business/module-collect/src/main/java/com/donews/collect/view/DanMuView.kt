package com.donews.collect.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import com.donews.collect.bean.DanMuBean

/**
 *  make in st
 *  on 2022/5/7 10:37
 */

class DanMuView constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var widthPixels = 0
    private var curPos = 0
    private val danMuList: MutableList<DanMuBean> = arrayListOf()

    init {
        initView()
    }

    private fun initView(){
        widthPixels = resources.displayMetrics.widthPixels
    }

    fun addDanMuView(){
        addTextView()
    }

    fun setData(dataList: MutableList<DanMuBean>) {
        danMuList.clear()
        danMuList.addAll(dataList)
    }

    private fun addTextView() {
        if (danMuList.isEmpty()) {
            return
        }
        if (curPos == danMuList.size) {
            //循环播放
            curPos = 0
        }
        val moveView = MoveView(context, danMuList[curPos++])
        addView(moveView)
        moveView.randomVerticalPos()
        startTranslateAnim(moveView)
    }

    /**
     * 设置从右往左移动动画,时间范围是5000-10000毫秒
     * @param view
     */
    private fun startTranslateAnim(view: View) {
        val randomDuration = (Math.random() * 4000 + 10000).toInt()
        val anim = TranslateAnimation(
            widthPixels.toFloat(),
            (-widthPixels).toFloat(), 0f, 0f
        )
        anim.duration = randomDuration.toLong()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                removeView(view)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(anim)
    }

}
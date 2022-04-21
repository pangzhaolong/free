package com.dn.sdk.bean.natives

import android.graphics.Bitmap

/**
 * 基础Draw自渲染广告数据对象
 *
 *  make in st
 *  on 2022/1/13 10:11
 */
interface ITTDrawFeedAdData: ITTFeedAd {

    fun setCanInterruptVideoPlay(var1: Boolean)

    fun setPauseIcon(var1: Bitmap?, var2: Int)

    fun setDrawVideoListener(var1: DrawVideoListener?)

    interface DrawVideoListener {
        fun onClickRetry()
        fun onClick()
    }
}
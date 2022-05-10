package com.donews.turntable.utils

class ClickDoubleUtil {

    companion object{
        /**
         * 两次点击按钮之间的点击间隔不能少于1000毫秒
         */
        private val MIN_CLICK_DELAY_TIME = 500

        /**
         * 最后一次点击的时间
         */
        private var mLastClickTime: Long = -1

        /**
         * 是否为快速点击
         *
         * @return true:快速点击  false:非快速点击
         */
        @JvmStatic
        fun isFastClick(): Boolean {
            val flag: Boolean
            val curClickTime = System.currentTimeMillis()
            flag = curClickTime - mLastClickTime >= MIN_CLICK_DELAY_TIME
            mLastClickTime = curClickTime
            return flag
        }
    }




}
package com.donews.task.util

import com.donews.base.utils.ext.isToday
import com.donews.utilslibrary.utils.SPUtils

/**
 *  make in st
 *  on 2022/5/9 15:25
 *  宝箱每日20打开次数判断类
 */
class DayBoxUtil {

    companion object {
        val instance: DayBoxUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DayBoxUtil()
        }
    }

    /*宝箱是否可打开*/
    fun isShowDayTwentyOpenBox(maxNum: Int = 20): Boolean {
        val todayOpenBoxNum = SPUtils.getInformain("todayOpenBoxNum", 0)
        val todayOpenBoxOpenTime = SPUtils.getLongInformain("todayOpenBoxOpenTime", 0L)
        return if (todayOpenBoxOpenTime.isToday()) {
            todayOpenBoxNum < maxNum
        } else {
            SPUtils.setInformain("todayOpenBoxNum", 0)
            SPUtils.setInformain("todayOpenBoxOpenTime", 0L)
            true
        }
    }

}
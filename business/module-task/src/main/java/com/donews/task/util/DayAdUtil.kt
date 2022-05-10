package com.donews.task.util
import com.donews.base.utils.ext.isToday
import com.donews.utilslibrary.utils.SPUtils

/**
 *  make in st
 *  on 2022/5/9 15:25
 *  宝箱每日20打开次数判断类
 */
class DayAdUtil {

    companion object {
        val instance: DayAdUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DayAdUtil()
        }
    }

    /*宝箱是否可打开*/
    fun isShowDayThreeSeeAd(maxNum: Int = 3): Boolean {
        val todaySeeAdNum = SPUtils.getInformain("todaySeeAdNum", 0)
        val todaySeeAdTime = SPUtils.getLongInformain("todaySeeAdTime", 0L)
        return if (todaySeeAdTime.isToday()) {
            todaySeeAdNum < maxNum
        } else {
            SPUtils.setInformain("todaySeeAdNum", 0)
            SPUtils.setInformain("todaySeeAdTime", 0L)
            true
        }
    }

}
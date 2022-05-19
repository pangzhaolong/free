package com.donews.collect.util

import com.donews.base.utils.ext.isToday
import com.donews.utilslibrary.utils.SPUtils

/**
 *  make in st
 *  on 2022/5/9 15:25
 *  宝箱每日20打开次数判断类
 */
class DayStepUtil {

    companion object {
        val instance: DayStepUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DayStepUtil()
        }
    }

    /*今日是否展示第1步过渡页*/
    fun isTodayShowOneStep(maxNum: Int = 1): Boolean {
        val todayShowOneStepNum = SPUtils.getInformain("todayShowOneStepNum", 0)
        val todayShowOneStepTime = SPUtils.getLongInformain("todayShowOneStepTime", 0L)
        return if (todayShowOneStepTime.isToday()) {
            todayShowOneStepNum < maxNum
        } else {
            SPUtils.setInformain("todayShowOneStepNum", 0)
            SPUtils.setInformain("todayShowOneStepTime", 0L)
            true
        }
    }

    fun setStepOneSp(num:Int = 0,time:Long = System.currentTimeMillis()){
        SPUtils.setInformain("todayShowOneStepNum", num)
        SPUtils.setInformain("todayShowOneStepTime", time)
    }

    /*今日是否展示第2步过渡页*/
    fun isTodayShowTwoStep(maxNum: Int = 1): Boolean {
        val todayShowTwoStepNum = SPUtils.getInformain("todayShowTwoStepNum", 0)
        val todayShowTwoStepTime = SPUtils.getLongInformain("todayShowTwoStepTime", 0L)
        return if (todayShowTwoStepTime.isToday()) {
            todayShowTwoStepNum < maxNum
        } else {
            SPUtils.setInformain("todayShowTwoStepNum", 0)
            SPUtils.setInformain("todayShowTwoStepTime", 0L)
            true
        }
    }

    fun setStepTwoSp(num:Int = 0,time:Long = System.currentTimeMillis()){
        SPUtils.setInformain("todayShowTwoStepNum", num)
        SPUtils.setInformain("todayShowTwoStepTime", time)
    }

    /*今日是否展示第3步过渡页*/
    fun isTodayShowThreeStep(maxNum: Int = 1): Boolean {
        val todayShowThreeStepNum = SPUtils.getInformain("todayShowThreeStepNum", 0)
        val todayShowThreeStepTime = SPUtils.getLongInformain("todayShowThreeStepTime", 0L)
        return if (todayShowThreeStepTime.isToday()) {
            todayShowThreeStepNum < maxNum
        } else {
            SPUtils.setInformain("todayShowThreeStepNum", 0)
            SPUtils.setInformain("todayShowThreeStepTime", 0L)
            true
        }
    }

    fun setStepThreeSp(num:Int = 0,time:Long = System.currentTimeMillis()){
        SPUtils.setInformain("todayShowThreeStepNum", num)
        SPUtils.setInformain("todayShowThreeStepTime", time)
    }

    /*今日是否展示第3步过渡页*/
    fun isTodayShowFourStep(maxNum: Int = 1): Boolean {
        val todayShowFourStepNum = SPUtils.getInformain("todayShowFourStepNum", 0)
        val todayShowFourStepTime = SPUtils.getLongInformain("todayShowFourStepTime", 0L)
        return if (todayShowFourStepTime.isToday()) {
            todayShowFourStepNum < maxNum
        } else {
            SPUtils.setInformain("todayShowFourStepNum", 0)
            SPUtils.setInformain("todayShowFourStepTime", 0L)
            true
        }
    }

    fun setStepFourSp(num:Int = 0,time:Long = System.currentTimeMillis()){
        SPUtils.setInformain("todayShowFourStepNum", num)
        SPUtils.setInformain("todayShowFourStepTime", time)
    }
}
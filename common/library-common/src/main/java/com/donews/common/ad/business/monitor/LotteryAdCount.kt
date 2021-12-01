package com.donews.common.ad.business.monitor

import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils
import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

/**
 *  抽奖广告统计
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 14:32
 */
object LotteryAdCount {

    private val mDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

    /** 总的抽奖次数 */
    private const val TOTAL_LOTTERY_TIME = "totalLotteryTime";

    /** 最新的打开app时间 */
    private const val NEW_OPEN_APP_TIME = "newOpenAppTime"

    /** 今天打开app次数 */
    private const val TODAY_OPEN_APP_TIMES = "todayOpenAppTimes"

    /** 最新抽奖时间 */
    private const val NEW_LOTTERY_TIME = "newLotteryTime"

    /** 今天抽奖次数 */
    private const val TODAY_LOTTERY_TIMES = "todayLotteryTimes"

    /** 退出app但是未抽奖次数 */
    private const val EXIT_APP_WITH_NOT_LOTTERY = "exitAppWithNotLottery"

    private val mmkv = MMKV.mmkvWithID("AdCount", MMKV.MULTI_PROCESS_MODE)!!


    fun init() {
        val newOpenAppTime = mmkv.decodeLong(NEW_OPEN_APP_TIME, 0L)
        //最新打开时间不是今天，则重置打开次数
        if (!isToday(newOpenAppTime)) {
            mmkv.encode(TODAY_OPEN_APP_TIMES, 1)
        } else {
            val todayOpenAppTimes = mmkv.decodeInt(TODAY_OPEN_APP_TIMES, 0)
            mmkv.encode(TODAY_OPEN_APP_TIMES, todayOpenAppTimes + 1)
        }
        //保存最新的app打开时间
        mmkv.encode(NEW_OPEN_APP_TIME, System.currentTimeMillis())
    }

    /**
     * 抽奖成功调用
     */
    fun lotterySuccess() {
        //记录总的抽奖次数
        var totalLotteryNumber = mmkv.decodeInt(TOTAL_LOTTERY_TIME, 0)
        totalLotteryNumber++
        mmkv.encode(TOTAL_LOTTERY_TIME, totalLotteryNumber)

        val newLotteryTime = mmkv.decodeLong(NEW_LOTTERY_TIME, 0L)
        if (!isToday(newLotteryTime)) {
            mmkv.encode(TODAY_LOTTERY_TIMES, 1)
        } else {
            val todayLotteryTimes = mmkv.decodeInt(TODAY_LOTTERY_TIMES, 0)
            mmkv.encode(TODAY_LOTTERY_TIMES, todayLotteryTimes + 1)
        }
        mmkv.encode(NEW_LOTTERY_TIME, System.currentTimeMillis())
        //抽奖以后重置退出app并且没有抽奖次数
        resetExitAppWithNotLotteryTimes()
    }

    /***
     * 退出app但是未抽奖次数
     */
    fun exitAppWithNotLottery() {
        val newOpenAppTime = mmkv.decodeLong(NEW_OPEN_APP_TIME, 0L)
        val newLotteryTime = mmkv.decodeLong(NEW_LOTTERY_TIME, 0L)
        val exitAppWithNotLotteryTimes = mmkv.decodeInt(EXIT_APP_WITH_NOT_LOTTERY, 0)
        if (newOpenAppTime > newLotteryTime) {
            mmkv.encode(EXIT_APP_WITH_NOT_LOTTERY, exitAppWithNotLotteryTimes + 1)
        }
    }

    fun getTotalLotteryNumber(): Int {
        return mmkv.decodeInt(TOTAL_LOTTERY_TIME, 0)
    }

    /** 返回退出app但是没有抽奖的次数 */
    fun getExitAppWithNotLotteryTimes(): Int {
        return mmkv.decodeInt(EXIT_APP_WITH_NOT_LOTTERY, 0)
    }

    fun resetExitAppWithNotLotteryTimes() {
        mmkv.encode(EXIT_APP_WITH_NOT_LOTTERY, 0)
    }

    /** 今天是否抽奖 */
    fun todayLottery(): Boolean {
        val newLotteryTime = mmkv.decodeLong(NEW_LOTTERY_TIME, 0L)
        val todayLotteryTimes = mmkv.decodeInt(TODAY_LOTTERY_TIMES, 0)
        return isToday(newLotteryTime) && todayLotteryTimes > 0
    }

    /** 今天是否抽奖(扩展方法),扩展方法：[todayLottery] */
    fun todayLotteryExt(): Boolean {
        val windCount = SPUtils.getInformain(KeySharePreferences.LOTTERY_COUNTS, 0)
        return windCount > 0
    }

    /**
     * 获取今日参与抽奖的次数
     * @return Int
     */
    fun getTodayLotteryCount(): Int {
        return mmkv.decodeInt(TODAY_LOTTERY_TIMES, 0)
    }

    /**
     * 获取抽奖激励视频播放完成次数
     * @return Int
     */
    fun getTodayLotteryCountExt(): Int {
        return SPUtils.getInformain(KeySharePreferences.LOTTERY_COUNTS, 0)
    }

    /**
     * 判断是否为今天
     * @param time Long 时间戳 毫秒
     * @return Boolean true，是今天，false 不是今天
     */
    private fun isToday(time: Long): Boolean {
        val curDate = Date(time)
        val today = Date()
        return mDataFormat.format(curDate) == mDataFormat.format(today)
    }

}
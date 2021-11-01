package com.donews.common.ad.business.monitor

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

    /** 最新的打开app时间 */
    private const val NEW_OPEN_APP_TIME = "newOpenAppTime"

    /** 今天打开app次数 */
    private const val TODAY_OPEN_APP_TIMES = "todayOpenAppTimes"

    /** 最新抽奖时间 */
    private const val NEW_LOTTERY_TIME = "newLotteryTime"

    /** 今天抽奖次数 */
    private const val TODAY_LOTTERY_TIMES = "todayLotteryTimes"

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
        val newLotteryTime = mmkv.decodeLong(NEW_LOTTERY_TIME, 0L)
        if (!isToday(newLotteryTime)) {
            mmkv.encode(TODAY_LOTTERY_TIMES, 1)
        } else {
            val todayLotteryTimes = mmkv.decodeInt(TODAY_LOTTERY_TIMES, 0)
            mmkv.encode(TODAY_LOTTERY_TIMES, todayLotteryTimes + 1)
        }
        mmkv.encode(NEW_LOTTERY_TIME, System.currentTimeMillis())
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
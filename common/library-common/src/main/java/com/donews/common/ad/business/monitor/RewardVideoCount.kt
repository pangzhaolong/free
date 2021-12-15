package com.donews.common.ad.business.monitor

import com.dn.sdk.utils.AdLoggerUtils
import com.donews.base.utils.ext.isToday
import com.donews.base.utils.ext.toDataString
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.tencent.mmkv.MMKV
import java.lang.StringBuilder

/**
 * 激励视频播放统计
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 15:31
 */
object RewardVideoCount {

    private val mmkv = MMKV.mmkvWithID("AdCount", MMKV.MULTI_PROCESS_MODE)!!

    /** 最新播放激励视频时间 */
    private const val REWARD_VIDEO_LAST_PLAY_TIME = "RewardVideoLastPlayTime"

    /** 今天激励视频播放次数 */
    private const val REWARD_VIDEO_TODAY_PLAY_NUMBER = "RewardVideoPlayNumbers"

    /** 同一小时类第一个激励视频播放时机 */
    private const val REWARD_VIDEO_HOUR_START_PLAY_TIME = "RewardVideoStartPlayTime"

    /** 一小时内播放次数 */
    private const val REWARD_VIDEO_HOUR_PLAY_NUMBER = "RewardVideoHourPlayNumbers"

    /** 小时内达到最大播放次数的限制开始时间 */
    private const val REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME = "RewardVideoHourPlayNumberLimitTime"

    /** 小时内播放时间限制开始时间 */
    private const val REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME = "RewardVideoHourClickLimitTime"

    /** 小时内激励视频点击次数 */
    private const val REWARD_VIDEO_HOUR_CLICK_NUMBER = "RewardVideoHourClickNumber"


    /** 判断是否可以显示广告 */
    fun checkShouldLoadAd(): Boolean {
        val lastPlayTime = mmkv.decodeLong(REWARD_VIDEO_LAST_PLAY_TIME, 0L)
        val currentTime = System.currentTimeMillis()

        var totalPlayNumber = 0
        var clickNumber = 0
        var hourNumber = 0
        var hourStartShowTime: Long = 0L
        var hourLimitStartTime = 0L
        var clickLimitStartTime = 0L
        if (lastPlayTime.isToday()) {
            totalPlayNumber = mmkv.decodeInt(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
            hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
            clickNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
            hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
            hourLimitStartTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
            clickLimitStartTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)

            if (currentTime - hourStartShowTime > 60 * 60 * 1000L) {
                mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
                mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
                mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
                hourLimitStartTime = currentTime
            }
        } else {
            mmkv.encode(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
            mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
            mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
            mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
            mmkv.removeValueForKey(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME)
            hourStartShowTime = currentTime
            mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, hourStartShowTime)
        }


        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        //每日达到最大播放次数
        if (totalPlayNumber >= jddAdConfigBean.todayMaxRewardVideoNumber) {
            log(3)
            return false
        }

        if (hourNumber == 0) {
            log(3)
            return true
        }

        //1小时内达到最大次数
        if (hourNumber >= jddAdConfigBean.hourRewardVideoNumber) {
            //判断限制时间是否已经过了
            if (currentTime - hourLimitStartTime > jddAdConfigBean.hourLimitTime) {
                //过了限制时间，则要重新开始 小时计时
                mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
                mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
                log(3)
                return true
            }
            log(3)
            return false
        }

        if (hourNumber >= jddAdConfigBean.hourClickAdMaxNumber) {
            val p = clickNumber * 1.0f / hourNumber
            //判断点击率是否达标
            if (p >= jddAdConfigBean.hourClickRate) {
                //判断禁用时间是否超时
                if (currentTime - clickLimitStartTime > jddAdConfigBean.hourClickLimitTime) {
                    //过了限制时间，则要重新开始 小时计时
                    mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
                    mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
                    mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
                    return true
                }
                log(3)
                return false
            }
        }
        log(3)
        return true
    }


    /** 播放激励视频成功 */
    fun playRewardVideoSuccess() {
        val currentTime = System.currentTimeMillis()
        val lastPlayTime = mmkv.decodeLong(REWARD_VIDEO_LAST_PLAY_TIME, 0L)

        //统计总次数
        if (!lastPlayTime.isToday()) {
            mmkv.encode(REWARD_VIDEO_TODAY_PLAY_NUMBER, 1)
        } else {
            val todayPlayTimes = mmkv.decodeInt(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
            mmkv.encode(REWARD_VIDEO_TODAY_PLAY_NUMBER, todayPlayTimes + 1)
        }
        mmkv.encode(REWARD_VIDEO_LAST_PLAY_TIME, currentTime)

        //统计 小时数据
        var hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)

        if (!hourStartShowTime.isToday()) {
            //重置时间，次数,点击次数
            mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
            mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 1)
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
            mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
        } else {
            val duration = currentTime - hourStartShowTime;
            val hour = 60 * 60 * 1000L
            if (duration >= hour) {
                //2次间隔大于60分钟，则超过1小时，重置时间，次数,点击次数
                mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 1)
                mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
                mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
            } else {
                //小时播放次数+1
                val playTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, playTime + 1)
            }
        }


        // 判断限制时间
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
        val hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
        val clickNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)


        if (hourNumber >= jddAdConfigBean.hourRewardVideoNumber) {
            val limitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
            if (limitTime <= hourStartShowTime) {
                //设置时间，开始限制
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, currentTime)
            }
        }

        if (hourNumber == 0) {
            log(1)
            return
        }

        if (hourNumber >= jddAdConfigBean.hourClickAdMaxNumber) {
            val p = clickNumber * 1.0f / hourNumber
            if (p > jddAdConfigBean.hourClickRate) {
                val limitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
                if (limitTime <= hourStartShowTime) {
                    //设置时间，开始点击限制
                    mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, currentTime)
                }
            }
        }
        log(1)
    }

    /** 激励视频点击 */
    fun rewardVideoClick() {
        //统计 小时数据
        val hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
        var clickNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
        val hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
        val currentTime = System.currentTimeMillis()
        val duration = currentTime - hourStartShowTime;
        val hour = 60 * 60 * 1000L

        //如果点击的时候，距离1小时的判断已经过期，则不再记录点击次数
        if (duration < hour) {
            clickNumber++
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, clickNumber)
            val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
            var p = 0f
            if (hourNumber > 0) {
                p = clickNumber * 1.0f / hourNumber
            }
            if (hourNumber >= jddAdConfigBean.hourClickAdMaxNumber) {
                if (p >= jddAdConfigBean.hourClickRate) {
                    val limitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
                    if (limitTime <= hourStartShowTime) {
                        mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, currentTime)
                    }
                }
            }
        }
        log(2)
    }

    /** 今天播放激励视频次数 */
    fun todayPlayRewardVideoTimes(): Int {
        val newPlayTime = mmkv.decodeLong(REWARD_VIDEO_LAST_PLAY_TIME, 0L)
        return if (!newPlayTime.isToday()) {
            0
        } else {
            mmkv.decodeInt(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
        }
    }

    /** 重置激励视频次数 */
    fun resetTodayPlayRewardVideoTimes() {
        mmkv.encode(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
    }


    private fun log(type: Int) {
        val totalPlayNumber = mmkv.decodeInt(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
        val hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
        val hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
        val hourPlayLimitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
        val clickNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
        val clickPlayLimitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)

        val stringBuilder = StringBuilder()
            .apply {

                when (type) {
                    1 -> {
                        append("playRewardVideoSuccess {\n")
                    }
                    2 -> {
                        append("rewardVideoClick {\n")
                    }
                    3 -> {
                        append("loadCheckShouldLoadAd {\n")
                    }
                }

                append("  今日播放次数： ${totalPlayNumber},\n")
                append("  1小时开始计时时间：${hourStartShowTime.toDataString()},\n")
                append("  1小时播放量：${hourNumber},\n")
                append("  1小时播放量开始限制时间：${hourPlayLimitTime.toDataString()},\n")
                append("  点击量：${clickNumber},\n")
                if (hourNumber != 0) {
                    append("  点击率：${clickNumber * 1.0f / hourNumber},\n")
                } else {
                    append("  点击率：0,\n")
                }
                append("  点击限制开始时间：${clickPlayLimitTime.toDataString()}\n")
                append("}")
            }

        AdLoggerUtils.d(stringBuilder.toString())
    }
}
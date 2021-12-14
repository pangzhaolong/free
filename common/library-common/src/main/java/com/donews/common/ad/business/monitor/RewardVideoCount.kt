package com.donews.common.ad.business.monitor

import com.donews.base.utils.ext.isToday
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.tencent.mmkv.MMKV

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

    /** 播放激励视频成功 */
    fun playRewardVideoSuccess() {
        val currentTime = System.currentTimeMillis()
        val newPlayTime = mmkv.decodeLong(REWARD_VIDEO_LAST_PLAY_TIME, 0L)
        var hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
        var clickNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
        if (!newPlayTime.isToday()) {
            hourNumber = 0
            clickNumber = 0
        }

        //统计总次数
        if (!newPlayTime.isToday()) {
            mmkv.encode(REWARD_VIDEO_TODAY_PLAY_NUMBER, 1)
        } else {
            val todayPlayTimes = mmkv.decodeInt(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
            mmkv.encode(REWARD_VIDEO_TODAY_PLAY_NUMBER, todayPlayTimes + 1)
        }
        mmkv.encode(REWARD_VIDEO_LAST_PLAY_TIME, currentTime)

        //统计 小时数据
        val hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
        if (!hourStartShowTime.isToday()) {
            //重置时间，次数,点击次数
            mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
            mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 1)
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
        } else {
            val duration = currentTime - hourStartShowTime;
            val hour = 60 * 60 * 1000L
            if (duration > hour) {
                //2次间隔大于60分钟，则超过1小时，重置时间，次数,点击次数
                mmkv.encode(REWARD_VIDEO_HOUR_START_PLAY_TIME, currentTime)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, 1)
                mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
            } else {
                //播放次数+1
                val playTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER, playTime + 1)
            }
        }


        // 判断限制时间
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        if (hourNumber > jddAdConfigBean.hourRewardVideoNumber) {
            val limitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
            if (limitTime <= hourStartShowTime) {
                mmkv.encode(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, currentTime)
            }
        }

        if (hourNumber == 0) {
            return
        }

        if (hourNumber > jddAdConfigBean.hourClickAdMaxNumber) {
            val p = clickNumber * 1.0f / hourNumber
            val limitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
            if (p > jddAdConfigBean.hourClickRate) {
                if (limitTime <= hourStartShowTime) {
                    mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, currentTime)
                }
            }
        }
    }

    /** 激励视频点击 */
    fun rewardVideoClick() {
        //统计 小时数据
        val hourStartShowTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
        val clickNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
        val hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
        val currentTime = System.currentTimeMillis()
        val duration = currentTime - hourStartShowTime;
        val hour = 60 * 60 * 1000L
        //如果点击的时候，距离1小时的判断已经过期，则不再记录点击次数
        if (duration < hour) {
            mmkv.encode(REWARD_VIDEO_HOUR_CLICK_NUMBER, clickNumber + 1)

            val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
            var p = 0f
            if (hourNumber > 0) {
                p = clickNumber * 1.0f / hourNumber
            }
            val limitTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)
            if (p > jddAdConfigBean.hourClickRate) {
                if (limitTime <= hourStartShowTime) {
                    mmkv.encode(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, currentTime)
                }
            }
        }
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

    /** 判断是否可以显示广告 */
    fun checkShouldLoadAd(): Boolean {
        val newPlayTime = mmkv.decodeLong(REWARD_VIDEO_LAST_PLAY_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        var todayNumber = 0
        var hourNumber = 0
        if (newPlayTime.isToday()) {
            todayNumber = mmkv.decodeInt(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
            hourNumber = mmkv.decodeInt(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0)
        }

        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
        //每日达到最大播放次数
        if (todayNumber >= jddAdConfigBean.todayMaxRewardVideoNumber) {
            return false
        }

        if (hourNumber == 0) {
            return true
        }

        //1小时内达到最大次数
        if (hourNumber >= jddAdConfigBean.hourRewardVideoNumber) {
            //判断限制时间是否已经过了
            val limitStartTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
            if (currentTime - limitStartTime > jddAdConfigBean.hourLimitTime) {
                return true
            }
            return false
        }


        if (hourNumber > jddAdConfigBean.hourClickAdMaxNumber) {
            val clickNumber = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
            val p = clickNumber * 1.0f / hourNumber
            //判断点击率是否达标
            if (p > jddAdConfigBean.hourClickRate) {
                //判断禁用时间是否超时
                val limitStartTime = mmkv.decodeLong(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0)
                if (currentTime - limitStartTime > jddAdConfigBean.hourClickLimitTime) {
                    return true
                }

                return false
            }
        }
        return true
    }
}
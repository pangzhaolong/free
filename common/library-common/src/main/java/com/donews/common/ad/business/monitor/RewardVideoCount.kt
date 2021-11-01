package com.donews.common.ad.business.monitor

import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

/**
 * 激励视频播放统计
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 15:31
 */
object RewardVideoCount {

    /** 最新播放激励视频时间 */
    private const val REWARD_VIDEO_NEW_PLAY_TIME = "RewardVideoNewPlayTime"

    /** 今天激励视频播放次数 */
    private const val REWARD_VIDEO_PLAY_TIMES = "reward_video_play_times"


    private val mDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    private val mmkv = MMKV.mmkvWithID("AdCount", MMKV.MULTI_PROCESS_MODE)!!


    fun playRewardVideoSuccess() {
        val newPlayTime = mmkv.decodeLong(REWARD_VIDEO_NEW_PLAY_TIME, 0L)
        if (!isToday(newPlayTime)) {
            mmkv.encode(REWARD_VIDEO_PLAY_TIMES, 1)
        } else {
            val todayPlayTimes = mmkv.decodeInt(REWARD_VIDEO_PLAY_TIMES, 0)
            mmkv.encode(REWARD_VIDEO_PLAY_TIMES, todayPlayTimes + 1)
        }
        mmkv.encode(REWARD_VIDEO_NEW_PLAY_TIME, System.currentTimeMillis())
    }

    /** 今天播放激励视频次数 */
    fun todayPlayRewardVideoTimes(): Int {
        val newPlayTime = mmkv.decodeLong(REWARD_VIDEO_NEW_PLAY_TIME, 0L)
        return if (!isToday(newPlayTime)) {
            0
        } else {
            mmkv.decodeInt(REWARD_VIDEO_PLAY_TIMES, 0)
        }
    }

    /** 重置激励视频次数 */
    fun resetTodayPlayRewardVideoTimes() {
        mmkv.encode(REWARD_VIDEO_PLAY_TIMES, 0)
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
package com.donews.common.ad.business.monitor

import com.dn.sdk.BuildConfig
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.base.utils.ext.isToday
import com.donews.base.utils.ext.toDataString
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.CallBack
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.ext.int
import com.donews.utilslibrary.ext.long
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.DeviceUtils
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils
import com.tencent.mmkv.MMKV
import org.json.JSONObject
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


    private var lastPlayTime: Long by mmkv.long(REWARD_VIDEO_LAST_PLAY_TIME, 0L)
    private var todayPlayNumber: Int by mmkv.int(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)
    private var hourStartTime: Long by mmkv.long(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
    private var hourPlayNumber: Int by mmkv.int(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
    private var hourPlayNumberLimitTime: Long by mmkv.long(REWARD_VIDEO_HOUR_PLAY_NUMBER_LIMIT_TIME, 0L)
    private var hourClickNumber: Int by mmkv.int(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
    private var hourClickLimitTime: Long by mmkv.long(REWARD_VIDEO_HOUR_CLICK_LIMIT_TIME, 0L)

    private const val HOUR_TIME = 60 * 60 * 1000L

    /** 判断是否可以显示广告 */
    fun checkShouldLoadAd(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (lastPlayTime.isToday()) {
            if (currentTime - hourStartTime > HOUR_TIME) {
                hourStartTime = currentTime
                hourPlayNumber = 0
                hourClickNumber = 0
                hourPlayNumberLimitTime = 0L
                hourClickLimitTime = 0L
            }
        } else {
            todayPlayNumber = 0
            hourStartTime = currentTime
            hourPlayNumber = 0
            hourPlayNumberLimitTime = 0L
            hourClickNumber = 0
            hourClickLimitTime = 0L
        }

        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean

        //每日达到最大播放次数
        if (todayPlayNumber >= jddAdConfigBean.todayMaxRewardVideoNumber) {
            log(3)
            return false
        }

        if (hourPlayNumber == 0) {
            log(3)
            return true
        }

        //1小时内达到最大次数
        if (hourPlayNumber >= jddAdConfigBean.hourRewardVideoNumber) {
            //判断限制时间是否已经过了
            if (currentTime - hourPlayNumberLimitTime > jddAdConfigBean.hourLimitTime) {
                //过了限制时间，则要重新开始 小时计时
                hourStartTime = currentTime
                hourPlayNumber = 0
                hourClickNumber = 0
                log(3)
                return true
            }
            log(3)
            return false
        }

        if (hourPlayNumber >= jddAdConfigBean.hourClickAdMaxNumber) {
            val p = hourClickNumber * 1.0f / hourPlayNumber
            //判断点击率是否达标
            if (p >= jddAdConfigBean.hourClickRate) {
                //判断禁用时间是否超时
                if (currentTime - hourClickLimitTime > jddAdConfigBean.hourClickLimitTime) {
                    //过了限制时间，则要重新开始 小时计时
                    hourStartTime = currentTime
                    hourPlayNumber = 0
                    hourClickNumber = 0
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
    fun playRewardVideoSuccess(adStatus: AdStatus?) {
        // 判断限制时间
        val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean

        val currentTime = System.currentTimeMillis()

        //统计总次数
        if (!lastPlayTime.isToday()) {
            todayPlayNumber = 1
        } else {
            todayPlayNumber += 1
        }

        lastPlayTime = currentTime

        //今天需要重新开始计数
        if (!hourStartTime.isToday()) {
            //重置时间，次数,点击次数
            hourStartTime = currentTime
            //这里播放次数为1
            hourPlayNumber = 1
            hourPlayNumberLimitTime = 0L
            hourClickNumber = 0
            hourClickLimitTime = 0L
        } else {
            val duration = currentTime - hourStartTime;
            if (duration >= HOUR_TIME) {
                //2次间隔大于60分钟，则超过1小时，重置时间，次数,点击次数
                hourStartTime = currentTime
                hourPlayNumber = 1
                hourPlayNumberLimitTime = 0
                hourClickNumber = 0
                hourClickLimitTime = 0
            } else {
                //小时播放次数+1
                hourPlayNumber += 1
            }
        }

        if (todayPlayNumber >= jddAdConfigBean.todayMaxRewardVideoNumber) {
            adLimit()
        }


        if (hourPlayNumber >= jddAdConfigBean.hourRewardVideoNumber) {
            if (hourPlayNumberLimitTime <= hourStartTime) {
                //设置时间，开始限制
                hourPlayNumberLimitTime = currentTime
                adLimit()
            }
        }

        if (hourPlayNumber == 0) {
            log(1)
            return
        }

        if (hourPlayNumber >= jddAdConfigBean.hourClickAdMaxNumber) {
            val p = hourClickNumber * 1.0f / hourPlayNumber
            if (p > jddAdConfigBean.hourClickRate) {
                if (hourClickLimitTime <= hourStartTime) {
                    //设置时间，开始点击限制
                    hourClickLimitTime = currentTime
                    adLimit()
                }
            }
        }
        log(1)
    }

    /** 激励视频点击 */
    fun rewardVideoClick() {
        //统计 小时数据
        val currentTime = System.currentTimeMillis()
        val duration = currentTime - hourStartTime;

        //如果点击的时候，距离1小时的判断已经过期，则不再记录点击次数
        if (duration < HOUR_TIME) {
            hourClickNumber += 1
            val jddAdConfigBean = JddAdConfigManager.jddAdConfigBean
            var p = 0f
            if (hourPlayNumber > 0) {
                p = hourClickNumber * 1.0f / hourPlayNumber
            }
            if (hourPlayNumber >= jddAdConfigBean.hourClickAdMaxNumber) {
                if (p >= jddAdConfigBean.hourClickRate) {
                    if (hourClickLimitTime <= hourStartTime) {
                        //开始限制
                        hourClickLimitTime = currentTime
                        adLimit()
                    }
                }
            }
        }
        log(2)
    }

    /** 今天播放激励视频次数 */
    fun todayPlayRewardVideoTimes(): Int {
        return if (lastPlayTime.isToday()) {
            0
        } else {
            todayPlayNumber
        }
    }

    /** 重置激励视频次数 */
    fun resetTodayPlayRewardVideoTimes() {
        todayPlayNumber = 0
    }

    private fun log(type: Int) {
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
                append("  今日播放次数： ${todayPlayNumber},\n")
                append("  1小时开始计时时间：${hourStartTime.toDataString()},\n")
                append("  1小时播放量：${hourPlayNumber},\n")
                append("  1小时播放量开始限制时间：${hourPlayNumberLimitTime.toDataString()},\n")
                append("  点击量：${hourClickNumber},\n")
                if (hourPlayNumber != 0) {
                    append("  点击率：${hourClickNumber * 1.0f / hourPlayNumber},\n")
                } else {
                    append("  点击率：0,\n")
                }
                append("  点击限制开始时间：${hourClickLimitTime.toDataString()}\n")
                append("}")
            }
        AdLoggerUtils.d(stringBuilder.toString())
    }


    private fun adLimit() {
        try {
            var jsonString = ""
            val jsonObject = JSONObject()
            jsonObject.put("suuid", DeviceUtils.getMyUUID())
            jsonObject.put("user_id", AppInfo.getUserId())
            jsonObject.put("package_name", DeviceUtils.getPackage())
            jsonString = jsonObject.toString()

            val url = if (BuildConfig.HTTP_DEBUG) {
                "http://ecpm-customer.dev.tagtic.cn/api/v1/ad-limit"
            } else {
                "http://ecpm-customer.xg.tagtic.cn/api/v1/ad-limit"
            }
            AdLoggerUtils.d(" 上报限制开始:$jsonString")
            EasyHttp.post(url)
                .upJson(jsonString)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException?) {
                        AdLoggerUtils.d("上报限制失败：${e.toString()}")
                    }

                    override fun onSuccess(t: String?) {
                        AdLoggerUtils.d("上报限制成功：$t")
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
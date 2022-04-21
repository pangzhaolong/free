package com.donews.yfsdk.check

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.BuildConfig
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.base.utils.ext.isToday
import com.donews.base.utils.ext.toDataString
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.CallBack
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.ext.boolean
import com.donews.utilslibrary.ext.int
import com.donews.utilslibrary.ext.long
import com.donews.utilslibrary.sdk.getSuuid
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.DeviceUtils
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils
import com.donews.yfsdk.bean.adaction.AdAction
import com.donews.yfsdk.manager.AdConfigManager
import com.donews.yfsdk.queue.LoadTimeQueue
import com.tencent.mmkv.MMKV
import org.json.JSONObject

/**
 * 激励视频播放统计
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 15:31
 */
object RewardVideoCheck {

    private val mmkv = MMKV.mmkvWithID("AdCount", MMKV.MULTI_PROCESS_MODE)!!

    /** 最新播放激励视频时间 */
    private const val REWARD_VIDEO_LAST_PLAY_TIME = "RewardVideoLastPlayTime"

    /** 今天加载激励视频次数 */
    private const val REWARD_VIDEO_LOAD_COUNT = "RewardVideoLoadCount"

    /** 今天加载视频NoFill*/
    private const val REWARD_VIDEO_LOAD_NO_FILL_COUNT = "RewardVideoLoadNoFillCount"

    /** 今天加载视频Success*/
    private const val REWARD_VIDEO_LOAD_SUCCESS_COUNT = "RewardVideoLoadSuccessCount"

    /** 今天激励视频播放次数 */
    private const val REWARD_VIDEO_TODAY_PLAY_NUMBER = "RewardVideoPlayNumbers"

    /** 今天load开始时间*/
    private const val REWARD_VIDEO_TODAY_LOAD_TIME = "RewardVideoTodayLoadTime"

    /** 同一小时类第一个激励视频播放时机 */
    private const val REWARD_VIDEO_HOUR_START_PLAY_TIME = "RewardVideoStartPlayTime"

    /** 一小时内播放次数 */
    private const val REWARD_VIDEO_HOUR_PLAY_NUMBER = "RewardVideoHourPlayNumbers"

    /** 今日内连续请求失败次数 */
    private const val REWARD_VIDEO_DAY_LOAD_FAILED_NUMBER = "RewardVideoDayLoadFailedNumber"

    /** 今日内连续请求失败限制开始时间*/
    private const val REWARD_VIDEO_FOR_CLICK_PLAY_NUMBER = "RewardVideoForClickPlayNumber"

    /** 小时内激励视频点击次数 */
    private const val REWARD_VIDEO_HOUR_CLICK_NUMBER = "RewardVideoHourClickNumber"

    private const val REWARD_VIDEO_REPORT_CLICK = "RewardVideoReportClick"
    private const val REWARD_VIDEO_REPORT_CLICK_EX = "RewardVideoReportClickEx"

    /** 单广告重复点击次数 */
    private const val REWARD_VIDEO_ONE_AD_CLICK_REPEAT_COUNT = "RewardVideoOneAdClickRepeatCount"


    private var lastPlayTime: Long by mmkv.long(REWARD_VIDEO_LAST_PLAY_TIME, 0L)
    private var todayPlayNumber: Int by mmkv.int(REWARD_VIDEO_TODAY_PLAY_NUMBER, 0)

    private var todayLoadTime: Long by mmkv.long(REWARD_VIDEO_TODAY_LOAD_TIME, 0)

    private var hourStartTime: Long by mmkv.long(REWARD_VIDEO_HOUR_START_PLAY_TIME, 0L)
    private var hourPlayNumber: Int by mmkv.int(REWARD_VIDEO_HOUR_PLAY_NUMBER, 0)
    private var dayClickNumber: Int by mmkv.int(REWARD_VIDEO_HOUR_CLICK_NUMBER, 0)
    private var dayForClickPlayNumber: Int by mmkv.int(REWARD_VIDEO_FOR_CLICK_PLAY_NUMBER, 0)

    private var dayLoadFailedNumber: Int by mmkv.int(REWARD_VIDEO_DAY_LOAD_FAILED_NUMBER, 0)

    private const val HOUR_TIME = 60 * 60 * 1000L

    private var loadAdCount: Int by mmkv.int(REWARD_VIDEO_LOAD_COUNT, 0)
    private var loadAdSuccessCount: Int by mmkv.int(REWARD_VIDEO_LOAD_SUCCESS_COUNT, 0)
    private var loadAdNoFillCount: Int by mmkv.int(REWARD_VIDEO_LOAD_NO_FILL_COUNT, 0)


    private var loadTimeQueue: LoadTimeQueue = LoadTimeQueue(mmkv)
    private var loadTimeQueueEx: LoadTimeQueue = LoadTimeQueue(mmkv)

    private var dayClickRate: Float = 0f
    private var hasReportClick: Boolean by mmkv.boolean(REWARD_VIDEO_REPORT_CLICK, false)
    private var hasReportClickEx: Boolean by mmkv.boolean(REWARD_VIDEO_REPORT_CLICK_EX, false)

    private var oneAdClickRepeatCount: Int by mmkv.int(REWARD_VIDEO_ONE_AD_CLICK_REPEAT_COUNT, 0)

    private var currentEcpm: String = ""

    private var currentAdRequestId: String = ""

    @Synchronized
    fun isEnable(): AdCustomError {
        if (!AdConfigManager.mNormalAdBean.enable) {
            return AdCustomError.CloseAdAll
        }

        if (!AdConfigManager.mNormalAdBean.rewardVideo.enable) {
            return AdCustomError.CloseAdOne
        }

        return AdCustomError.OK
    }

    /** 判断是否可以显示广告 */
    fun showLoadAdInfo() {
        val currentTime = System.currentTimeMillis()
        if (!todayLoadTime.isToday()) {
            todayLoadTime = currentTime
            todayPlayNumber = 0
            loadAdCount = 0
            loadAdNoFillCount = 0
            loadAdSuccessCount = 0
            dayClickNumber = 0
            hasReportClick = false
            hasReportClickEx = false
            loadTimeQueue.removeAll()
            loadTimeQueueEx.removeAll()
        }
        checkLoadTimeQueue()
        log(3)
    }

    fun checkLoadTimeQueue() {
        if (loadTimeQueue.getCapacity() != AdConfigManager.mNormalAdBean.rewardVideo.limit.H1Watch) {
            loadTimeQueue.initSize(AdConfigManager.mNormalAdBean.rewardVideo.limit.H1Watch, "loadTimeQueue")
        }
        if (loadTimeQueueEx.getCapacity() != AdConfigManager.mNormalAdBean.rewardVideo.limit.H1WatchEx) {
            loadTimeQueueEx.initSize(AdConfigManager.mNormalAdBean.rewardVideo.limit.H1WatchEx, "loadTimeQueueEx")
        }
    }

    /** 播放激励视频成功 */
    fun onAdShow() {
        checkLoadTimeQueue()

        //统计总次数
        if (!lastPlayTime.isToday()) {
            todayPlayNumber = 1
            dayForClickPlayNumber = 1
        } else {
            todayPlayNumber++
            dayForClickPlayNumber++
        }

        lastPlayTime = System.currentTimeMillis()
        loadTimeQueue.put(lastPlayTime)
        loadTimeQueueEx.put(lastPlayTime)

        if (loadTimeQueue.isFull() && !loadTimeQueue.isReported() && loadTimeQueue.getLast() - loadTimeQueue.getHead() <= HOUR_TIME) {
            reportAdAction("HourAdsOver")
        }
        if (loadTimeQueueEx.isFull() && !loadTimeQueueEx.isReported() && loadTimeQueueEx.getLast() - loadTimeQueueEx.getHead() <= HOUR_TIME) {
            reportAdAction("HourAdsOver1")
        }

        if (todayPlayNumber == AdConfigManager.mNormalAdBean.rewardVideo.limit.D1Watch) {
            reportAdAction("DailyAdsOver")
        }

        if (todayPlayNumber >= AdConfigManager.mNormalAdBean.rewardVideo.limit.D1WatchEx) {
            reportAdAction("DailyAdsOver1")
        }

        log(1)
    }

    /** 激励视频点击 */
    fun onAdVideoClick(adStatus: AdStatus?) {
        dayClickNumber += 1
        if (dayForClickPlayNumber >= AdConfigManager.mNormalAdBean.rewardVideo.limit.ClickWatch) {

            dayClickRate = dayClickNumber * 1.0f / dayForClickPlayNumber

            // 当日广告点击率限制
            if (dayClickRate * 100 > AdConfigManager.mNormalAdBean.rewardVideo.limit.Click
                    && !hasReportClick) {
                reportAdAction("DailyAdsClickOver")
            }
            if (dayClickRate * 100 > AdConfigManager.mNormalAdBean.rewardVideo.limit.ClickEx
                    && !hasReportClickEx) {
                reportAdAction("DailyAdsClickOver1")
            }
        }
        // 单广告点击次数限制
        adStatus?.let {
            if (it.reqId.equals(currentAdRequestId, false)) {
                oneAdClickRepeatCount++
            } else {
                oneAdClickRepeatCount = 1
            }

            if (oneAdClickRepeatCount == AdConfigManager.mNormalAdBean.rewardVideo.limit.OneAdClickRepeat) {
                reportAdAction("AdClickRepeat")
            } else if (oneAdClickRepeatCount == AdConfigManager.mNormalAdBean.rewardVideo.limit.OneAdClickRepeatEx) {
                reportAdAction("AdClickRepeat1")
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

    fun loadAdStart() {
        loadAdCount++
        log(4)
    }

    fun onAdLoad() {
        loadAdSuccessCount++
        loadAdNoFillCount = 0
    }

    fun loadAdNoFill() {
        loadAdNoFillCount++
        if (loadAdNoFillCount == AdConfigManager.mNormalAdBean.rewardVideo.fail.fail) {
            reportAdAction("NoFill")
        }

        if (loadAdNoFillCount == AdConfigManager.mNormalAdBean.rewardVideo.fail.lxFail) {
            reportAdAction("MultiNoFill")
        }

        log(5)
    }

    fun loadAdClose() {
        oneAdClickRepeatCount = 0
        AdConfigManager.updateRewardId()
    }

    fun loadRewardVideoFail() {
        dayLoadFailedNumber++
        AdConfigManager.updateRewardId()
    }

    private fun log(type: Int) {
        val stringBuilder = StringBuilder()
                .apply {
                    when (type) {
                        1 -> {
                            append("\n  播放激励视频 {\n")
                        }
                        2 -> {
                            append("\n  点击激励视频 {\n")
                        }
                        3 -> {
                            append("\n  加载激励视频前打印信息 {\n")
                        }
                        4 -> {
                            append("\n  开始加载激励视频 { \n")
                        }
                        5 -> {
                            append("\n  加载激励视频NoFill { \n")
                        }
                    }
                    append("  当前激励视频id：${AdConfigManager.mRewardVideoId.reward_video_id}")
                    append("  当前总加载广告次数: $loadAdCount\n")
                    append("  当前总加载广告失败次数: $dayLoadFailedNumber")
                    append("  当前总加载广告连续NoFill次数: $loadAdNoFillCount")
                    append("  今日播放次数： $todayPlayNumber \n")
                    append("  当前点击率计算值（观看多少次开始计算点击率）： ${AdConfigManager.mNormalAdBean.rewardVideo.limit.ClickWatch} \n")
                    append("  当前点击率计算值（参与计算的播放次数）：$dayForClickPlayNumber \n")
                    append("  当前点击次数：$dayClickNumber \n")
                    append("  当前点击率： $dayClickRate \n")
                    append("  当前单个广告点击次数： $oneAdClickRepeatCount \n")
                    if (loadTimeQueue.getSize() > 0) {
                        append("  1小时开始计时时间1：${loadTimeQueue.getHead().toDataString()},\n")
                    }
                    if (loadTimeQueueEx.getSize() > 0) {
                        append("  1小时开始计时时间2：${loadTimeQueueEx.getHead().toDataString()},\n")
                    }
                    if (loadTimeQueue.getLast() - loadTimeQueue.getHead() <= HOUR_TIME) {
                        append("  1小时播放量1：${loadTimeQueue.getSize()},\n")
                    }
                    if (loadTimeQueueEx.getLast() - loadTimeQueueEx.getHead() <= HOUR_TIME) {
                        append("  1小时播放量2：${loadTimeQueueEx.getSize()},\n")
                    }
                    append("}")
                }
        AdLoggerUtils.d(stringBuilder.toString())
    }

    private fun reportAdAction(type: String) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("suuid", DeviceUtils.getMyUUID())
            jsonObject.put("user_id", AppInfo.getUserId())
            jsonObject.put("package_name", DeviceUtils.getPackage())
            jsonObject.put("channel", DeviceUtils.getChannelName())
            jsonObject.put("action", type)
            val jsonString = jsonObject.toString()

            val url = if (BuildConfig.HTTP_DEBUG) {
                "http://ecpm-customer.dev.tagtic.cn/api/v1/ad-action"
            } else {
                "http://ecpm-customer.xg.tagtic.cn/api/v1/ad-action"
            }
            AdLoggerUtils.d(" 上报广告行为开始:$jsonString")
            EasyHttp.post(url)
                    .upJson(jsonString)
                    .cacheMode(CacheMode.NO_CACHE)
                    .execute(object : SimpleCallBack<AdAction>() {
                        override fun onError(e: ApiException?) {
                            AdLoggerUtils.d("上报广告行为失败：${e.toString()}")
                        }

                        override fun onSuccess(t: AdAction?) {
                            AdLoggerUtils.d("上报广告行为成功：$t")
                            AdConfigManager.updateRewardId()
                            if (type.equals("DailyAdsClickOver", false)) {
                                hasReportClick = true
                            }
                            if (type.equals("DailyAdsClickOver1", false)) {
                                hasReportClickEx = true
                                t?.let {
                                    if (it.handle == 2 || it.handle == 3) {
                                        dayClickNumber = 0
                                        dayForClickPlayNumber = 0
                                        hasReportClick = false
                                        hasReportClickEx = false
                                    }
                                }
                            }
                            if (type.equals("HourAdsOver", false)) {
                                loadTimeQueue.reported()
                            } else if (type.equals("HourAdsOver1", false)/*
                                    || type.equals("DailyAdsOver1", false)*/) {
                                loadTimeQueue.removeAll()
                                loadTimeQueueEx.removeAll()
                            } else if (type.equals("MultiNoFill", false)) {
                                loadAdNoFillCount = 0
                            }
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 给客户端 上报ecpm */
    fun reportEcpm(code: Int, any: Any?) {
        // code为10 且object instanceof DnUnionBean 时才可获取对应的参数
        if (code == 10 && any is AdStatus) {
            if (!currentAdRequestId.equals(any.reqId, false)) {
                oneAdClickRepeatCount = 0
            }
            currentAdRequestId = any.reqId
            if (any.platFormType == "2" || any.platFormType == "3") {
                currentEcpm = any.currentEcpm
                val params = JSONObject()
                params.put("req_id", any.reqId)
                params.put("ecpm", currentEcpm)
                //广告类型,0 缺省,1激励视频
                params.put("type", 1)
                params.put("suuid", getSuuid())
                params.put("user_id", SPUtils.getInformain(KeySharePreferences.USER_ID, "0"))

                val jsonString = params.toString()

                AdLoggerUtils.d("上报激励视频ecpm:开始 $jsonString")
                val url = if (BuildConfig.HTTP_DEBUG) {
                    "http://ecpm-customer.dev.tagtic.cn/api/v2/ecpm/report"
                } else {
                    "http://ecpm-customer.xg.tagtic.cn/api/v2/ecpm/report"
                }
                EasyHttp.post(url)
                        .upJson(jsonString)
                        .cacheMode(CacheMode.NO_CACHE)
                        .execute(object : CallBack<String>() {
                            override fun onError(e: ApiException?) {
                                AdLoggerUtils.d("上报激励视频ecpm: 错误:$e")
                                AdConfigManager.updateRewardId()
                            }

                            override fun onSuccess(t: String?) {
                                AdLoggerUtils.d("上报激励视频ecpm: 成功:$t")
                                AdConfigManager.updateRewardId()
                            }

                            override fun onCompleted() {
                            }

                            override fun onStart() {
                            }

                            override fun onCompleteOk() {
                            }
                        })
            } else {
                AdLoggerUtils.d("上报激励视频ecpm:当前广告platFormType: ${any.platFormType}无法进行ecpm进行上报")
            }
        } else {
            AdLoggerUtils.d("上报激励视频ecpm:当前广告code: ${code}无法进行ecpm进行上报")
        }
    }

    fun reportEcpmWhenReward(activity: Activity?, listener: IAdRewardVideoListener?) {
        if (activity == null || activity.isFinishing) {
            return
        }

        val params = EcpmParam()
        params.setEcpm(currentEcpm)
        listener?.addReportEcpmParamsWhenReward(params)

        val url = if (BuildConfig.HTTP_DEBUG) {
            "http://dtsgame.dev.tagtic.cn/award/v1/ecpm"
        } else {
            "http://dtsgame.xg.tagtic.cn/award/v1/ecpm"
        }

        AdLoggerUtils.d("开始请求发放ecpm奖励：${params}")
        EasyHttp.post(url)
                .upJson(params.getParamsJson())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<EcpmResponse>() {
                    override fun onError(e: ApiException?) {
                        AdLoggerUtils.d("返回发放奖励的ecpm的值：失败")
                        activity.runOnUiThread {
                            listener?.reportEcpmFailWhenReward()
                        }
                    }

                    override fun onSuccess(t: EcpmResponse?) {
                        if (t == null) {
                            AdLoggerUtils.d("返回发放奖励的ecpm的值：EcpmResponse is null")
                            activity.runOnUiThread {
                                listener?.reportEcpmFailWhenReward()
                            }
                        } else {
			    AdLoggerUtils.d("返回发放奖励的ecpm的值：" + t.toString())
                            activity.runOnUiThread {
                                listener?.reportEcpmSuccessWhenReward(t)
                            }
                        }
                    }
                })
    }
}
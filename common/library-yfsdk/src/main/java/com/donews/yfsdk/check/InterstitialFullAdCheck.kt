package com.donews.yfsdk.monitor

import com.dn.sdk.AdCustomError
import com.donews.utilslibrary.utils.AppStatusUtils
import com.donews.yfsdk.manager.AdConfigManager
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

/**
 * 插屏广告统计监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/2 14:58
 */
object InterstitialFullAdCheck {

    private const val MMKV_NAME = "InterstitialFullAdCheck"
    private val mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE)
    private val mDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

    /** app 总的插屏显示数量 */
    private const val KEY_TOTAL_INTERSTITIAL_FULL_AD = "TotalInterstitialFullAd"

    /** app 今日显示插屏数量 */
    private const val KEY_TODAY_INTERSTITIAL_FULL_AD = "todayInterstitialFullAd"

    /** 最近一次显示插屏广告时间 */
    private const val KEY_NEW_INTERSTITIAL_FULL_AD_TIME = "newInterstitialFullAdTime"

    /** 最近一次广告的开始加载时间 */
    private const val KEY_NEW_START_INTERSTITIAL_FULL_AD_TIME = "newStartInterstitialFullAdTime"

    /** 最近一次广告关闭时间 */
    private const val KEY_NEW_CLOSE_INTERSTITIAL_FULL_AD_TIME = "newCloseInterstitialFullAdTime"

    private var mIsShow = false
    private var mIsStartLoad = false

    fun reset() {
        if (!isToday(System.currentTimeMillis())) {
            mIsShow = false
            mIsStartLoad = false
            mmkv?.let {
                it.removeValueForKey(KEY_NEW_INTERSTITIAL_FULL_AD_TIME)
                it.removeValueForKey(KEY_NEW_START_INTERSTITIAL_FULL_AD_TIME)
                it.removeValueForKey(KEY_NEW_CLOSE_INTERSTITIAL_FULL_AD_TIME)
            }
        }
    }

    @Synchronized
    fun isEnable(): AdCustomError {
        if (!AdConfigManager.mNormalAdBean.enable) {
            return AdCustomError.CloseAdAll
        }
        if (!AdConfigManager.mNormalAdBean.interstitialFull.enable) {
            return AdCustomError.CloseAdOne
        }

        //安装时间
        val installTime = AppStatusUtils.getAppInstallTime()
        val duration = AdConfigManager.mNormalAdBean.interstitialFull.startTime * 60 * 1000
        if ((System.currentTimeMillis() - installTime) < duration) {
            Logger.d("插全屏广告未超过设置开启时间")
            return AdCustomError.InterstitialOpenError
        }

        if (mIsShow || mIsStartLoad) {
            return AdCustomError.InterstitialHadShowError
        }

        mmkv?.let {
            val loadTime = it.decodeLong(KEY_NEW_START_INTERSTITIAL_FULL_AD_TIME, 0L)
            val showTime = it.decodeLong(KEY_NEW_INTERSTITIAL_FULL_AD_TIME, 0L)
            val closeTime = it.decodeLong(KEY_NEW_CLOSE_INTERSTITIAL_FULL_AD_TIME, 0L)

            val currentTime = System.currentTimeMillis()

            val closeDuration = currentTime - closeTime

            //证明上一个广告还未关闭,或者出现错误了
            if (loadTime > closeTime) {
                //显示时间大于加载时间，则广告正常展示，并且未关闭，则不需要继续显示下一个广告
                return if (showTime <= loadTime) {
                    AdCustomError.OK
                } else {
                    AdCustomError.InterstitialHadShowError
                }
            }
            if (closeDuration > AdConfigManager.mNormalAdBean.interstitialFull.interval * 1000L) {
                return AdCustomError.OK
            }
        }
        return AdCustomError.InterstitialIntervalError
    }

    fun onAdStartLoad() {
        mIsStartLoad = true
        mIsShow = false
    }

    fun onAdLoad() {
        mIsStartLoad = false
        mIsShow = false
    }

    fun onAdShow() {
        mIsStartLoad = false
        mIsShow = true
        mmkv?.let {
            var totalNumber = it.decodeInt(KEY_TOTAL_INTERSTITIAL_FULL_AD, 0)
            val newTime = it.decodeLong(KEY_NEW_INTERSTITIAL_FULL_AD_TIME, 0L)
            var today = it.decodeInt(KEY_TODAY_INTERSTITIAL_FULL_AD, 0)
            if (isToday(newTime)) {
                today += 1
            } else {
                today = 1
            }
            totalNumber += 1
            it.encode(KEY_NEW_INTERSTITIAL_FULL_AD_TIME, System.currentTimeMillis())
            it.encode(KEY_TODAY_INTERSTITIAL_FULL_AD, today)
            it.encode(KEY_TOTAL_INTERSTITIAL_FULL_AD, totalNumber)
        }
    }

    fun onAdClose() {
        mIsShow = false
        mIsStartLoad = false
        mmkv?.encode(KEY_NEW_CLOSE_INTERSTITIAL_FULL_AD_TIME, System.currentTimeMillis())
    }

    fun onAdError() {
        mIsShow = false
        mIsStartLoad = false
        mmkv?.encode(KEY_NEW_CLOSE_INTERSTITIAL_FULL_AD_TIME, System.currentTimeMillis())
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
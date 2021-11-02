package com.donews.common.ad.business.monitor

import com.donews.common.ad.business.bean.JddAdConfigBean
import com.donews.common.ad.business.callback.JddAdConfigManager
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
object InterstitialAdCount {

    private const val MMKV_NAME = "AdCount"
    private val mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE)
    private val mDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

    /** app 总的插屏显示数量 */
    private const val KEY_TOTAL_INTERSTITIAL_AD = "TotalInterstitialAd"

    /** app 今日显示插屏数量 */
    private const val KEY_TODAY_INTERSTITIAL_AD = "todayInterstitialAd"

    /** 最近一次显示插屏广告时间 */
    private const val KEY_NEW_INTERSTITIAL_AD_TIME = "newInterstitialAdTime"

    /** 最近一次广告的开始加载时间 */
    private const val KEY_NEW_START_INTERSTITIAL_AD_TIME = "newStartInterstitialAdTime"

    /** 最近一次广告关闭时间 */
    private const val KEY_NEW_CLOSE_INTERSTITIAL_AD_TIME = "newCloseInterstitialAdTime"

    fun showInterstitialAd() {
        mmkv?.let {
            var totalNumber = it.decodeInt(KEY_TOTAL_INTERSTITIAL_AD, 0)
            val newTime = it.decodeLong(KEY_NEW_INTERSTITIAL_AD_TIME, 0L)
            var today = it.decodeInt(KEY_TODAY_INTERSTITIAL_AD, 0)
            if (isToday(newTime)) {
                today += 1
            } else {
                today = 1
            }
            totalNumber += 1
            it.encode(KEY_NEW_INTERSTITIAL_AD_TIME, System.currentTimeMillis())
            it.encode(KEY_TODAY_INTERSTITIAL_AD, today)
            it.encode(KEY_TOTAL_INTERSTITIAL_AD, totalNumber)
        }
    }

    @Synchronized
    fun isCanShowInters(): Boolean {
        mmkv?.let {
            val loadTime = it.decodeLong(KEY_NEW_START_INTERSTITIAL_AD_TIME, 0L)
            val closeTime = it.decodeLong(KEY_NEW_CLOSE_INTERSTITIAL_AD_TIME, 0L)
            //证明上一个广告还未关闭
            if (loadTime > closeTime) {
                return false
            }
            val duration = JddAdConfigManager.jddAdConfigBean.interstitialDuration * 1000L
            if (System.currentTimeMillis() - closeTime > duration) {
                return true
            }
            return false
        }
        return false
    }

    fun updateLoadAdTime() {
        mmkv?.encode(KEY_NEW_START_INTERSTITIAL_AD_TIME, System.currentTimeMillis())
    }

    fun updateCloseAdTime() {
        mmkv?.encode(KEY_NEW_CLOSE_INTERSTITIAL_AD_TIME, System.currentTimeMillis())
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
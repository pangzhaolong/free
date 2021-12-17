package com.dn.sdk.listener.proxy

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.IAdInterstitialListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 插屏广告日志
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:42
 */
class LoggerInterstitialListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdInterstitialListener?
) : IAdInterstitialListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdStartLoad()"))
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdLoad()"))
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdShow()"))
        listener?.onAdShow()
    }

    override fun onAdExposure() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdExposure()"))
        listener?.onAdExposure()
        countTrack.onAdShow()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdClicked()"))
        listener?.onAdClicked()
        countTrack.onAdClick()
    }

    override fun onAdClosed() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdClosed()"))
        listener?.onAdClosed()
        countTrack.onAdClose()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }
}
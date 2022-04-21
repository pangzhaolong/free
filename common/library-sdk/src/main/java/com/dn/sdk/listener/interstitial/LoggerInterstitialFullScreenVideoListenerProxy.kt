package com.dn.sdk.listener.interstitial

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 插屏广告日志
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:42
 */
class LoggerInterstitialFullScreenVideoListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdInterstitialFullScreenListener?
) : IAdInterstitialFullScreenListener {
    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdLoad()"))
        listener?.onAdLoad()
    }

    override fun onAdCached() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdCached()"))
        listener?.onAdCached()
    }

    override fun onAdError(errorCode: Int, errprMsg: String) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdError($errorCode,$errprMsg)"))
        listener?.onAdError(errorCode, errprMsg)
    }

    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdShow()"))
        listener?.onAdShow()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdClicked()"))
        listener?.onAdClicked()
    }

    override fun onAdComplete() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdComplete()"))
        listener?.onAdComplete()
    }

    override fun onAdClose() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdClose()"))
        listener?.onAdClose()
    }

    override fun onSkippedVideo() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onSkippedVideo()"))
        listener?.onSkippedVideo()
    }

    override fun onRewardVerify(reward: Boolean) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onRewardVerify($reward)"))
        listener?.onRewardVerify(reward)
    }

    override fun onAdShowFail(errCode: Int, errMsg: String) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdShowFail($errCode,$errMsg)"))
        listener?.onAdShowFail(errCode, errMsg)
    }

    override fun onAdVideoError(errCode: Int, errMsg: String) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdVideoError($errCode,$errMsg)"))
        listener?.onAdVideoError(errCode, errMsg)
    }

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Interstitial full onAdStartLoad()"))
        listener?.onAdStartLoad()
    }
}
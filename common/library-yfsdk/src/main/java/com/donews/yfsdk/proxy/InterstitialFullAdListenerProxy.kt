package com.donews.yfsdk.proxy

import com.dn.sdk.listener.interstitial.IAdInterstitialFullScreenListener
import com.donews.yfsdk.monitor.InterstitialFullAdCheck

/**
 * 插屏广告代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/2 15:09
 */
class InterstitialFullAdListenerProxy(val listener: IAdInterstitialFullScreenListener? = null) : IAdInterstitialFullScreenListener {

    override fun onAdStartLoad() {
        listener?.onAdStartLoad()
        InterstitialFullAdCheck.onAdStartLoad()
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
        InterstitialFullAdCheck.onAdLoad()
    }

    override fun onAdCached() {
        listener?.onAdCached()
    }

    override fun onAdError(errorCode: Int, errprMsg: String) {
        listener?.onAdError(errorCode, errprMsg)
        InterstitialFullAdCheck.onAdError()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }

    override fun onAdShow() {
        listener?.onAdShow()
        InterstitialFullAdCheck.onAdShow()
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
    }

    override fun onAdComplete() {
    }

    override fun onAdClose() {
        listener?.onAdClose()
        InterstitialFullAdCheck.onAdClose()
    }

    override fun onSkippedVideo() {
    }

    override fun onRewardVerify(reward: Boolean) {
    }

    override fun onAdShowFail(errCode: Int, errMsg: String) {
    }

    override fun onAdVideoError(errCode: Int, errMsg: String) {
    }

}
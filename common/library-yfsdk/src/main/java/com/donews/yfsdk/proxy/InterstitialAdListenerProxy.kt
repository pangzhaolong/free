package com.donews.yfsdk.proxy

import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.donews.yfsdk.check.InterstitialAdCheck

/**
 * 插屏广告代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/2 15:09
 */
class InterstitialAdListenerProxy(val listener: IAdInterstitialListener? = null) : IAdInterstitialListener {

    override fun onAdStartLoad() {
        listener?.onAdStartLoad()
        InterstitialAdCheck.onAdStartLoad()
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
    }


    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }


    override fun onAdShow() {
        listener?.onAdShow()
        InterstitialAdCheck.onAdShow()
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
        InterstitialAdCheck.onAdClose()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
        InterstitialAdCheck.onAdError()
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
    }

}
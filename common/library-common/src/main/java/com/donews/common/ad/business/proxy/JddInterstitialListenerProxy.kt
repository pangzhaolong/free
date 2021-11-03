package com.donews.common.ad.business.proxy

import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener
import com.donews.common.ad.business.monitor.InterstitialAdCount

/**
 * 插屏广告代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/2 15:09
 */
class JddInterstitialListenerProxy(val listener: IAdInterstitialListener? = null) : IAdInterstitialListener {
    override fun onLoad() {
        listener?.onLoad()
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        InterstitialAdCount.updateCloseAdTime()
        InterstitialAdCount.closeAd()
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
        InterstitialAdCount.updateCloseAdTime()
        InterstitialAdCount.closeAd()
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        InterstitialAdCount.updateCloseAdTime()
        InterstitialAdCount.closeAd()
    }

    override fun onAdShow() {
        listener?.onAdShow()
        InterstitialAdCount.showInterstitialAd()
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
        InterstitialAdCount.updateCloseAdTime()
        InterstitialAdCount.closeAd()
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
    }

    override fun onAdShowFail(code: Int, msg: String?) {
        listener?.onAdShowFail(code, msg)
        InterstitialAdCount.updateCloseAdTime()
        InterstitialAdCount.closeAd()
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
    }

    override fun onAdOpened() {
        listener?.onAdOpened()
    }

    override fun onAdLeftApplication() {
        listener?.onAdLeftApplication()
    }
}
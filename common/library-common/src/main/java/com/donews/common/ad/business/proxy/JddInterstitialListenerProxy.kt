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
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }

    override fun onAdShow() {
        listener?.onAdShow()
        InterstitialAdCount.showInterstitialAd()
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
        InterstitialAdCount.updateCloseAdTime()
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
    }

    override fun onAdShowFail(code: Int, msg: String?) {
        listener?.onAdShowFail(code, msg)
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
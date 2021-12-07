package com.donews.common.ad.business.proxy

import com.dn.sdk.listener.IAdInterstitialListener
import com.donews.common.ad.business.monitor.InterstitialAdCount

/**
 * 插屏广告代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/2 15:09
 */
class JddInterstitialListenerProxy(val listener: IAdInterstitialListener? = null) : IAdInterstitialListener {
    override fun onAdLoad() {
        listener?.onAdLoad()
    }


    override fun onAdStatus(code: Int, any: Any?) {

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

    override fun onAdError(code: Int, errorMsg: String?) {
        InterstitialAdCount.updateCloseAdTime()
        InterstitialAdCount.closeAd()
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
    }

}
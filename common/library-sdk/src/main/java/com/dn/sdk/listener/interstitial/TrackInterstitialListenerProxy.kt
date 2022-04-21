package com.dn.sdk.listener.interstitial

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl

/**
 *  插屏广告埋点
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:41
 */
class TrackInterstitialListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdInterstitialListener?
) : IAdInterstitialListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        listener?.onAdShow()
    }

    override fun onAdExposure() {
        countTrack.onAdShow()
        listener?.onAdExposure()
    }

    override fun onAdClicked() {
        countTrack.onAdClick()
        listener?.onAdClicked()
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
        countTrack.onAdClose()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }
}
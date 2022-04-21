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
class TrackInterstitialFullScreenVideoListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdInterstitialFullScreenListener?
) : IAdInterstitialFullScreenListener {

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
        countTrack.onAdShow()
        listener?.onAdShow()
    }

    override fun onAdClicked() {
        countTrack.onAdClick()
        listener?.onAdClicked()
    }

    override fun onAdCached() {
        listener?.onAdClicked()
    }

    override fun onAdComplete() {
        listener?.onAdComplete()
    }

    override fun onSkippedVideo() {
        listener?.onSkippedVideo()
    }

    override fun onRewardVerify(reward: Boolean) {
        listener?.onRewardVerify(reward)
    }

    override fun onAdShowFail(errCode: Int, errMsg: String) {
        listener?.onAdShowFail(errCode, errMsg)
    }

    override fun onAdVideoError(errCode: Int, errMsg: String) {
        listener?.onAdVideoError(errCode, errMsg)
    }

    override fun onAdClose() {
        listener?.onAdClose()
        countTrack.onAdClose()
    }

    override fun onAdError(code: Int, errorMsg: String) {
        listener?.onAdError(code, errorMsg)
    }
}
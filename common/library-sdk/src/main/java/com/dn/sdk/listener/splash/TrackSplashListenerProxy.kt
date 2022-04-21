package com.dn.sdk.listener.splash

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.splash.IAdSplashListener

/**
 * 开屏广告埋点
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:53
 */
class TrackSplashListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdSplashListener?
) : IAdSplashListener {

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

    override fun onAdDismiss() {
        countTrack.onAdClose()
        listener?.onAdDismiss()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }
}
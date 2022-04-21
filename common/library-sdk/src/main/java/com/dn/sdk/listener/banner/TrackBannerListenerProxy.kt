package com.dn.sdk.listener.banner

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl

/**
 * Banner广告埋点事件上报
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:38
 */
class TrackBannerListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdBannerListener?
) : IAdBannerListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
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
        countTrack.onAdClose()
        listener?.onAdClosed()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }
}
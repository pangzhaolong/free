package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdBannerListener
import com.dn.sdk.sdk.statistics.CountTrackImpl

/**
 * BannerListener 代理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 11:01
 */
class AdBannerListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdBannerListener? = null
) : IAdBannerListener {

    private val countTrack = CountTrackImpl(requestInfo)

    override fun onLoad() {
        listener?.onLoad()
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        countTrack.onLoadError()
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
        countTrack.onLoadError()
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }

    override fun onAdShow() {
        listener?.onAdShow()
        countTrack.onShow()
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
        countTrack.onAdClose()
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
        countTrack.onClick()
    }

    override fun onAdShowFail(code: Int, msg: String?) {
        listener?.onAdShowFail(code, msg)
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
        countTrack.onADExposure()
    }

    override fun onAdOpened() {
        listener?.onAdOpened()
    }

    override fun onAdLeftApplication() {
        listener?.onAdLeftApplication()
    }
}
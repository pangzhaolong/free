package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener
import com.dn.sdk.sdk.statistics.CountTrackImpl

/**
 * 插屏广告代理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 11:07
 */
class AdInterstitialListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdInterstitialListener? = null
) : IAdInterstitialListener {

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
        listener?.onAdClosed()
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
package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener

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
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
    }

    override fun onAdClicked() {
        listener?.onAdClosed()
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
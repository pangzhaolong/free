package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener

/**
 * 代理监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 10:43
 */
class AdSplashListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdSplashListener? = null
) : IAdSplashListener {
    override fun onLoadFail(code: Int, error: String?) {
        listener?.onAdShowFail(code, error)
    }

    override fun onLoad() {
        listener?.onLoad()
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

    override fun onAdClicked() {
        listener?.onAdClicked()
    }

    override fun onAdShowFail(code: Int, error: String?) {
        listener?.onAdShowFail(code, error)
    }

    override fun onAdSkip() {
        listener?.onAdSkip()
    }

    override fun onAdDismiss() {
        listener?.onAdDismiss()
    }

    override fun onPresent() {
        listener?.onPresent()
    }

    override fun extendExtra(var1: String?) {
        listener?.extendExtra(var1)
    }
}
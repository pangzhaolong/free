package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener
import com.dn.sdk.sdk.statistics.CountTrackImpl

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

    private val countTrack = CountTrackImpl(requestInfo)

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
        countTrack.onLoadError()
    }

    override fun onAdShow() {
        listener?.onAdShow()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_GRO_MORE) {
            countTrack.onShow()
        }
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
        countTrack.onClick()
    }

    override fun onAdShowFail(code: Int, error: String?) {
        listener?.onAdShowFail(code, error)
    }

    override fun onAdSkip() {
        listener?.onAdSkip()
    }

    override fun onAdDismiss() {
        listener?.onAdDismiss()
        countTrack.onAdClose()
    }

    override fun onPresent() {
        listener?.onPresent()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_NEWS) {
            countTrack.onShow()
        }
    }

    override fun extendExtra(var1: String?) {
        listener?.extendExtra(var1)
    }
}
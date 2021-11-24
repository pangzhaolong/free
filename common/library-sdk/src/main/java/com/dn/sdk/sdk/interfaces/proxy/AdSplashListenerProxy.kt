package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener
import com.dn.sdk.sdk.statistics.CountTrackImpl
import com.dn.sdk.sdk.utils.AdLoggerUtils

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
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadFail($code,${error})"))

    }

    override fun onLoad() {
        listener?.onLoad()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoad"))
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadTimeout"))
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        countTrack.onLoadError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onError($code,$msg)"))
    }

    override fun onAdShow() {
        listener?.onAdShow()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_GRO_MORE) {
            countTrack.onShow()
        }
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdShow"))
    }

    override fun onAdClicked() {
        listener?.onAdClicked()
        countTrack.onClick()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdClicked"))
    }

    override fun onAdShowFail(code: Int, error: String?) {
        listener?.onAdShowFail(code, error)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdShowFail($code,$error)"))
    }

    override fun onAdSkip() {
        listener?.onAdSkip()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdSkip"))
    }

    override fun onAdDismiss() {
        listener?.onAdDismiss()
        countTrack.onAdClose()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdDismiss"))
    }

    override fun onPresent() {
        listener?.onPresent()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_NEWS) {
            countTrack.onShow()
        }
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onPresent"))
    }

    override fun extendExtra(var1: String?) {
        listener?.extendExtra(var1)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "extendExtra($var1)"))
    }
}
package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener
import com.dn.sdk.sdk.statistics.CountTrackImpl
import com.dn.sdk.sdk.utils.AdLoggerUtils

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
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoad"))
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        countTrack.onLoadError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadFail($code,$error)"))
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
        countTrack.onLoadError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadTimeout"))
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onError($code,$msg)"))
    }

    override fun onAdShow() {
        listener?.onAdShow()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_GRO_MORE) {
            countTrack.onShow()
        }
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdShow"))
    }

    override fun onAdClosed() {
        listener?.onAdClosed()
        countTrack.onAdClose()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdClosed"))
    }

    override fun onAdClicked() {
        listener?.onAdClosed()
        countTrack.onClick()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdClicked"))
    }

    override fun onAdShowFail(code: Int, msg: String?) {
        listener?.onAdShowFail(code, msg)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdShowFail($code,$msg)"))
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_NEWS) {
            countTrack.onShow()
        }
        countTrack.onADExposure()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdExposure"))
    }

    override fun onAdOpened() {
        listener?.onAdOpened()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdOpened"))
    }

    override fun onAdLeftApplication() {
        listener?.onAdLeftApplication()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdLeftApplication"))
    }
}
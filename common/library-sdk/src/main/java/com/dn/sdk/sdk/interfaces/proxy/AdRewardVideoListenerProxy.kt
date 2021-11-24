package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener
import com.dn.sdk.sdk.statistics.CountTrackImpl
import com.dn.sdk.sdk.utils.AdLoggerUtils

/**
 * 激励视频监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 11:12
 */
class AdRewardVideoListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdRewardVideoListener? = null
) : IAdRewardVideoListener {

    private val countTrack = CountTrackImpl(requestInfo)

    override fun onLoad() {
        listener?.onLoad()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoad"))
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadFail($code,$error)"))
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadTimeout"))
    }

    override fun onLoadCached() {
        listener?.onLoadCached()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadCached"))
    }

    override fun onRewardAdShow() {
        listener?.onRewardAdShow()
        countTrack.onShow()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardAdShow"))
    }

    override fun onRewardBarClick() {
        listener?.onRewardBarClick()
        countTrack.onClick()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardBarClick"))
    }

    override fun onRewardedClosed() {
        listener?.onRewardedClosed()
        countTrack.onAdClose()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardedClosed"))
    }

    override fun onRewardVideoComplete() {
        listener?.onRewardVideoComplete()
        countTrack.onVideoComplete()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardVideoComplete"))
    }

    override fun onRewardVideoError() {
        listener?.onRewardVideoError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardVideoError"))
    }

    override fun onRewardVideoAdShowFail(code: Int, message: String?) {
        listener?.onRewardVideoAdShowFail(code, message)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardVideoAdShowFail($code,$message)"))
    }

    override fun onRewardVerify(result: Boolean) {
        listener?.onRewardVerify(result)
        countTrack.onRewardVerify(result)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRewardVerify($result)"))
    }

    override fun onSkippedRewardVideo() {
        listener?.onSkippedRewardVideo()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onSkippedRewardVideo"))
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        countTrack.onLoadError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onError($code,$msg)"))
    }
}
package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener
import com.dn.sdk.sdk.statistics.CountTrackImpl
import com.dn.sdk.sdk.utils.AdLoggerUtils

/**
 * 全屏视频监听器代理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 11:17
 */
class AdFullVideoListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdFullVideoListener? = null
) : IAdFullVideoListener {

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

    override fun onLoadCached() {
        listener?.onLoadCached()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadCached"))
    }

    override fun onFullVideoAdShow() {
        listener?.onFullVideoAdShow()
        countTrack.onShow()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onFullVideoAdShow"))
    }

    override fun onFullVideoClick() {
        listener?.onFullVideoClick()
        countTrack.onClick()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onFullVideoClick"))
    }

    override fun onFullVideoClosed() {
        listener?.onFullVideoClosed()
        countTrack.onAdClose()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onFullVideoClosed"))
    }

    override fun onFullVideoComplete() {
        listener?.onFullVideoComplete()
        countTrack.onVideoComplete()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onFullVideoComplete"))
    }

    override fun onFullVideoError() {
        listener?.onFullVideoError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onFullVideoError"))
    }

    override fun onFullVideoAdShowFail(code: Int, message: String?) {
        listener?.onFullVideoAdShowFail(code, message)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onFullVideoAdShowFail($code,$message)"))
    }

    override fun onSkippedFullVideo() {
        listener?.onSkippedFullVideo()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onSkippedFullVideo"))
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onError($code,$msg)"))
    }
}
package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener
import com.dn.sdk.sdk.statistics.CountTrackImpl

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
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        countTrack.onLoadError()
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
        countTrack.onLoadError()
    }

    override fun onLoadCached() {
        listener?.onLoadCached()
    }

    override fun onFullVideoAdShow() {
        listener?.onFullVideoAdShow()
        countTrack.onShow()
    }

    override fun onFullVideoClick() {
        listener?.onFullVideoClick()
        countTrack.onClick()
    }

    override fun onFullVideoClosed() {
        listener?.onFullVideoClosed()
        countTrack.onAdClose()
    }

    override fun onFullVideoComplete() {
        listener?.onFullVideoComplete()
        countTrack.onVideoComplete()
    }

    override fun onFullVideoError() {
        listener?.onFullVideoError()
    }

    override fun onFullVideoAdShowFail(code: Int, message: String?) {
        listener?.onFullVideoAdShowFail(code, message)
    }

    override fun onSkippedFullVideo() {
        listener?.onSkippedFullVideo()
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }
}
package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener

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
    override fun onLoad() {
        listener?.onLoad()
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
    }

    override fun onLoadTimeout() {
        listener?.onLoadTimeout()
    }

    override fun onLoadCached() {
        listener?.onLoadCached()
    }

    override fun onFullVideoAdShow() {
        listener?.onFullVideoAdShow()
    }

    override fun onFullVideoClick() {
        listener?.onFullVideoClick()
    }

    override fun onFullVideoClosed() {
        listener?.onFullVideoClosed()
    }

    override fun onFullVideoComplete() {
        listener?.onFullVideoComplete()
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
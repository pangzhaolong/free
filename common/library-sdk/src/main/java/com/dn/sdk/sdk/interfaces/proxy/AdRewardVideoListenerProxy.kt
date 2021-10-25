package com.dn.sdk.sdk.interfaces.proxy

import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener

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

    override fun onRewardAdShow() {
        listener?.onRewardAdShow()
    }

    override fun onRewardBarClick() {
        listener?.onRewardBarClick()
    }

    override fun onRewardedClosed() {
        listener?.onRewardedClosed()
    }

    override fun onRewardVideoComplete() {
        listener?.onRewardVideoComplete()
    }

    override fun onRewardVideoError() {
        listener?.onRewardVideoError()
    }

    override fun onRewardVideoAdShowFail(code: Int, message: String?) {
        listener?.onRewardVideoAdShowFail(code, message)
    }

    override fun onRewardVerify(result: Boolean) {
        listener?.onRewardVerify(result)
    }

    override fun onSkippedRewardVideo() {
        listener?.onSkippedRewardVideo()
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }
}
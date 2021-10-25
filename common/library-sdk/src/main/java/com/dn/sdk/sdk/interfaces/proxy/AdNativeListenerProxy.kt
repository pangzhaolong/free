package com.dn.sdk.sdk.interfaces.proxy

import android.view.View
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.listener.IAdNativeListener

/**
 * 自渲染信息流 监听器代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 11:26
 */
class AdNativeListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdNativeListener? = null
) : IAdNativeListener {
    override fun onLoad(views: MutableList<View>?) {
        listener?.onLoad(views)
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
    }

    override fun onSelected(i: Int, s: String?) {
        listener?.onSelected(i, s)
    }

    override fun onCancel() {
        listener?.onCancel()
    }

    override fun onRefuse() {
        listener?.onRefuse()
    }

    override fun onShow() {
        listener?.onShow()
    }

    override fun onVideoStart() {
        listener?.onVideoStart()
    }

    override fun onVideoPause() {
        listener?.onVideoPause()
    }

    override fun onVideoResume() {
        listener?.onVideoResume()
    }

    override fun onVideoCompleted() {
        listener?.onVideoCompleted()
    }

    override fun onVideoError(code: Int, error: String?) {
        listener?.onVideoError(code, error)
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }

    override fun onAdClick() {
        listener?.onAdClick()
    }

    override fun onAdShow() {
        listener?.onAdShow()
    }

    override fun onAdClose() {
        listener?.onAdClose()
    }
}
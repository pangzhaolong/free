package com.dn.sdk.sdk.interfaces.proxy

import android.view.View
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.interfaces.listener.IAdNativeListener
import com.dn.sdk.sdk.statistics.CountTrackImpl

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

    private val countTrack = CountTrackImpl(requestInfo)

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
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_GRO_MORE) {
            countTrack.onShow()
        }
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
        countTrack.onVideoComplete()
    }

    override fun onVideoError(code: Int, error: String?) {
        listener?.onVideoError(code, error)
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        countTrack.onLoadError()
    }

    override fun onAdClick() {
        listener?.onAdClick()
        countTrack.onClick()
    }

    override fun onAdShow() {
        listener?.onAdShow()
        countTrack.onShow()
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_NEWS) {
            countTrack.onShow()
        }
        countTrack.onADExposure()
    }

    override fun onAdClose() {
        listener?.onAdClose()
        countTrack.onAdClose()
    }
}
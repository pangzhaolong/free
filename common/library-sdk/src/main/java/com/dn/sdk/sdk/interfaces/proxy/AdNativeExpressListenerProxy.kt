package com.dn.sdk.sdk.interfaces.proxy

import android.view.View
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener
import com.dn.sdk.sdk.statistics.CountTrackImpl

/**
 * 模板信息流 监听器代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 11:20
 */
class AdNativeExpressListenerProxy(
    private val requestInfo: RequestInfo,
    private val listener: IAdNativeExpressListener? = null
) : IAdNativeExpressListener {

    private val countTrack = CountTrackImpl(requestInfo)

    override fun onLoad(views: MutableList<View>?) {
        listener?.onLoad(views)
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        countTrack.onLoadError()
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
        countTrack.onVideoComplete()
    }

    override fun onVideoError(code: Int, error: String?) {
        listener?.onVideoError(code, error)
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
    }

    override fun onAdClick() {
        listener?.onAdClick()
        countTrack.onClick()
    }

    override fun onAdShow() {
        listener?.onAdShow()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_GRO_MORE) {
            countTrack.onShow()
        }
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_NEWS) {
            countTrack.onShow()
        }
    }

    override fun onAdClose() {
        listener?.onAdClose()
        countTrack.onAdClose()
    }

    override fun onRenderFail(view: View?, msg: String?, code: Int) {
        listener?.onRenderFail(view, msg, code)
    }

    override fun onRenderSuccess(width: Float, height: Float) {
        listener?.onRenderSuccess(width, height)
    }
}
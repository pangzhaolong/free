package com.dn.sdk.sdk.interfaces.proxy

import android.view.View
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.bean.SDKType
import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener
import com.dn.sdk.sdk.statistics.CountTrackImpl
import com.dn.sdk.sdk.utils.AdLoggerUtils

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
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoad"))
    }

    override fun onLoadFail(code: Int, error: String?) {
        listener?.onLoadFail(code, error)
        countTrack.onLoadError()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onLoadFail($code,$error)"))
    }

    override fun onSelected(i: Int, s: String?) {
        listener?.onSelected(i, s)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onSelected($i,$s)"))
    }

    override fun onCancel() {
        listener?.onCancel()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onCancel"))
    }

    override fun onRefuse() {
        listener?.onRefuse()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRefuse"))
    }

    override fun onShow() {
        listener?.onShow()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onShow"))
    }

    override fun onVideoStart() {
        listener?.onVideoStart()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onVideoStart"))
    }

    override fun onVideoPause() {
        listener?.onVideoPause()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onVideoPause"))
    }

    override fun onVideoResume() {
        listener?.onVideoResume()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onVideoResume"))
    }

    override fun onVideoCompleted() {
        listener?.onVideoCompleted()
        countTrack.onVideoComplete()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onVideoCompleted"))
    }

    override fun onVideoError(code: Int, error: String?) {
        listener?.onVideoError(code, error)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onVideoError($code,$error)"))
    }

    override fun onError(code: Int, msg: String?) {
        listener?.onError(code, msg)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onError($code,$msg)"))
    }

    override fun onAdClick() {
        listener?.onAdClick()
        countTrack.onClick()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdClick"))
    }

    override fun onAdShow() {
        listener?.onAdShow()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_GRO_MORE) {
            countTrack.onShow()
        }
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdShow"))
    }

    override fun onAdExposure() {
        listener?.onAdExposure()
        if (requestInfo.platform.getLoader().sdkType == SDKType.DO_NEWS) {
            countTrack.onShow()
        }
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdExposure"))
    }

    override fun onAdClose() {
        listener?.onAdClose()
        countTrack.onAdClose()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onAdClose"))
    }

    override fun onRenderFail(view: View?, msg: String?, code: Int) {
        listener?.onRenderFail(view, msg, code)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRenderFail($code,$msg)"))
    }

    override fun onRenderSuccess(width: Float, height: Float) {
        listener?.onRenderSuccess(width, height)
        AdLoggerUtils.d(AdLoggerUtils.createMsg(requestInfo, "onRenderSuccess($width,$height)"))
    }
}
package com.dn.sdk.listener.proxy

import android.view.View
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.IAdNativeTemplateListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 信息流模板日志打印
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:49
 */
class LoggerNativeTemplateListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdNativeTemplateListener?
) : IAdNativeTemplateListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdStartLoad()"))
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad(views: MutableList<View>) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdLoad($views)"))
        listener?.onAdLoad(views)
    }

    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdShow()"))
        listener?.onAdShow()
    }

    override fun onAdExposure() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdExposure()"))
        listener?.onAdExposure()
        countTrack.onAdShow()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdClicked()"))
        listener?.onAdClicked()
        countTrack.onAdClick()
    }

    override fun onAdClose() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdClose()"))
        listener?.onAdClose()
        countTrack.onAdClose()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeTemplate onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }
}
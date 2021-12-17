package com.dn.sdk.listener.proxy

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.IAdNativeListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 源生信息流状态监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:52
 */
class LoggerNativeListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdNativeListener?
) : IAdNativeListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdExposure() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdExposure()"))
        listener?.onAdExposure()
        countTrack.onAdShow()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdClicked()"))
        listener?.onAdClicked()
        countTrack.onAdClick()
    }

    override fun onAdError(errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdError($errorMsg)"))
        listener?.onAdError(errorMsg)
    }
}
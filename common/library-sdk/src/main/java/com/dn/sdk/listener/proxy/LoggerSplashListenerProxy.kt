package com.dn.sdk.listener.proxy

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.IAdSplashListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 日志打印监听代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:34
 */
class LoggerSplashListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdSplashListener?
) : IAdSplashListener {

    override fun onAdStartLoad() {
        super.onAdStartLoad()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdStartLoad()"))
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdLoad()"))
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdShow()"))
        listener?.onAdShow()
    }

    override fun onAdExposure() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdExposure()"))
        listener?.onAdExposure()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdClicked()"))
        listener?.onAdClicked()
    }

    override fun onAdDismiss() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdDismiss()"))
        listener?.onAdDismiss()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Splash onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }
}
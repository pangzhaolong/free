package com.dn.sdk.listener.proxy

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.IAdBannerListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 日志Banner
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:30
 */
class LoggerBannerListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdBannerListener?
) : IAdBannerListener {

    override fun onAdStartLoad() {
        super.onAdStartLoad()
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdStartLoad()"))
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdShow()"))
        listener?.onAdShow()
    }

    override fun onAdExposure() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdExposure()"))
        listener?.onAdExposure()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdClicked()"))
        listener?.onAdClicked()
    }

    override fun onAdClosed() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdClosed()"))
        listener?.onAdClosed()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Banner onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }
}
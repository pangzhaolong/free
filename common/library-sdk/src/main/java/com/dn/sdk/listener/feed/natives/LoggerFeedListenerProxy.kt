package com.dn.sdk.listener.feed.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.feed.natives.IAdFeedListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 源生信息流状态监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:52
 */
class LoggerFeedListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdFeedListener?
) : IAdFeedListener {


    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdExposure() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdExposure()"))
        listener?.onAdExposure()
    }

    override fun onAdClicked() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdClicked()"))
        listener?.onAdClicked()
    }

    override fun onAdError(errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "Native onAdError($errorMsg)"))
        listener?.onAdError(errorMsg)
    }
}
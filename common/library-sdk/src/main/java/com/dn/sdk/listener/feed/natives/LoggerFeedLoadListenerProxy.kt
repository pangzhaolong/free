package com.dn.sdk.listener.feed.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.INativeAdData
import com.dn.sdk.listener.feed.natives.IAdFeedLoadListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 *
 * 源生信息流日志代理
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:54
 */
class LoggerFeedLoadListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdFeedLoadListener?
) : IAdFeedLoadListener {

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeLoad onAdStartLoad()"))
        listener?.onAdStartLoad()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeLoad onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }

    override fun onAdLoad(list: List<INativeAdData>) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeLoad onAdLoad($list)"))
        listener?.onAdLoad(list)
    }
}
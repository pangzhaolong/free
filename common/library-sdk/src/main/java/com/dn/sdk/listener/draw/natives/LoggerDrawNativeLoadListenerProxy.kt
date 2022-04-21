package com.dn.sdk.listener.draw.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTDrawFeedAdData
import com.dn.sdk.utils.AdLoggerUtils

/**
 * Draw流加载代理监听器
 *
 *  make in st
 *  on 2021/12/27 18:20
 */
class LoggerDrawNativeLoadListenerProxy(
    private val adRequest: AdRequest,
    private val listenerNative: IAdDrawNativeLoadListener?
) : IAdDrawNativeLoadListener {

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onAdError($code,$errorMsg)"))
        listenerNative?.onAdError(code, errorMsg)
    }

    override fun onAdLoad(list: List<ITTDrawFeedAdData>) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onAdLoad($list)"))
        listenerNative?.onAdLoad(list)
    }

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onAdStartLoad()"))
        listenerNative?.onAdStartLoad()
    }

}
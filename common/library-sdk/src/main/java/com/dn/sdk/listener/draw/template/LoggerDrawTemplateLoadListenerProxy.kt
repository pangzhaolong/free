package com.dn.sdk.listener.draw.template

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTNativeExpressAdData
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 模板渲染Draw相关的监听器回调
 *
 *  make in st
 *  on 2021/12/27 18:20
 */
class LoggerDrawTemplateLoadListenerProxy(
    private val adRequest: AdRequest,
    private val listenerTemplate: IAdDrawTemplateLoadListener?
) : IAdDrawTemplateLoadListener {


    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeDraw onAdStartLoad()"))
        listenerTemplate?.onAdStartLoad()
    }

    override fun onAdLoad(list: List<ITTNativeExpressAdData>) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeDraw onAdLoad($list)"))
        listenerTemplate?.onAdLoad(list)
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "NativeDraw onAdError($code,$errorMsg)"))
        listenerTemplate?.onAdError(code, errorMsg)
    }
}
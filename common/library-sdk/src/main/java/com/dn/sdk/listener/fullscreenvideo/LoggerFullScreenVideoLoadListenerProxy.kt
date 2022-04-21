package com.dn.sdk.listener.fullscreenvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.utils.AdLoggerUtils

/**
 *  make in st
 *  on 2021/12/27 18:20
 */
class LoggerFullScreenVideoLoadListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdFullScreenVideoLoadListener?
) : IAdFullScreenVideoLoadListener {
    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }

    override fun onAdLoad(ad: ITTFullScreenVideoAdData) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onAdLoad($ad)"))
        listener?.onAdLoad(ad)
    }

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onAdStartLoad()"))
        listener?.onAdStartLoad()
    }
}
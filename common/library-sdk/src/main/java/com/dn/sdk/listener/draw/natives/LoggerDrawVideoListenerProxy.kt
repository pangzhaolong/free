package com.dn.sdk.listener.draw.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTDrawFeedAdData
import com.dn.sdk.utils.AdLoggerUtils

/**
 *  Draw视频监听代理日志
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/25 16:08
 */
class LoggerDrawVideoListenerProxy(
    val adRequest: AdRequest,
    val listener: ITTDrawFeedAdData.DrawVideoListener?
) : ITTDrawFeedAdData.DrawVideoListener {
    override fun onClickRetry() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onClickRetry()"))
        listener?.onClickRetry()
    }

    override fun onClick() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd onClick()"))
        listener?.onClick()
    }
}
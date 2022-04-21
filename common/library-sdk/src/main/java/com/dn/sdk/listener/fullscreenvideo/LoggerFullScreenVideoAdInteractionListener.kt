package com.dn.sdk.listener.fullscreenvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 全屏视频广告操作日志代理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/3/2 11:47
 */
class LoggerFullScreenVideoAdInteractionListener(
    private val adRequest: AdRequest,
    private val listener: ITTFullScreenVideoAdData.FullScreenVideoAdInteractionListener?
) : ITTFullScreenVideoAdData.FullScreenVideoAdInteractionListener {
    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onAdShow()"))
        listener?.onAdShow()
    }

    override fun onAdVideoBarClick() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onAdVideoBarClick()"))
        listener?.onAdVideoBarClick()
    }

    override fun onAdClose() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onAdClose()"))
        listener?.onAdClose()
    }

    override fun onVideoComplete() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onVideoComplete()"))
        listener?.onVideoComplete()
    }

    override fun onSkippedVideo() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "FullScreenVideo onSkippedVideo()"))
        listener?.onSkippedVideo()
    }
}
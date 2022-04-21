package com.dn.sdk.listener.draw.natives

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFeedAd
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 视频相关控制日志代理监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/25 16:27
 */
class LoggerCustomizeVideoListenerProxy(
    private val adRequest: AdRequest,
    private val customizeVideo: ITTFeedAd.CustomizeVideo?
) : ITTFeedAd.CustomizeVideo {
    override val videoUrl: String?
        get() = customizeVideo?.videoUrl

    override fun reportVideoStart() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd CustomizeVideo reportVideoStart()"))
        customizeVideo?.reportVideoStart()
    }

    override fun reportVideoPause(var1: Long) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd CustomizeVideo reportVideoPause(${var1})"))
        customizeVideo?.reportVideoPause(var1)
    }

    override fun reportVideoContinue(var1: Long) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd CustomizeVideo reportVideoContinue(${var1})"))
        customizeVideo?.reportVideoContinue(var1)
    }

    override fun reportVideoFinish() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd CustomizeVideo reportVideoFinish()"))
        customizeVideo?.reportVideoFinish()
    }

    override fun reportVideoBreak(var1: Long) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd CustomizeVideo reportVideoBreak(${var1})"))
        customizeVideo?.reportVideoBreak(var1)
    }

    override fun reportVideoAutoStart() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "DrawFeedAd CustomizeVideo reportVideoAutoStart()"))
        customizeVideo?.reportVideoAutoStart()
    }

    override fun reportVideoStartError(var1: Int, var2: Int) {
        AdLoggerUtils.d(
            AdLoggerUtils.createMsg(
                adRequest,
                "DrawFeedAd CustomizeVideo reportVideoStartError($var1,$var2)"
            )
        )
        customizeVideo?.reportVideoStartError(var1, var2)
    }

    override fun reportVideoError(var1: Long, var3: Int, var4: Int) {
        AdLoggerUtils.d(
            AdLoggerUtils.createMsg(
                adRequest,
                "DrawFeedAd CustomizeVideo reportVideoError($var1,$var3,$var4)"
            )
        )
        customizeVideo?.reportVideoError(var1, var3, var4)
    }
}
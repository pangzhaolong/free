package com.dn.sdk.listener.draw.natives

import com.bytedance.sdk.openadsdk.TTFeedAd
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFeedAd
import com.dn.sdk.utils.AdLoggerUtils

/**
 * draw流视频广告对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/25 15:25
 */
class LoggerVideoAdListenerProxy(
    private val adRequest: AdRequest,
    private val listener: ITTFeedAd.VideoAdListener?
) :
    ITTFeedAd.VideoAdListener {
    override fun onVideoLoad(var1: TTFeedAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onVideoLoad()"))
        listener?.onVideoLoad(var1)
    }

    override fun onVideoError(var1: Int, var2: Int) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onVideoError($var1,$var2)"))
        listener?.onVideoError(var1, var2)
    }

    override fun onVideoAdStartPlay(var1: TTFeedAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onVideoAdStartPlay()"))
        listener?.onVideoAdStartPlay(var1)
    }

    override fun onVideoAdPaused(var1: TTFeedAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onVideoAdPaused()"))
        listener?.onVideoAdPaused(var1)
    }

    override fun onVideoAdContinuePlay(var1: TTFeedAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onVideoAdContinuePlay()"))
        listener?.onVideoAdContinuePlay(var1)
    }

    override fun onProgressUpdate(var1: Long, var3: Long) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onProgressUpdate($var1,$var3)"))
        listener?.onProgressUpdate(var1, var3)
    }


    override fun onVideoAdComplete(var1: TTFeedAd?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest,"DrawFeedAd  onVideoAdComplete()"))
        listener?.onVideoAdComplete(var1)
    }
}
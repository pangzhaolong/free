package com.dn.sdk.platform.csj.bean

import android.app.Activity
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.fullscreenvideo.LoggerFullScreenVideoAdInteractionListener
import com.dn.sdk.listener.fullscreenvideo.TrackFullScreenVideoAdInteractionListener

/**
 * 穿山甲全屏视频广告对象
 *
 *  make in st
 *  on 2021/12/28 09:30
 */
class CsjFullScreenVideoAd(
    private val adRequest: AdRequest,
    private val countTrackImpl: CountTrackImpl,
    private val nativeData: TTFullScreenVideoAd
) : ITTFullScreenVideoAdData {
    override fun setFullScreenVideoAdInteractionListener(var1: ITTFullScreenVideoAdData.FullScreenVideoAdInteractionListener?) {

        val proxyListener = TrackFullScreenVideoAdInteractionListener(
            adRequest,
            countTrackImpl,
            LoggerFullScreenVideoAdInteractionListener(adRequest, var1)
        )

        val listener = object : TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
            override fun onAdShow() {
                proxyListener.onAdShow()
            }

            override fun onAdVideoBarClick() {
                proxyListener.onAdVideoBarClick()
            }

            override fun onAdClose() {
                proxyListener.onAdClose()
            }

            override fun onVideoComplete() {
                proxyListener.onVideoComplete()
            }

            override fun onSkippedVideo() {
                proxyListener.onSkippedVideo()
            }
        }
        nativeData.setFullScreenVideoAdInteractionListener(listener)
    }

    override fun setDownloadListener(var1: TTAppDownloadListener?) {
        nativeData.setDownloadListener(var1)
    }

    override fun getInteractionType(): Int {
        return nativeData.interactionType
    }

    override fun showFullScreenVideoAd(var1: Activity?) {
        nativeData.showFullScreenVideoAd(var1)
    }

    override fun showFullScreenVideoAd(
        var1: Activity?,
        var2: TTAdConstant.RitScenes?,
        var3: String?
    ) {
        nativeData.showFullScreenVideoAd(var1, var2, var3)
    }

    override fun setShowDownLoadBar(var1: Boolean) {
        nativeData.setShowDownLoadBar(var1)
    }

    override fun getMediaExtraInfo(): Map<String?, Any?>? {
        return nativeData.mediaExtraInfo
    }

    override fun getFullVideoAdType(): Int {
        return nativeData.fullVideoAdType
    }

    override fun getExpirationTimestamp(): Long {
        return nativeData.expirationTimestamp
    }

}
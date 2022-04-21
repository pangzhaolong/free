package com.dn.sdk.platform.gromore.bean

import android.app.Activity
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.reward.RewardItem
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAd
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAdListener
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTFullScreenVideoAdData
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.fullscreenvideo.LoggerFullScreenVideoAdInteractionListener
import com.dn.sdk.listener.fullscreenvideo.TrackFullScreenVideoAdInteractionListener

/**
 * 穿山甲(GroMore)全屏视频广告对象
 *
 *  make in st
 *  on 2021/12/28 09:30
 */
class GroMoreFullScreenVideoAd(
    private val adRequest: AdRequest,
    private val countTrackImpl: CountTrackImpl,
    private val nativeData: GMFullVideoAd
) : ITTFullScreenVideoAdData {
    override fun setFullScreenVideoAdInteractionListener(var1: ITTFullScreenVideoAdData.FullScreenVideoAdInteractionListener?) {

        val proxyListener = TrackFullScreenVideoAdInteractionListener(
            adRequest,
            countTrackImpl,
            LoggerFullScreenVideoAdInteractionListener(adRequest, var1)
        )

        val listener = object : GMFullVideoAdListener {
            override fun onFullVideoAdShow() {
                proxyListener.onAdShow()
            }

            override fun onFullVideoAdShowFail(p0: AdError) {
            }

            override fun onFullVideoAdClick() {
            }

            override fun onFullVideoAdClosed() {
                proxyListener.onAdClose()
            }


            override fun onVideoComplete() {
                proxyListener.onVideoComplete()
            }

            override fun onVideoError() {
            }

            override fun onSkippedVideo() {
                proxyListener.onSkippedVideo()
            }

            override fun onRewardVerify(p0: RewardItem) {

            }
        }
        nativeData.setFullVideoAdListener(listener)
    }

    override fun setDownloadListener(var1: TTAppDownloadListener?) {
        throw NotImplementedError("当前对象未实现此方法,请实现后在使用,code = -1")
    }

    override fun getInteractionType(): Int {
        throw NotImplementedError("当前对象未实现此方法,请实现后在使用,code = -1")
    }

    override fun showFullScreenVideoAd(var1: Activity?) {
        nativeData.showFullAd(var1)
    }

    override fun showFullScreenVideoAd(
        var1: Activity?,
        var2: TTAdConstant.RitScenes?,
        var3: String?
    ) {
        nativeData.showFullAd(var1)
    }

    override fun setShowDownLoadBar(var1: Boolean) {
        throw NotImplementedError("当前对象未实现此方法,请实现后在使用,code = -1")
    }

    override fun getMediaExtraInfo(): Map<String?, Any?>? {
        return mutableMapOf()
    }

    override fun getFullVideoAdType(): Int {
        throw NotImplementedError("当前对象未实现此方法,请实现后在使用,code = -1")
    }

    override fun getExpirationTimestamp(): Long {
        throw NotImplementedError("当前对象未实现此方法,请实现后在使用,code = -1")
    }
}
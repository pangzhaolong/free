package com.dn.sdk.platform.gromore.helper

import android.app.Activity
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.reward.RewardItem
import com.bytedance.msdk.api.v2.GMAdConstant
import com.bytedance.msdk.api.v2.ad.reward.GMRewardAd
import com.bytedance.msdk.api.v2.ad.reward.GMRewardedAdListener
import com.bytedance.msdk.api.v2.ad.reward.GMRewardedAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil
import com.bytedance.msdk.api.v2.slot.GMAdSlotRewardVideo
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.listener.rewardvideo.LoggerRewardVideoListenerProxy
import com.dn.sdk.listener.rewardvideo.TrackRewardVideoListenerProxy
import com.dn.sdk.platform.BaseHelper
import com.donews.ads.mediation.v2.framework.bean.DnUnionBean


/**
 * GroMore 激励视频加载展示
 *
 * @author dw
 * @version v1.0
 * @date 2022/5/16
 */
object GroMoreRewardedAdHelper : BaseHelper() {

    /** 加载和显示广告 */
    var gmRewardAd: GMRewardAd? = null
    var loggerListener: LoggerRewardVideoListenerProxy? = null
    var trackListener: TrackRewardVideoListenerProxy? = null
    fun loadRewardedAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?) {
        if (trackListener == null) {
            trackListener = TrackRewardVideoListenerProxy(adRequest, listener)
        }
        if (loggerListener == null) {
            loggerListener = LoggerRewardVideoListenerProxy(adRequest, trackListener)
        }

        trackListener?.adRequest = adRequest
        trackListener?.listener = listener
        loggerListener?.adRequest = adRequest
        loggerListener?.listener = trackListener

        loggerListener?.onAdStartLoad()
        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                loggerListener?.onAdError(
                        AdCustomError.ParamsAdIdNullOrBlank.code,
                        AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                )
            }
            return
        }

        gmRewardAd = GMRewardAd(activity, adRequest.mAdId)

        val adSlotRewardVideo = GMAdSlotRewardVideo.Builder()
                .setMuted(true) //对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setVolume(0f) //配合Admob的声音大小设置[0-1]
                .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build())
                .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())
                .setRewardName("") //奖励的名称
                .setRewardAmount(0) //奖励的数量
                .setUserID(adRequest.mUserId) //用户id,必传参数
                .setOrientation(GMAdConstant.VERTICAL) //必填参数，期望视频的播放方向：GMAdConstant.HORIZONTAL 或 GMAdConstant.VERTICAL
                .build()

        gmRewardAd?.loadAd(adSlotRewardVideo, object : GMRewardedAdLoadCallback {
            override fun onRewardVideoLoadFail(p0: AdError) {
                loggerListener?.onAdError(p0.code, p0.message)
                gmRewardAd?.destroy()
            }

            override fun onRewardVideoAdLoad() {
                loggerListener?.onAdLoad()
            }

            override fun onRewardVideoCached() {
                loggerListener?.onVideoCached()
            }
        })
    }

    fun isAdReady(): Boolean {
        return gmRewardAd != null && gmRewardAd?.isReady == true
    }

    fun showRewardedAd(activity: Activity, listener: IAdRewardVideoListener?) {
        gmRewardAd?.setRewardAdListener(object : GMRewardedAdListener {
            override fun onRewardedAdShow() {
                loggerListener?.onAdShow()
                gmRewardAd?.let {
                    val dub = DnUnionBean()
                    dub.reqId =it.showEcpm.requestId
                    dub.currentEcpm = it.showEcpm.preEcpm
                    dub.platFormType = "2"
                    dub.positionId = ""

                    loggerListener?.onAdStatus(10, AdStatus(dub))
                }
            }

            override fun onRewardedAdShowFail(p0: AdError) {
                loggerListener?.onAdError(p0.code, p0.message)
                gmRewardAd?.destroy()
            }

            override fun onRewardClick() {
                loggerListener?.onAdVideoClick()
            }

            override fun onRewardedAdClosed() {
                loggerListener?.onAdClose()
                gmRewardAd?.destroy()
            }

            override fun onVideoComplete() {
                loggerListener?.onVideoComplete()
            }

            override fun onVideoError() {
                gmRewardAd?.destroy()
            }

            override fun onRewardVerify(p0: RewardItem) {
                loggerListener?.onRewardVerify(p0.rewardVerify())
            }

            override fun onSkippedVideo() {
                loggerListener?.onAdSkipped()
            }
        })
        gmRewardAd?.showRewardAd(activity)
    }
}
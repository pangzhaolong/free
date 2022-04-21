package com.dn.sdk.platform.gromore.helper

import android.app.Activity
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.GMAdConstant
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAd
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil
import com.bytedance.msdk.api.v2.slot.GMAdSlotFullVideo
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.fullscreenvideo.IAdFullScreenVideoLoadListener
import com.dn.sdk.listener.fullscreenvideo.TrackFullScreenVideoLoadListenerProxy
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.gromore.bean.GroMoreFullScreenVideoAd


/**
 * GroMore广告Sdk 请求全屏视频
 *
 * @author lcl
 * @version v1.0
 * @date 2022/3/2 11:13
 */
object GroMoreFullScreenVideoLoadHelper : BaseHelper() {

    /**
     * 全屏视频的加载
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdFullScreenVideoLoadListener?
     */
    fun loadFullScreenVideoAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdFullScreenVideoLoadListener?
    ) {
        runOnUiThread(activity) {
            listener?.onAdStartLoad() //开始加载广告
        }
        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdIdNullOrBlank.code,
                    AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                )
            }
            return
        }

        if (adRequest.mAdCount > 5) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdCountError.code,
                    AdCustomError.ParamsAdCountError.errorMsg
                )
            }
            return
        }

        val countTrackImpl = if (listener is TrackFullScreenVideoLoadListenerProxy) {
            listener.countTrack
        } else {
            CountTrackImpl(adRequest)
        }

        val gmFullVideoAd = GMFullVideoAd(activity, adRequest.mAdId)

        bindLifecycle(activity, null) {
            gmFullVideoAd.destroy()
        }


        val customData: MutableMap<String, String> = HashMap()
        customData[GMAdConstant.CUSTOM_DATA_KEY_GDT] = "gdt custom data" //目前仅支持gdt

        val orientation = if (adRequest.mOrientation == 1) {
            TTAdConstant.VERTICAL
        } else {
            TTAdConstant.HORIZONTAL
        }

        val muted = adRequest.mMuted
        val volume = adRequest.volume

        val adSlotFullVideo = GMAdSlotFullVideo.Builder()
            .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())
            //广点通个性配置
            .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build())
            .setUserID(adRequest.mUserId)
            .setOrientation(orientation)
            .setRewardName(adRequest.mRewardName)
            .setRewardAmount(adRequest.mRewardAmount)
            // 对除广点通外的信息流广告生效，其中穿山甲SDK需要在云端单独配置。
            .setMuted(muted)
            .setVolume(volume)
            .setCustomData(customData)
            .build()

        gmFullVideoAd.loadAd(adSlotFullVideo, object : GMFullVideoAdLoadCallback {
            override fun onFullVideoLoadFail(p0: AdError) {
                runOnUiThread(activity) {
                    listener?.onAdError(p0.code, p0.message)
                }
            }

            override fun onFullVideoAdLoad() {
                //视频广告描述信息加载完成
            }

            override fun onFullVideoCached() {
                runOnUiThread(activity) {
                    listener?.let {
                        it.onAdLoad(
                            GroMoreFullScreenVideoAd(
                                adRequest,
                                countTrackImpl,
                                gmFullVideoAd
                            )
                        )
                    }
                }
            }
        })
    }

}
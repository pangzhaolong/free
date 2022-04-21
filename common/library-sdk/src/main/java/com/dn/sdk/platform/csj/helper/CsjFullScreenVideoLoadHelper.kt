package com.dn.sdk.platform.csj.helper

import android.app.Activity
import com.bytedance.sdk.openadsdk.*
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.fullscreenvideo.IAdFullScreenVideoLoadListener
import com.dn.sdk.listener.fullscreenvideo.TrackFullScreenVideoLoadListenerProxy
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.csj.bean.CsjFullScreenVideoAd

/**
 * 穿山甲 全屏视频加载
 *
 *  make in st
 *  on 2021/12/27 18:17
 */
object CsjFullScreenVideoLoadHelper : BaseHelper() {

    fun loadFullScreenAd(activity: Activity, adRequest: AdRequest, listener: IAdFullScreenVideoLoadListener?) {
        runOnUiThread(activity) {
            listener?.onAdStartLoad()
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


        val orientation = if (adRequest.mOrientation == 1) {
            TTAdConstant.VERTICAL
        } else {
            TTAdConstant.HORIZONTAL
        }

        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)

        val adSlot = AdSlot.Builder()
            .setCodeId(adRequest.mAdId)//广告位id
            .setOrientation(orientation)
            .setExpressViewAcceptedSize(adRequest.mWidthDp, adRequest.mHeightDp)
            .build()


        val countTrackImpl = if (listener is TrackFullScreenVideoLoadListenerProxy) {
            listener.countTrack
        } else {
            CountTrackImpl(adRequest)
        }

        val doNewsFullScreenListener = object : TTAdNative.FullScreenVideoAdListener {
            //广告加载失败
            override fun onError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }

            //广告物料加载完成的回调
            override fun onFullScreenVideoAdLoad(p0: TTFullScreenVideoAd?) {

            }

            //广告视频本地加载完成的回调，接入方可以在这个回调后直接播放本地视频
            override fun onFullScreenVideoCached() {

            }

            override fun onFullScreenVideoCached(ad: TTFullScreenVideoAd?) {
                runOnUiThread(activity) {
                    listener?.let {
                        if (ad != null) {
                            it.onAdLoad(CsjFullScreenVideoAd(adRequest, countTrackImpl, ad))
                        }
                    }
                }
            }
        }
        mTTAdNative.loadFullScreenVideoAd(adSlot, doNewsFullScreenListener)
    }
}
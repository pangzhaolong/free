package com.dn.sdk.platform.gromore.helper

import android.app.Activity
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.gromore.preloadad.GroMorePreloadInterstitialAd
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil

import com.bytedance.msdk.api.v2.slot.GMAdSlotInterstitial

import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAd
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAdListener
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAdLoadCallback


/**
 * 多牛v2 加载插屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 16:01
 */
object GroMoreInterstitialLoadHelper : BaseHelper() {

    /**
     * 穿山甲插屏广告加载
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdInterstitialListener?
     * @return PreloadInterstitialAd
     */
    fun preloadInterstitialAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ): PreloadInterstitialAd {

        val mInterstitialAd = GMInterstitialAd(activity, adRequest.mAdId)

        val adSlotInterstitial = GMAdSlotInterstitial.Builder()
            .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())
            .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build())
            .setImageAdSize(adRequest.mWidthDp.toInt(), adRequest.mHeightDp.toInt())
            .build()

        val preloadAd = GroMorePreloadInterstitialAd(mInterstitialAd, activity)
        preloadAd.setLoadState(PreloadAdState.Loading)
        mInterstitialAd.setAdInterstitialListener(object : GMInterstitialAdListener {
            override fun onInterstitialShow() {
                preloadAd.setLoadState(PreloadAdState.Shown)
                listener?.onAdShow()
                listener?.onAdExposure()
            }

            override fun onInterstitialShowFail(p0: AdError) {
                listener?.onAdError(p0.code, p0.message)
            }

            override fun onInterstitialAdClick() {
                listener?.onAdClicked()
            }

            override fun onInterstitialClosed() {
                listener?.onAdClosed()
            }

            override fun onAdOpened() {
            }

            override fun onAdLeftApplication() {
            }

        })
        listener?.onAdStartLoad()
        DelayExecutor.delayExec(100) {
            mInterstitialAd.loadAd(adSlotInterstitial, object : GMInterstitialAdLoadCallback {
                override fun onInterstitialLoadFail(p0: AdError) {
                    preloadAd.setLoadState(PreloadAdState.Error)
                    p0?.let {
                        listener?.onAdError(it.code, it.message)
                    } ?: run {
                        listener?.onAdError(
                            AdCustomError.InterstitialPreloadError.code,
                            AdCustomError.InterstitialPreloadError.errorMsg
                        )
                    }
                }

                override fun onInterstitialLoad() {
                    listener?.onAdLoad()
                    preloadAd.setLoadState(PreloadAdState.Success)
                    if (preloadAd.isNeedShow()) {
                        preloadAd.showAd()
                    }
                }
            })
        }
        return preloadAd
    }
}
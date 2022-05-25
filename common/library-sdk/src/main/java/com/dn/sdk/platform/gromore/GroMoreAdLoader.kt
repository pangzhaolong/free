package com.dn.sdk.platform.gromore;

import android.app.Activity
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.listener.banner.IAdBannerListener
import com.dn.sdk.listener.banner.LoggerBannerListenerProxy
import com.dn.sdk.listener.banner.TrackBannerListenerProxy
import com.dn.sdk.listener.draw.natives.IAdDrawNativeLoadListener
import com.dn.sdk.listener.draw.natives.LoggerDrawNativeLoadListenerProxy
import com.dn.sdk.listener.draw.natives.TrackDrawNativeLoadListenerProxy
import com.dn.sdk.listener.draw.template.IAdDrawTemplateLoadListener
import com.dn.sdk.listener.draw.template.LoggerDrawTemplateLoadListenerProxy
import com.dn.sdk.listener.feed.natives.IAdFeedLoadListener
import com.dn.sdk.listener.feed.natives.LoggerFeedLoadListenerProxy
import com.dn.sdk.listener.feed.template.IAdFeedTemplateListener
import com.dn.sdk.listener.feed.template.LoggerFeedTemplateListenerProxy
import com.dn.sdk.listener.feed.template.TrackFeedTemplateListenerProxy
import com.dn.sdk.listener.interstitialfull.IAdInterstitialFullScreenListener
import com.dn.sdk.listener.interstitialfull.LoggerInterstitialFullScreenVideoListenerProxy
import com.dn.sdk.listener.interstitialfull.TrackInterstitialFullScreenVideoListenerProxy
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.listener.rewardvideo.LoggerRewardVideoListenerProxy
import com.dn.sdk.listener.rewardvideo.TrackRewardVideoListenerProxy
import com.dn.sdk.listener.splash.IAdSplashListener
import com.dn.sdk.listener.splash.LoggerSplashListenerProxy
import com.dn.sdk.listener.splash.TrackSplashListenerProxy
import com.dn.sdk.loader.IAdLoader
import com.dn.sdk.loader.SdkType
import com.dn.sdk.platform.csj.helper.CsjDrawNativeLoadHelper
import com.dn.sdk.platform.csj.helper.CsjDrawTemplateLoadHelper
import com.dn.sdk.platform.csj.helper.CsjSplashLoadHelper
import com.dn.sdk.platform.donews.helper.*
import com.dn.sdk.platform.gromore.helper.GroMoreRewardedAdHelper
import com.dn.sdk.platform.gromore.helper.GroMoreSplashLoadHelper

/**
 * 多牛聚合sdk 加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 15:24
 */
class GroMoreAdLoader : IAdLoader {
    override fun getSdkType(): SdkType {
        return SdkType.GRO_MORE
    }

    override fun loadAndShowSplashAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ) {
    }

    override fun preloadSplashAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ) {
    }

    override fun isDnSplashAdReady(): Boolean {
        return false
    }

    override fun showDnSplashAd() {
    }

    override fun loadAdShowSplashAdV2(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ) {
        GroMoreSplashLoadHelper.loadAndShowAd(
                activity,
                adRequest,
                LoggerSplashListenerProxy(adRequest, listener)
        )
    }

    override fun preloadSplashAdV2(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ): PreloadSplashAd {
        return GroMoreSplashLoadHelper.preloadAd(
                activity,
                adRequest,
                LoggerSplashListenerProxy(adRequest, listener)
        )
    }

    override fun loadAndShowBannerAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdBannerListener?
    ) {
    }

    /**
     * 新增人：lcl
     * 新增方法,加载并显示插屏全屏视频(插全屏)
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdInterstitialListener?
     * @return PreloadInterstitialAd
     */
    override fun loadAndShowInterstitiaScreenFulllAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdInterstitialFullScreenListener?
    ) {
    }

    override fun loadAndShowRewardVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdRewardVideoListener?
    ) {
    }

    override fun preloadRewardVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdRewardVideoListener?
    ): PreloadRewardVideoAd? {
        return null
    }

    override fun loadFeedTemplateAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFeedTemplateListener?
    ) {
    }

    override fun loadFeedAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFeedLoadListener?
    ) {
    }

    override fun loadDrawTemplateAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdDrawTemplateLoadListener?
    ) {
        CsjDrawTemplateLoadHelper.loadDrawTemplateAd(
                activity,
                adRequest,
                LoggerDrawTemplateLoadListenerProxy(adRequest, listener)
        )
    }

    override fun loadDrawAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdDrawNativeLoadListener?
    ) {
        CsjDrawNativeLoadHelper.loadDrawAd(
                activity,
                adRequest,
                TrackDrawNativeLoadListenerProxy(adRequest, LoggerDrawNativeLoadListenerProxy(adRequest, listener))
        )
    }

    override fun loadCsjSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        CsjSplashLoadHelper.loadSplashAd(activity, adRequest, listener)
    }

    override fun preLoadCsjSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        CsjSplashLoadHelper.preLoadSplashAd(activity, adRequest, listener)
    }

    override fun isCsjSplashAdReady(): Boolean {
        return CsjSplashLoadHelper.isAdReady()
    }

    override fun showCsjPreloadSplashAd() {
        CsjSplashLoadHelper.showSplash()
    }

    override fun loadGroMoreRewardedAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?) {
        GroMoreRewardedAdHelper.loadRewardedAd(activity, adRequest, listener)
    }

    override fun isGroMoreRewardAdReady(): Boolean {
        return GroMoreRewardedAdHelper.isAdReady()
    }

    override fun showGroMoreRewardedAd(activity: Activity, listener: IAdRewardVideoListener?) {
        GroMoreRewardedAdHelper.showRewardedAd(activity, listener)
    }
}

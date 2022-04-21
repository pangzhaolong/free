package com.donews.yfsdk.loader

import android.app.Activity
import android.view.ViewGroup
import com.dn.sdk.listener.banner.IAdBannerListener
import com.dn.sdk.listener.draw.natives.IAdDrawNativeLoadListener
import com.dn.sdk.listener.draw.template.IAdDrawTemplateLoadListener
import com.dn.sdk.listener.feed.natives.IAdFeedLoadListener
import com.dn.sdk.listener.feed.template.IAdFeedTemplateListener
import com.dn.sdk.listener.fullscreenvideo.IAdFullScreenVideoLoadListener
import com.dn.sdk.listener.interstitial.IAdInterstitialFullScreenListener
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.listener.splash.IAdSplashListener

/**
 * 上层请求广告管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 16:45
 */
interface IAdLoadManager {

    /**banner*/
    fun loadAndShowBannerAd(activity: Activity, adContainer: ViewGroup, widthDp: Float, heightDp: Float, listener: IAdBannerListener?)

    /**开屏*/
    fun loadSplashAd(activity: Activity, hotStart: Boolean, container: ViewGroup, listener: IAdSplashListener?, isHalfScreen: Boolean)

    /**插屏*/
    fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?)

    /**激励视频*/
    fun loadRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?)

    /**预加载激励视频*/
    fun preloadRewardVideoAd(activity: Activity, preloadAdListener: IPreloadAdListener, listener: IAdRewardVideoListener?)

    /**插屏（全屏）*/
    fun loadAndShowInterstitialFullAd(activity: Activity, listener: IAdInterstitialFullScreenListener?)

    /**信息流模板*/
    fun loadFeedTemplateAd(activity: Activity, widthDp: Float, heightDp: Float, listener: IAdFeedTemplateListener?)

    /**信息流自渲染*/
    fun loadFeedNativeAd(activity: Activity, listener: IAdFeedLoadListener?)

    /**Draw信息流模板*/
    fun loadDrawTemplateAd(activity: Activity, listener: IAdDrawTemplateLoadListener?)

    /**Draw信息流*/
    fun loadDrawNativeAd(activity: Activity, listenerNative: IAdDrawNativeLoadListener?)

    /**全屏视频*/
    fun loadFullScreenVideoAd(activity: Activity, listener: IAdFullScreenVideoLoadListener?)

    /** 穿山甲开屏广告*/
    fun loadCsjSplashAd(activity: Activity,
                     hotStart: Boolean,
                     container: ViewGroup,
                     isHalfScreen: Boolean,
                     listener: IAdSplashListener?)

}
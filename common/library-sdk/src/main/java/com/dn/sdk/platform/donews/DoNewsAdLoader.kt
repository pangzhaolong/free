package com.dn.sdk.platform.donews;

import android.app.Activity
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.preload.PreloadInterstitialAd
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
import com.dn.sdk.listener.fullscreenvideo.IAdFullScreenVideoLoadListener
import com.dn.sdk.listener.fullscreenvideo.LoggerFullScreenVideoLoadListenerProxy
import com.dn.sdk.listener.interstitial.*
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
import com.dn.sdk.platform.gromore.helper.GroMoreFullScreenVideoLoadHelper
import com.dn.sdk.platform.gromore.helper.GroMoreInterstitialLoadHelper
import com.dn.sdk.platform.gromore.helper.GroMoreSplashLoadHelper

/**
 * 多牛聚合sdk 加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 15:24
 */
class DoNewsAdLoader : IAdLoader {
    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun loadAndShowSplashAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ) {
        DoNewsSplashLoadHelper.loadAndShowAd(
                activity,
                adRequest,
                LoggerSplashListenerProxy(adRequest, TrackSplashListenerProxy(adRequest, listener))
        )
    }

    override fun preloadSplashAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ): PreloadSplashAd {
        return DoNewsSplashLoadHelper.preloadAd(
                activity,
                adRequest,
                LoggerSplashListenerProxy(adRequest, TrackSplashListenerProxy(adRequest, listener))
        )
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
        DoNewsBannerLoadHelper.loadAndShowAd(
                activity,
                adRequest,
                LoggerBannerListenerProxy(adRequest, TrackBannerListenerProxy(adRequest, listener))
        )
    }

    override fun loadAndShowInterstitialAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdInterstitialListener?
    ) {
        DoNewsInterstitialLoadHelper.loadAndShowAd(
                activity,
                adRequest,
                LoggerInterstitialListenerProxy(
                        adRequest,
                        TrackInterstitialListenerProxy(adRequest, listener)
                )
        )
    }

    /**
     * 新增人：lcl
     * 新增方法,预加载插屏广告
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdInterstitialListener?
     * @return PreloadInterstitialAd
     */
    override fun preloadInterstitialAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdInterstitialListener?
    ): PreloadInterstitialAd {
        //走GroMre插屏预加载
        return GroMoreInterstitialLoadHelper.preloadInterstitialAd(
                activity, adRequest, LoggerInterstitialListenerProxy(
                adRequest,
                TrackInterstitialListenerProxy(adRequest, listener)
        )
        )
        //走GroMre插屏预加载
//        return DoNewsInterstitialLoadHelper.preloadInterstitialAd(
//            activity, adRequest, LoggerInterstitialListenerProxy(
//                adRequest,
//                TrackInterstitialListenerProxy(adRequest, listener)
//            )
//        )
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
        DoNewsInterstitialFullScreenLoadHelper.loadAndShowAd(
                activity, adRequest, LoggerInterstitialFullScreenVideoListenerProxy(
                adRequest,
                TrackInterstitialFullScreenVideoListenerProxy(adRequest, listener)
        )
        )
    }

    override fun loadAndShowRewardVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdRewardVideoListener?
    ) {
        DoNewsRewardVideoLoadHelper.loadAndShowAd(
                activity,
                adRequest,
                LoggerRewardVideoListenerProxy(
                        adRequest,
                        TrackRewardVideoListenerProxy(adRequest, listener)
                )
        )
    }

    override fun preloadRewardVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdRewardVideoListener?
    ): PreloadRewardVideoAd {
        return DoNewsRewardVideoLoadHelper.preloadAd(
                activity,
                adRequest,
                LoggerRewardVideoListenerProxy(
                        adRequest,
                        TrackRewardVideoListenerProxy(adRequest, listener)
                )
        )
    }

    override fun loadFeedTemplateAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFeedTemplateListener?
    ) {
        DoNewsFeedTemplateLoadHelper.loadFeedTemplateAd(
                activity, adRequest,
                LoggerFeedTemplateListenerProxy(
                        adRequest,
                        TrackFeedTemplateListenerProxy(adRequest, listener)
                )
        )
    }

    override fun loadFeedAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFeedLoadListener?
    ) {
        DoNewsFeedLoadHelper.loadFeedAd(
                activity,
                adRequest,
                LoggerFeedLoadListenerProxy(adRequest, listener)
        )
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

    override fun loadFullScreenVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFullScreenVideoLoadListener?
    ) {
        //新增：2022.3.7
        //走新的GroMore的全屏视频广告
        GroMoreFullScreenVideoLoadHelper.loadFullScreenVideoAd(
                activity,
                adRequest,
                LoggerFullScreenVideoLoadListenerProxy(adRequest, listener)
        )
        //失效：2022.3.7
        //走的原始穿山甲的全屏广告
//        CsjFullScreenVideoLoadHelper.loadFullScreenAd(
//            activity,
//            adRequest,
//            LoggerFullScreenVideoLoadListenerProxy(adRequest, listener)
//        )
    }

    override fun loadCsjSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        CsjSplashLoadHelper.loadSplashAd(activity, adRequest, listener)
    }
}

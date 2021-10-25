package com.dn.sdk.business.loader

import android.app.Activity
import android.content.Context
import com.dn.sdk.business.callback.JddAdIdConfigHelper
import com.dn.sdk.sdk.AdSdkManager
import com.dn.sdk.sdk.bean.AdType
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.IAdIdConfig
import com.dn.sdk.sdk.interfaces.ISdkManager
import com.dn.sdk.sdk.interfaces.listener.*
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener
import com.dn.sdk.sdk.interfaces.loader.IAdLoader
import com.dn.sdk.sdk.platform.IAdIdConfigCallback

/**
 * 广告加载实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:16
 */
object AdLoader : IAdLoader, ISdkManager by AdSdkManager, IAdIdConfig by JddAdIdConfigHelper {

    override fun loadSplashAd(activity: Activity, adIdKey: String, listener: IAdSplashListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.SPLASH
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadSplashAd(activity, requestInfo, listener)
            }
        })
    }

    override fun loadBannerAd(activity: Activity, adIdKey: String, listener: IAdBannerListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.BANNER
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadBannerAd(activity, requestInfo, listener)
            }
        })
    }

    override fun loadInterstitialAd(activity: Activity, adIdKey: String, listener: IAdInterstitialListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.INTERSTITIAL
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadInterstitialAd(activity, requestInfo, listener)
            }
        })
    }

    override fun loadRewardVideoAd(activity: Activity, adIdKey: String, listener: IAdRewardVideoListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadRewardVideoAd(activity, requestInfo, listener)
            }
        })
    }

    override fun preloadRewardViewAd(
        activity: Activity,
        adIdKey: String,
        viewListener: IAdPreloadVideoViewListener,
        listener: IAdRewardVideoListener?
    ) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().preloadRewardViewAd(activity, requestInfo, viewListener, listener)
            }
        })
    }

    override fun loadFullVideoAd(activity: Activity, adIdKey: String, listener: IAdFullVideoListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.FULL_SCREEN_VIDEO
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadFullVideoAd(activity, requestInfo, listener)
            }
        })
    }

    override fun preloadFullVideoAd(
        activity: Activity,
        adIdKey: String,
        viewListener: IAdPreloadVideoViewListener?,
        listener: IAdFullVideoListener?
    ) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.FULL_SCREEN_VIDEO
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().preloadFullVideoAd(activity, requestInfo, viewListener, listener)
            }
        })
    }

    override fun loadFeedNativeAd(context: Context, adIdKey: String, listener: IAdNativeListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.NEWS_FEED_CUSTOM_RENDER
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadFeedNativeAd(context, requestInfo, listener)
            }
        })
    }

    override fun loadFeedNativeExpressAd(context: Context, adIdKey: String, listener: IAdNativeExpressListener?) {
        JddAdIdConfigHelper.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val platform = JddAdIdConfigHelper.getPlatform()
                val requestInfo = RequestInfo()
                requestInfo.platform = platform
                requestInfo.adType = AdType.NEWS_FEED_TEMPLATE
                requestInfo.adId = platform.getAdIdByKey(adIdKey)
                platform.getLoader().loadFeedNativeExpressAd(context, requestInfo, listener)
            }
        })
    }
}
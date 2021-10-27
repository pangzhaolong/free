package com.dn.sdk.business.loader

import android.app.Activity
import android.view.ViewGroup
import com.dn.sdk.business.bean.JddAdIdConfigBean
import com.dn.sdk.business.callback.JddAdIdConfigManager
import com.dn.sdk.business.constant.*
import com.dn.sdk.sdk.AdSdkManager
import com.dn.sdk.sdk.bean.AdType
import com.dn.sdk.sdk.bean.RequestInfo
import com.dn.sdk.sdk.interfaces.ISdkManager
import com.dn.sdk.sdk.interfaces.idconfig.IAdIdConfig
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener
import com.dn.sdk.sdk.interfaces.loader.IAdManager
import com.dn.sdk.sdk.platform.IAdIdConfigCallback
import com.donews.common.utils.DensityUtils
import com.orhanobut.logger.Logger

/**
 * 广告加载实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:16
 */
object AdManager : IAdManager, ISdkManager by AdSdkManager, IAdIdConfig by JddAdIdConfigManager {
    override fun loadInvalidRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = INVALID_REWARD_VIDEO
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.platform.getLoader().loadRewardVideoAd(activity, requestInfo, listener)
            }
        })
    }

    override fun preloadInvalidRewardVideoAd(
        activity: Activity,
        viewListener: IAdPreloadVideoViewListener,
        listener: IAdRewardVideoListener?
    ) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = INVALID_REWARD_VIDEO
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.platform.getLoader().preloadRewardViewAd(activity, requestInfo, viewListener, listener)
            }
        })
    }

    override fun loadInvalidInterstitialAd(activity: Activity, listener: IAdInterstitialListener?) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = INVALID_INTERSTITIAL
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.INTERSTITIAL
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.width = 600
                requestInfo.height = 900
                requestInfo.platform.getLoader().loadInterstitialAd(activity, requestInfo, listener)
            }
        })
    }

    override fun loadFullScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = SPLASH
                val minimumCodeIdKey = SPLASH_MINIMUM_CODE_ID
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.minimumCodeId = requestInfo.platform.getAdIdByKey(minimumCodeIdKey)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.SPLASH
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.container = container
                requestInfo.width = DensityUtils.getScreenWidth()
                requestInfo.height = DensityUtils.getScreenHeight()
                requestInfo.platform.getLoader().loadSplashAd(activity, requestInfo, listener)
            }
        })
    }

    override fun loadHalfScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = SPLASH
                val minimumCodeIdKey = SPLASH_MINIMUM_CODE_ID
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.minimumCodeId = requestInfo.platform.getAdIdByKey(minimumCodeIdKey)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.SPLASH
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.container = container
                requestInfo.width = DensityUtils.getScreenWidth()
                requestInfo.height = (DensityUtils.getScreenHeight() * 0.27f).toInt()
                requestInfo.platform.getLoader().loadSplashAd(activity, requestInfo, listener)
            }
        })
    }

    override fun loadRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = REWARD_VIDEO
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.platform.getLoader().loadRewardVideoAd(activity, requestInfo, listener)
            }
        })
    }

    override fun preloadRewardVideoAd(
        activity: Activity,
        viewListener: IAdPreloadVideoViewListener,
        listener: IAdRewardVideoListener?
    ) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = REWARD_VIDEO
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.platform.getLoader().preloadRewardViewAd(activity, requestInfo, viewListener, listener)
            }
        })
    }

    override fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?) {
        JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
            override fun initSuccess() {
                val key = INTERSTITIAL
                val requestInfo = RequestInfo()
                requestInfo.platform = JddAdIdConfigManager.getPlatform()
                requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                requestInfo.appIdKey = key
                requestInfo.adType = AdType.INTERSTITIAL
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.width = 600
                requestInfo.height = 900
                Logger.d(requestInfo);
                requestInfo.platform.getLoader().loadInterstitialAd(activity, requestInfo, listener)
            }
        })
    }

}
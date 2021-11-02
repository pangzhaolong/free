package com.donews.common.ad.business.loader

import android.app.Activity
import android.view.ViewGroup
import android.view.WindowManager
import com.donews.common.ad.business.callback.JddAdIdConfigManager
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
import com.donews.base.utils.ToastUtil
import com.donews.common.BuildConfig
import com.donews.common.ad.business.constant.*
import com.donews.common.ad.business.monitor.InterstitialAdCount
import com.donews.common.ad.business.proxy.JddAdRewardVideoListenerProxy
import com.donews.common.ad.business.proxy.JddInterstitialListenerProxy
import com.donews.utilslibrary.utils.DensityUtils

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
                requestInfo.platform.getLoader().loadRewardVideoAd(
                    activity, requestInfo,
                    JddAdRewardVideoListenerProxy(activity, listener)
                )
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
                val pxScreenWidth = DensityUtils.getScreenWidth()
                val marginWidth = DensityUtils.dip2px(30f) * 2
                requestInfo.width = DensityUtils.px2dp((pxScreenWidth - marginWidth).toFloat()).toInt()
                requestInfo.height = 0
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
                requestInfo.width = container.layoutParams.width
                requestInfo.height = container.layoutParams.height
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
                requestInfo.isOpenSound = true
                requestInfo.platform.getLoader().loadRewardVideoAd(
                    activity, requestInfo,
                    JddAdRewardVideoListenerProxy(activity, listener)
                )
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
                requestInfo.isOpenSound = true
                requestInfo.adType = AdType.REWARD_VIDEO
                requestInfo.userId = AdSdkManager.userId
                requestInfo.channel = AdSdkManager.channel
                requestInfo.oaid = AdSdkManager.oaid
                requestInfo.platform.getLoader().preloadRewardViewAd(activity, requestInfo, viewListener, listener)
            }
        })
    }

    override fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?) {
        if (InterstitialAdCount.isCanShowInters()) {
            //先更新一次显示时间
            InterstitialAdCount.updateLoadAdTime()
            JddAdIdConfigManager.addInitListener(object : IAdIdConfigCallback {
                override fun initSuccess() {
                    val proxyListener = JddInterstitialListenerProxy(listener)
                    val key = INTERSTITIAL
                    val requestInfo = RequestInfo()
                    requestInfo.platform = JddAdIdConfigManager.getPlatform()
                    requestInfo.adId = requestInfo.platform.getAdIdByKey(key)
                    requestInfo.appIdKey = key
                    requestInfo.adType = AdType.INTERSTITIAL
                    requestInfo.userId = AdSdkManager.userId
                    requestInfo.channel = AdSdkManager.channel
                    requestInfo.oaid = AdSdkManager.oaid

                    val pxScreenWidth = DensityUtils.getScreenWidth()
                    val marginWidth = DensityUtils.dip2px(30f) * 2
                    requestInfo.width = DensityUtils.px2dp((pxScreenWidth - marginWidth).toFloat()).toInt()
                    requestInfo.height = 0
                    requestInfo.platform.getLoader().loadInterstitialAd(activity, requestInfo, proxyListener)
                }
            })
        } else {
            listener?.onError(-1001, "显示插屏广告间隔小于配置时间")
//            if (BuildConfig.DEBUG) {
//                ToastUtil.showShort(activity, "显示插屏广告间隔小于配置时间")
//            }
        }
    }

}
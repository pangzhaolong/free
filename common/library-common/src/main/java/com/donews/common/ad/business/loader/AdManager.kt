package com.donews.common.ad.business.loader

import android.app.Activity
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdType
import com.dn.sdk.listener.IAdInterstitialListener
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.listener.IAdSplashListener
import com.dn.sdk.manager.config.IAdConfigInitListener
import com.dn.sdk.manager.config.IAdConfigManager
import com.dn.sdk.manager.sdk.AdSdkManager
import com.dn.sdk.manager.sdk.ISdkManager
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.common.ad.business.constant.NEW_INTERSTITIAL_ID
import com.donews.common.ad.business.constant.NEW_INVALID_REWARD_VIDEO_ID
import com.donews.common.ad.business.constant.NEW_REWARD_VIDEO_ID
import com.donews.common.ad.business.constant.NEW_SPLASH_ID
import com.donews.common.ad.business.manager.JddAdManager
import com.donews.common.ad.business.monitor.InterstitialAdCount
import com.donews.common.ad.business.monitor.RewardVideoCount
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
object AdManager : IAdLoadManager, ISdkManager by AdSdkManager, IAdConfigManager by JddAdManager {

    /** 加载插屏广告 */
    override fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?) {
        if (InterstitialAdCount.isCanShowInters()) {
            //先更新一次显示时间
            InterstitialAdCount.showAdStart()
            InterstitialAdCount.updateLoadAdTime()

            addInitListener(object : IAdConfigInitListener {
                override fun initSuccess() {
                    val key = NEW_INTERSTITIAL_ID
                    val adRequest = AdRequest(AdType.INTERSTITIAL)
                    adRequest.mPlatform = getPlatform()
                    adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                    adRequest.mAdKey = key
                    adRequest.mAdPreload = true

                    val pxScreenWidth = DensityUtils.getScreenWidth()
                    val marginWidth = DensityUtils.dip2px(30f) * 2
                    adRequest.mWidthDp = DensityUtils.px2dp((pxScreenWidth - marginWidth).toFloat())
                    adRequest.mHeightDp = adRequest.mWidthDp / 2f * 3
                    val proxyListener = JddInterstitialListenerProxy(listener)
                    adRequest.mPlatform.getLoader().loadAndShowInterstitialAd(activity, adRequest, proxyListener)
                }
            })
        } else {
            listener?.onAdError(
                AdCustomError.InterstitialIntervalError.code,
                AdCustomError.InterstitialIntervalError.errorMsg
            )
        }
    }

    /** 加载全屏的开屏广告 */
    override fun loadFullScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?) {
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_SPLASH_ID
                val adRequest = AdRequest(AdType.SPLASH)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mAdContainer = container
                adRequest.mWidthDp = DensityUtils.px2dp(DensityUtils.getScreenWidth().toFloat())
                adRequest.mHeightDp = DensityUtils.px2dp(DensityUtils.getScreenHeight().toFloat())
                adRequest.mPlatform.getLoader().loadAndShowSplashAd(activity, adRequest, listener)
            }
        })
    }

    override fun preloadFullScreenSplashAd(
        activity: Activity,
        container: ViewGroup,
        preloadAdListener: IPreloadAdListener,
        listener: IAdSplashListener?
    ) {
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_SPLASH_ID
                val adRequest = AdRequest(AdType.SPLASH)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mAdContainer = container
                adRequest.mWidthDp = DensityUtils.px2dp(DensityUtils.getScreenWidth().toFloat())
                adRequest.mHeightDp = DensityUtils.px2dp(DensityUtils.getScreenHeight().toFloat())
                val preloadAd = adRequest.mPlatform.getLoader().preloadSplashAd(activity, adRequest, listener)
                preloadAdListener.preloadAd(preloadAd)
            }
        })
    }


    /** 加载半屏的开屏广告 */
    override fun loadHalfScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?) {
        AdLoggerUtils.d("loadHalfScreenSplashAd")
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_SPLASH_ID
                val adRequest = AdRequest(AdType.SPLASH)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mAdContainer = container

                if (container.layoutParams.width > 0) {
                    adRequest.mWidthDp = DensityUtils.px2dp(container.layoutParams.width.toFloat())
                    adRequest.mHeightDp = DensityUtils.px2dp(container.layoutParams.height.toFloat()) - 96
                } else {
                    adRequest.mWidthDp = DensityUtils.px2dp(ScreenUtils.getScreenWidth().toFloat())
                    adRequest.mHeightDp = DensityUtils.px2dp(ScreenUtils.getScreenHeight().toFloat()) - 96
                }
                adRequest.mPlatform.getLoader().loadAndShowSplashAd(activity, adRequest, listener)
            }
        })
    }

    override fun preloadHalfScreenSplashAd(
        activity: Activity,
        container: ViewGroup,
        preloadAdListener: IPreloadAdListener,
        listener: IAdSplashListener?
    ) {
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_SPLASH_ID
                val adRequest = AdRequest(AdType.SPLASH)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mAdContainer = container
                if (container.layoutParams.width > 0) {
                    adRequest.mWidthDp = DensityUtils.px2dp(container.layoutParams.width.toFloat())
                    adRequest.mHeightDp = DensityUtils.px2dp(container.layoutParams.height.toFloat()) - 96
                } else {
                    adRequest.mWidthDp = DensityUtils.px2dp(ScreenUtils.getScreenWidth().toFloat())
                    adRequest.mHeightDp = DensityUtils.px2dp(ScreenUtils.getScreenHeight().toFloat()) - 96
                }
                val preloadAd = adRequest.mPlatform.getLoader().preloadSplashAd(activity, adRequest, listener)
                preloadAdListener.preloadAd(preloadAd)
            }
        })
    }

    /** 直接加载激励视频 */
    override fun loadRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?) {
        if (!RewardVideoCount.checkShouldLoadAd()) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${AdCustomError.LimitAdError.code},${AdCustomError.LimitAdError.errorMsg}")
            listener?.onAdError(AdCustomError.LimitAdError.code, AdCustomError.LimitAdError.errorMsg)
            return
        }
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_REWARD_VIDEO_ID
                val adRequest = AdRequest(AdType.REWARD_VIDEO)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                val proxyListener = JddAdRewardVideoListenerProxy(activity, listener)
                adRequest.mPlatform.getLoader().loadAndShowRewardVideoAd(activity, adRequest, proxyListener)
            }
        })
    }

    /** 使用无效广告位加载激励视频 */
    override fun loadInvalidRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?) {
        if (!RewardVideoCount.checkShouldLoadAd()) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${AdCustomError.LimitAdError.code},${AdCustomError.LimitAdError.errorMsg}")
            listener?.onAdError(AdCustomError.LimitAdError.code, AdCustomError.LimitAdError.errorMsg)
            return
        }
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_INVALID_REWARD_VIDEO_ID
                val adRequest = AdRequest(AdType.REWARD_VIDEO)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mPlatform.getLoader().loadAndShowRewardVideoAd(activity, adRequest, listener)
            }
        })

    }


    /** 预加载激励视频 */
    override fun preloadRewardVideoAd(
        activity: Activity,
        preloadAdListener: IPreloadAdListener,
        listener: IAdRewardVideoListener?
    ) {

        if (!RewardVideoCount.checkShouldLoadAd()) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${AdCustomError.LimitAdError.code},${AdCustomError.LimitAdError.errorMsg}")
            listener?.onAdError(AdCustomError.LimitAdError.code, AdCustomError.LimitAdError.errorMsg)
            return
        }

        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_REWARD_VIDEO_ID
                val adRequest = AdRequest(AdType.REWARD_VIDEO)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mAdPreload = true
                val proxyListener = JddAdRewardVideoListenerProxy(activity, listener)
                val preloadAd =
                    adRequest.mPlatform.getLoader().preloadRewardVideoAd(activity, adRequest, proxyListener)
                preloadAdListener.preloadAd(preloadAd)
            }
        })
    }

    /** 使用无效广告位预加载激励视频 */
    override fun preloadInvalidRewardVideoAd(
        activity: Activity,
        preloadAdListener: IPreloadAdListener,
        listener: IAdRewardVideoListener?
    ) {

        if (!RewardVideoCount.checkShouldLoadAd()) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${AdCustomError.LimitAdError.code},${AdCustomError.LimitAdError.errorMsg}")
            listener?.onAdError(AdCustomError.LimitAdError.code, AdCustomError.LimitAdError.errorMsg)
            return
        }
        addInitListener(object : IAdConfigInitListener {
            override fun initSuccess() {
                val key = NEW_INVALID_REWARD_VIDEO_ID
                val adRequest = AdRequest(AdType.REWARD_VIDEO)
                adRequest.mPlatform = getPlatform()
                adRequest.mAdId = adRequest.mPlatform.getAdIdByKey(key)
                adRequest.mAdKey = key
                adRequest.mAdPreload = true

                val proxyListener = JddAdRewardVideoListenerProxy(activity, listener)
                val preloadAd =
                    adRequest.mPlatform.getLoader().preloadRewardVideoAd(activity, adRequest, proxyListener)
//                val preloadAd = adRequest.mPlatform.getLoader().preloadRewardVideoAd(activity, adRequest, listener)
                preloadAdListener.preloadAd(preloadAd)
            }
        })
    }
}
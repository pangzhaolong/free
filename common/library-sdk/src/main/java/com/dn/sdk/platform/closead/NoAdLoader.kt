package com.dn.sdk.platform.closead


import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.listener.banner.IAdBannerListener
import com.dn.sdk.listener.banner.LoggerBannerListenerProxy
import com.dn.sdk.listener.draw.natives.IAdDrawNativeLoadListener
import com.dn.sdk.listener.draw.natives.LoggerDrawNativeLoadListenerProxy
import com.dn.sdk.listener.draw.template.IAdDrawTemplateLoadListener
import com.dn.sdk.listener.draw.template.LoggerDrawTemplateLoadListenerProxy
import com.dn.sdk.listener.feed.natives.IAdFeedLoadListener
import com.dn.sdk.listener.feed.natives.LoggerFeedLoadListenerProxy
import com.dn.sdk.listener.feed.template.IAdFeedTemplateListener
import com.dn.sdk.listener.feed.template.LoggerFeedTemplateListenerProxy
import com.dn.sdk.listener.fullscreenvideo.IAdFullScreenVideoLoadListener
import com.dn.sdk.listener.fullscreenvideo.LoggerFullScreenVideoLoadListenerProxy
import com.dn.sdk.listener.interstitial.IAdInterstitialFullScreenListener
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.dn.sdk.listener.interstitial.LoggerInterstitialFullScreenVideoListenerProxy
import com.dn.sdk.listener.interstitial.LoggerInterstitialListenerProxy
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.listener.rewardvideo.LoggerRewardVideoListenerProxy
import com.dn.sdk.listener.splash.IAdSplashListener
import com.dn.sdk.listener.splash.LoggerSplashListenerProxy
import com.dn.sdk.loader.IAdLoader
import com.dn.sdk.loader.SdkType
import com.dn.sdk.platform.closead.preload.NoAdPreloadInterstitialAd
import com.dn.sdk.platform.closead.preload.NoAdPreloadRewardVideo
import com.dn.sdk.platform.closead.preload.NoAdPreloadSplashAd


/**
 * 关闭广告加载器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 11:03
 */
class NoAdLoader : IAdLoader {


    override fun getSdkType(): SdkType {
        return SdkType.CLOSE_AD
    }

    override fun loadAndShowSplashAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ) {
        val loggerProxy = LoggerSplashListenerProxy(adRequest, listener)
        loggerProxy.onAdStartLoad()
        loggerProxy.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun preloadSplashAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ): PreloadSplashAd {

        val loggerProxy = LoggerSplashListenerProxy(adRequest, listener)
        loggerProxy.onAdStartLoad()
        val preloadSplashAd = NoAdPreloadSplashAd()
        preloadSplashAd.setLoadState(PreloadAdState.Error)
        //延迟执行，防止错误先执行，然后才返回预加载对象
        DelayExecutor.delayExec(500) {
            loggerProxy.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
        }
        return preloadSplashAd
    }

    override fun loadAndShowBannerAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdBannerListener?
    ) {
        val loggerBannerListener = LoggerBannerListenerProxy(adRequest, listener)
        loggerBannerListener.onAdStartLoad()
        loggerBannerListener.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadAdShowSplashAdV2(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ) {
        val loggerSplashListener = LoggerSplashListenerProxy(adRequest, listener)
        loggerSplashListener.onAdStartLoad()
        loggerSplashListener.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun preloadSplashAdV2(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ): PreloadSplashAd {
        val loggerProxy = LoggerSplashListenerProxy(adRequest, listener)
        loggerProxy.onAdStartLoad()
        val preloadSplashAd = NoAdPreloadSplashAd()
        preloadSplashAd.setLoadState(PreloadAdState.Error)
        //延迟执行，防止错误先执行，然后才返回预加载对象
        DelayExecutor.delayExec(500) {
            loggerProxy.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
        }
        return preloadSplashAd
    }

    override fun loadAndShowInterstitialAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ) {

        val loggerInterProxy = LoggerInterstitialListenerProxy(adRequest, listener)
        loggerInterProxy.onAdStartLoad()
        loggerInterProxy.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun preloadInterstitialAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ): PreloadInterstitialAd {
        listener?.onAdStartLoad()
        val preloadAd = NoAdPreloadInterstitialAd()
        DelayExecutor.delayExec(500) {
            listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
        }
        return preloadAd
    }

    override fun loadAndShowInterstitiaScreenFulllAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialFullScreenListener?
    ) {
        val loggerInterProxy = LoggerInterstitialFullScreenVideoListenerProxy(adRequest, listener)
        loggerInterProxy.onAdStartLoad()
        loggerInterProxy.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadAndShowRewardVideoAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdRewardVideoListener?
    ) {
        val loggerRewardVideoListener = LoggerRewardVideoListenerProxy(adRequest, listener)
        loggerRewardVideoListener.onAdStartLoad()
        loggerRewardVideoListener.onAdError(
            AdCustomError.CloseAd.code,
            AdCustomError.CloseAd.errorMsg
        )
    }

    override fun preloadRewardVideoAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdRewardVideoListener?
    ): PreloadRewardVideoAd {
        val loggerRewardVideoListener = LoggerRewardVideoListenerProxy(adRequest, listener)
        loggerRewardVideoListener.onAdStartLoad()
        val preloadRewardVideoAd = NoAdPreloadRewardVideo()
        preloadRewardVideoAd.setLoadState(PreloadAdState.Error)
        DelayExecutor.delayExec(500) {
            loggerRewardVideoListener.onAdError(
                AdCustomError.CloseAd.code,
                AdCustomError.CloseAd.errorMsg
            )
        }
        return preloadRewardVideoAd
    }

    override fun loadFeedTemplateAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdFeedTemplateListener?
    ) {
        val loggerFeedTemplateListener = LoggerFeedTemplateListenerProxy(adRequest, listener)
        loggerFeedTemplateListener.onAdStartLoad()
        loggerFeedTemplateListener.onAdError(
            AdCustomError.CloseAd.code,
            AdCustomError.CloseAd.errorMsg
        )
    }

    override fun loadFeedAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdFeedLoadListener?
    ) {
        val loggerFeedLoadListener = LoggerFeedLoadListenerProxy(adRequest, listener)
        loggerFeedLoadListener.onAdStartLoad()
        loggerFeedLoadListener.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadDrawTemplateAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdDrawTemplateLoadListener?
    ) {
        val loggerDrawTemplateLoadListener =
            LoggerDrawTemplateLoadListenerProxy(adRequest, listener)
        loggerDrawTemplateLoadListener.onAdStartLoad()
        loggerDrawTemplateLoadListener.onAdError(
            AdCustomError.CloseAd.code,
            AdCustomError.CloseAd.errorMsg
        )
    }

    override fun loadDrawAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdDrawNativeLoadListener?
    ) {
        val loggerDrawLoadListener = LoggerDrawNativeLoadListenerProxy(adRequest, listener)
        loggerDrawLoadListener.onAdStartLoad()
        loggerDrawLoadListener.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadFullScreenVideoAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdFullScreenVideoLoadListener?
    ) {
        val loggerFullScreenVideoLoadListener =
            LoggerFullScreenVideoLoadListenerProxy(adRequest, listener)
        loggerFullScreenVideoLoadListener.onAdStartLoad()
        loggerFullScreenVideoLoadListener.onAdError(
            AdCustomError.CloseAd.code,
            AdCustomError.CloseAd.errorMsg
        )
    }

    override fun loadCsjSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        val loggerSplashLoaderListener = LoggerSplashListenerProxy(adRequest, listener)
        loggerSplashLoaderListener.onAdStartLoad()
        loggerSplashLoaderListener.onAdError(
                AdCustomError.CloseAd.code,
                AdCustomError.CloseAd.errorMsg)

    }
}

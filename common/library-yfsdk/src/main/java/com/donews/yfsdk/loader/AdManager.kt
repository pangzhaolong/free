package com.donews.yfsdk.loader

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdType
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
import com.dn.sdk.manager.sdk.AdSdkManager
import com.dn.sdk.manager.sdk.ISdkManager
import com.dn.sdk.platform.donews.DoNewsPlatform
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.utilslibrary.sdk.getSuuid
import com.donews.utilslibrary.utils.DensityUtils
import com.donews.yfsdk.check.*
import com.donews.yfsdk.manager.AdConfigManager
import com.donews.yfsdk.check.InterstitialAdCheck
import com.donews.yfsdk.monitor.InterstitialFullAdCheck
import com.donews.yfsdk.proxy.InterstitialAdListenerProxy
import com.donews.yfsdk.proxy.InterstitialFullAdListenerProxy
import com.donews.yfsdk.utils.DensityUtil
import com.donews.yfsdk.utils.IdUtil


/**
 * 广告加载实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:16
 */
object AdManager : IAdLoadManager, ISdkManager by AdSdkManager {

    /** Banner广告加载*/
    override fun loadAndShowBannerAd(
        activity: Activity,
        adContainer: ViewGroup,
        widthDp: Float,
        heightDp: Float,
        listener: IAdBannerListener?
    ) {

        val adCheckResult = BannerAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getBannerDnId()

        AdLoggerUtils.d("开始加载Banner广告， Id: $adId")

        if (adId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.BANNER)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdContainer = adContainer
        adRequest.mWidthDp = widthDp
        adRequest.mHeightDp = heightDp
        adRequest.mPlatform.getLoader().loadAndShowBannerAd(activity, adRequest, listener)
    }

    /** 加载插屏广告 */
    override fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?) {
        val adCheckResult = InterstitialAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getInstlDnId()

        AdLoggerUtils.d("开始加载插屏广告， Id: $adId")

        if (adId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        InterstitialAdCheck.onAdClose()

        //先更新一次显示时间
        val adRequest = AdRequest(AdType.INTERSTITIAL)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true

        val pxScreenWidth = DensityUtils.getScreenWidth()
        val marginWidth = DensityUtils.dip2px(30f) * 2
        adRequest.mWidthDp = DensityUtils.px2dp((pxScreenWidth - marginWidth).toFloat())
        adRequest.mHeightDp = adRequest.mWidthDp / 2f * 3
        adRequest.mPlatform.getLoader()
            .loadAndShowInterstitialAd(activity, adRequest, InterstitialAdListenerProxy(listener))
    }

    /** 预加载使用GroMore广告Id*/
    fun preloadInterstitialAd(
        activity: Activity,
        preloadAdListener: IPreloadInterstitialAd?,
        listener: IAdInterstitialListener?
    ) {
        val adCheckResult = InterstitialAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getInstlGmId()

        AdLoggerUtils.d("开始预加载插屏广告，使用GroMore平台id， Id: $adId")

        if (adId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.INTERSTITIAL)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true

        val pxScreenWidth = DensityUtils.getScreenWidth()
        val marginWidth = DensityUtils.dip2px(30f) * 2
        adRequest.mWidthDp = DensityUtils.px2dp((pxScreenWidth - marginWidth).toFloat())
        adRequest.mHeightDp = adRequest.mWidthDp / 2f * 3
        val preload =
            adRequest.mPlatform.getLoader().preloadInterstitialAd(activity, adRequest, listener)
        preloadAdListener?.preload(preload)
    }

    /** 加载开屏广告(全屏/半屏) */
    override fun loadSplashAd(
        activity: Activity,
        hotStart: Boolean,
        container: ViewGroup,
        listener: IAdSplashListener?,
        isHalfScreen: Boolean
    ) {

        val adCheckResult = SplashAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getSplashDnId()
        AdLoggerUtils.d("开始加载开屏广告， Id: $adId")
        if (adId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.SPLASH)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdContainer = container

        if (!isHalfScreen) {
            adRequest.mWidthDp = DensityUtils.px2dp(DensityUtils.getScreenWidth().toFloat())
            adRequest.mHeightDp = DensityUtils.px2dp(DensityUtils.getScreenHeight().toFloat())
        } else {
            if (container.layoutParams.width > 0) {
                adRequest.mWidthDp = DensityUtils.px2dp(container.layoutParams.width.toFloat())
                adRequest.mHeightDp =
                    DensityUtils.px2dp(container.layoutParams.height.toFloat()) - 96
            } else {
                adRequest.mWidthDp = DensityUtils.px2dp(ScreenUtils.getScreenWidth().toFloat())
                adRequest.mHeightDp =
                    DensityUtils.px2dp(ScreenUtils.getScreenHeight().toFloat()) - 96
            }
        }

        adRequest.mPlatform.getLoader().loadAndShowSplashAd(activity, adRequest, listener)
    }

    /** 直接加载激励视频 */
    override fun loadRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?) {

        RewardVideoCheck.showLoadAdInfo()

        val adCheckResult = RewardVideoCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }


        if (AdConfigManager.mRewardVideoId.reward_video_id.isBlank()) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            AdConfigManager.updateRewardId()
            return
        }

        val adRequest = AdRequest(AdType.REWARD_VIDEO)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = AdConfigManager.mRewardVideoId.reward_video_id
        adRequest.mPlatform.getLoader().loadAndShowRewardVideoAd(activity, adRequest, listener)
    }

    /** 预加载激励视频 */
    override fun preloadRewardVideoAd(
        activity: Activity,
        preloadAdListener: IPreloadAdListener,
        listener: IAdRewardVideoListener?
    ) {
        RewardVideoCheck.showLoadAdInfo()

        val adCheckResult = RewardVideoCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        if (AdConfigManager.mRewardVideoId.reward_video_id.isBlank()) {
            ToastUtils.showShort("暂无新视频，请稍后再试")
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            AdConfigManager.updateRewardId()
            return
        }

        val adRequest = AdRequest(AdType.REWARD_VIDEO)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = AdConfigManager.mRewardVideoId.reward_video_id
        adRequest.mAdPreload = true
        val preloadAd =
            adRequest.mPlatform.getLoader().preloadRewardVideoAd(activity, adRequest, listener)
        preloadAdListener.preloadAd(preloadAd)
    }

    /** 插屏（全屏）*/
    override fun loadAndShowInterstitialFullAd(
        activity: Activity,
        listener: IAdInterstitialFullScreenListener?
    ) {
        val adCheckResult = InterstitialFullAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getInstlFullDnId()
        AdLoggerUtils.d("开始加载插屏（全屏）广告， Id: $adId")
        if (adId.isBlank()) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.INTERSTITIAL_FULL)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true
        val outMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity.windowManager.defaultDisplay.getMetrics(outMetrics)
        } else {
            activity.display?.getRealMetrics(outMetrics)
        }
        val widthPixels = outMetrics.widthPixels
        val widthDP: Float = DensityUtil.px2dip(activity, widthPixels.toFloat()).toFloat()
        val heightDp: Float =
            DensityUtil.px2dip(activity, outMetrics.heightPixels.toFloat()).toFloat()
        //插屏的高度，单位dp 必填参数 不能为0
        //插屏的高度，单位dp 必填参数 不能为0
        adRequest.mWidthDp = widthDP
        adRequest.mHeightDp = heightDp
        adRequest.mUserId = getSuuid()
        adRequest.mOrientation = 1
        adRequest.mPlatform.getLoader().loadAndShowInterstitiaScreenFulllAd(
            activity,
            adRequest,
            InterstitialFullAdListenerProxy(listener)
        )
    }

    /**信息流模板*/
    override fun loadFeedTemplateAd(
        activity: Activity,
        widthDp: Float,
        heightDp: Float,
        listener: IAdFeedTemplateListener?
    ) {
        val adCheckResult = FeedNativeAndTemplateAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getFeedTemplateDnId()
        AdLoggerUtils.d("开始加载信息流模板广告， Id: $adId")
        if (adId.isBlank()) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.FEED_TEMPLATE)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mWidthDp = widthDp
        adRequest.mHeightDp = heightDp
        adRequest.mAdRequestTimeOut = 8 * 1000
        adRequest.mPlatform.getLoader().loadFeedTemplateAd(activity, adRequest, listener)
    }

    /**信息流自渲染*/
    override fun loadFeedNativeAd(activity: Activity, listener: IAdFeedLoadListener?) {
        val adCheckResult = FeedNativeAndTemplateAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getFeedTemplateDnId()
        AdLoggerUtils.d("开始加载信息流自渲染广告， Id: $adId")
        if (adId.isBlank()) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.FEED)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true
        adRequest.mPlatform.getLoader().loadFeedAd(activity, adRequest, listener)
    }

    /**Draw信息流模板*/
    override fun loadDrawTemplateAd(activity: Activity, listener: IAdDrawTemplateLoadListener?) {
        val adCheckResult = DrawNativeAndTemplateAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getDrawTemplateId()
        if (adId.isBlank()) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.DRAW_TEMPLATE)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true
        adRequest.mPlatform.getLoader().loadDrawTemplateAd(activity, adRequest, listener)
    }

    /**Draw信息流自渲染*/
    override fun loadDrawNativeAd(activity: Activity, listener: IAdDrawNativeLoadListener?) {
        val adCheckResult = DrawNativeAndTemplateAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getDrawTemplateId()
        if (adId.isBlank()) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.DRAW)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true
        adRequest.mPlatform.getLoader().loadDrawAd(activity, adRequest, listener)
    }

    /**全屏视频*/
    override fun loadFullScreenVideoAd(
        activity: Activity,
        listener: IAdFullScreenVideoLoadListener?
    ) {
        val adCheckResult = FullScreenAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            AdLoggerUtils.d("onAdError(${adCheckResult.code},${adCheckResult.errorMsg})")
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = IdUtil.getFullScreenDmId()
        if (adId.isBlank()) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ParamsAdIdNullOrBlank.code},${AdCustomError.ParamsAdIdNullOrBlank.errorMsg})")
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.FULL_SCREEN_VIDEO)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdPreload = true
        adRequest.mPlatform.getLoader().loadFullScreenVideoAd(activity, adRequest, listener)
    }

    /** 穿山甲闪屏页广告 - 作为闪屏广告兜底 */
    override fun loadCsjSplashAd(
        activity: Activity,
        hotStart: Boolean,
        container: ViewGroup,
        isHalfScreen: Boolean,
        listener: IAdSplashListener?
    ) {

        val adCheckResult = SplashAdCheck.isEnable()
        if (adCheckResult != AdCustomError.OK) {
            listener?.onAdError(adCheckResult.code, adCheckResult.errorMsg)
            return
        }

        val adId = AdConfigManager.mNormalAdBean.splash.protectId
        AdLoggerUtils.d("开始加载穿山甲开屏广告， Id: $adId")
        if (adId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        val adRequest = AdRequest(AdType.SPLASH)
        adRequest.mPlatform = DoNewsPlatform()
        adRequest.mAdId = adId
        adRequest.mAdContainer = container

        if (!isHalfScreen) {
            adRequest.mWidthDp = DensityUtils.px2dp(DensityUtils.getScreenWidth().toFloat())
            adRequest.mHeightDp = DensityUtils.px2dp(DensityUtils.getScreenHeight().toFloat())
            adRequest.widthPx = DensityUtils.getScreenWidth().toFloat()
            adRequest.heightPx = DensityUtils.getScreenHeight().toFloat()
        } else {
            if (container.layoutParams.width > 0) {
                adRequest.mWidthDp = DensityUtils.px2dp(container.layoutParams.width.toFloat())
                adRequest.mHeightDp =
                    DensityUtils.px2dp(container.layoutParams.height.toFloat()) - 96
                adRequest.widthPx = container.layoutParams.width.toFloat()
                adRequest.heightPx = container.layoutParams.height.toFloat() - 200
            } else {
                adRequest.mWidthDp = DensityUtils.px2dp(ScreenUtils.getScreenWidth().toFloat())
                adRequest.mHeightDp =
                    DensityUtils.px2dp(ScreenUtils.getScreenHeight().toFloat()) - 96
                adRequest.widthPx = DensityUtils.getScreenWidth().toFloat()
                adRequest.heightPx = DensityUtils.getScreenHeight().toFloat() - 200
            }
        }

        adRequest.mPlatform.getLoader().loadCsjSplashAd(activity, adRequest, listener)
    }
}
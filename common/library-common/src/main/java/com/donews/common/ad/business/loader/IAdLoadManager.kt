package com.donews.common.ad.business.loader

import android.app.Activity
import android.view.ViewGroup
import com.dn.sdk.listener.IAdInterstitialListener
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.listener.IAdSplashListener

/**
 * 上层请求广告管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 16:45
 */
interface IAdLoadManager {

    fun loadInvalidRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?)

    fun preloadInvalidRewardVideoAd(
        activity: Activity,
        preloadAdListener: IPreloadAdListener,
        listener: IAdRewardVideoListener?
    )

    fun loadFullScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?)

    fun preloadFullScreenSplashAd(
        activity: Activity,
        container: ViewGroup,
        preloadAdListener: IPreloadAdListener,
        listener: IAdSplashListener?
    )

    fun loadHalfScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?)

    fun preloadHalfScreenSplashAd(
        activity: Activity,
        container: ViewGroup,
        preloadAdListener: IPreloadAdListener,
        listener: IAdSplashListener??
    )

    fun loadRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?)

    fun preloadRewardVideoAd(
        activity: Activity,
        preloadAdListener: IPreloadAdListener,
        listener: IAdRewardVideoListener?
    )

    fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?)
}
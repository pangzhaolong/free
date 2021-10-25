package com.dn.sdk.sdk.interfaces.loader

import android.app.Activity
import android.content.Context
import com.dn.sdk.sdk.interfaces.listener.*
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener

/**
 * 广告loader，外部广告调用
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:09
 */
interface IAdLoader{


    /**
     * 开屏广告 (启动广告)
     */
    fun loadSplashAd(activity: Activity, adIdKey: String, listener: IAdSplashListener?)

    /**
     * Banner广告
     */
    fun loadBannerAd(activity: Activity, adIdKey: String, listener: IAdBannerListener?)

    /**
     * 插屏广告(弹窗形式广告)
     */
    fun loadInterstitialAd(activity: Activity, adIdKey: String, listener: IAdInterstitialListener?)

    /**
     * 激励视频广告
     */
    fun loadRewardVideoAd(activity: Activity, adIdKey: String, listener: IAdRewardVideoListener?)

    /**
     * 预加载激励视频广告
     */
    fun preloadRewardViewAd(
        activity: Activity, adIdKey: String,
        viewListener: IAdPreloadVideoViewListener, listener: IAdRewardVideoListener?
    )

    /**
     * 全屏视频
     */
    fun loadFullVideoAd(activity: Activity, adIdKey: String, listener: IAdFullVideoListener?)

    /**
     * 预加载全屏视频
     */
    fun preloadFullVideoAd(
        activity: Activity, adIdKey: String,
        viewListener: IAdPreloadVideoViewListener?, listener: IAdFullVideoListener?
    )

    /**
     * 信息流广告 (自渲染，需要自己渲染处理显示)
     */
    fun loadFeedNativeAd(context: Context, adIdKey: String, listener: IAdNativeListener?)

    /**
     * 模板信息流 (框架渲染，无需自己处理显示)
     */
    fun loadFeedNativeExpressAd(context: Context, adIdKey: String, listener: IAdNativeExpressListener?)
}
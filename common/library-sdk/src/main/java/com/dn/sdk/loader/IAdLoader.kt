package com.dn.sdk.loader

import android.app.Activity
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.bean.preload.PreloadSplashAd
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
 * 广告加载类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:42
 */
interface IAdLoader {

    /**
     * 当前loader使用的SDK Type
     *
     * @return 广告类型
     */
    fun getSdkType(): SdkType

    /**
     * 加载和显示开屏广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdSplashListener 广告请求回调
     */
    fun loadAndShowSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?)

    /**
     * 预加载 开屏广告对象
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdSplashListener? 广告请求回调
     * @return PreloadSplashAd 预加载广告对象,在合适的时候调用show()方法显示广告
     */
    fun preloadSplashAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ): PreloadSplashAd

    /**
     * 加载和现实开屏广告。但是走Gromore渠道
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdSplashListener?
     */
    fun loadAdShowSplashAdV2(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?)

    /**
     * 预加载开屏广告，但是走Gromore渠道
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdSplashListener?
     * @return PreloadSplashAd
     */
    fun preloadSplashAdV2(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?
    ): PreloadSplashAd

    /**
     * 加载和显示Banner广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdBannerListener? 广告回调
     */
    fun loadAndShowBannerAd(activity: Activity, adRequest: AdRequest, listener: IAdBannerListener?)

    /**
     * 加载和显示插屏广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdInterstitialListener? 广告回调
     */
    fun loadAndShowInterstitialAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdInterstitialListener?
    )

    /**
     * 预加载插屏广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdInterstitialListener? 广告回调
     * @return PreloadInterstitialAd 预加载对象
     */
    fun preloadInterstitialAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdInterstitialListener?
    ): PreloadInterstitialAd

    /**
     * 架子啊显示插全屏广告
     * @param activity Activity
     * @param adRequest AdRequest
     * @param listener IAdInterstitialFullScreenListener?
     */
    fun loadAndShowInterstitiaScreenFulllAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdInterstitialFullScreenListener?
    )

    /**
     * 加载和自动播放激励视频广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdRewardVideoListener? 广告回调
     */
    fun loadAndShowRewardVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdRewardVideoListener?
    )

    /**
     * 预加载激励视频广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdRewardVideoListener? 广告回调
     * @return PreloadRewardVideoAd 预加载的激励视频对象，合适时候调用 show()方法展示广告
     */
    fun preloadRewardVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdRewardVideoListener?
    ): PreloadRewardVideoAd


    /**
     * 加载信息流模板广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdNativeTemplateListener? 广告回调
     */
    fun loadFeedTemplateAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFeedTemplateListener?
    )

    /**
     * 请求信息流自渲染广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdNativeLoadListener? 广告回调
     */
    fun loadFeedAd(activity: Activity, adRequest: AdRequest, listener: IAdFeedLoadListener?)

    /**
     * 请求draw模板广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdNativeLoadListener? 广告回调
     */
    fun loadDrawTemplateAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdDrawTemplateLoadListener?
    )

    /**
     * 请求draw自定义广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdNativeDrawLoadListener? 广告回调
     */
    fun loadDrawAd(activity: Activity, adRequest: AdRequest, listener: IAdDrawNativeLoadListener?)

    /**
     * 请求全屏广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdFullScreenLoadListener? 广告回调
     */
    fun loadFullScreenVideoAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdFullScreenVideoLoadListener?
    )

    /**
     * 请求穿山甲开屏广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdSplashListener? 广告回调
     */
    fun loadCsjSplashAd(
            activity: Activity,
            adRequest: AdRequest,
            listener: IAdSplashListener?)
}

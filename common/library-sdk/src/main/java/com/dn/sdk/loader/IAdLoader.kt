package com.dn.sdk.loader

import android.app.Activity
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.*
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.bean.preload.PreloadSplashAd


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
    fun preloadSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?): PreloadSplashAd

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
    fun loadAndShowInterstitialAd(activity: Activity, adRequest: AdRequest, listener: IAdInterstitialListener?)

    /**
     * 加载和自动播放激励视频广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdRewardVideoListener? 广告回调
     */
    fun loadAndShowRewardVideoAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?)

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
    fun loadNativeTemplateAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeTemplateListener?)

    /**
     * 请求信息流自渲染广告
     * @param activity Activity 上下文对象
     * @param adRequest AdRequest 广告请求参数
     * @param listener IAdNativeLoadListener? 广告回调
     */
    fun loadNativeAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeLoadListener?)
}

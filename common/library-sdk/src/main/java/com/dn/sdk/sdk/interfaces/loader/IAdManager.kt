package com.dn.sdk.sdk.interfaces.loader

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.dn.sdk.sdk.interfaces.listener.*
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener

/**
 * 广告loader，外部广告调用
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:09
 */
interface IAdManager {

    /** 加载无效用户激励 */
    fun loadInvalidRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?)

    /** 预加载无效用户激励视频 */
    fun preloadInvalidRewardVideoAd(
        activity: Activity,
        viewListener: IAdPreloadVideoViewListener,
        listener: IAdRewardVideoListener?
    )

    /** 加载激励视频 */
    fun loadRewardVideoAd(activity: Activity, listener: IAdRewardVideoListener?)

    /** 预加载无激励视频 */
    fun preloadRewardVideoAd(
        activity: Activity,
        viewListener: IAdPreloadVideoViewListener,
        listener: IAdRewardVideoListener?
    )


    /** 加载全屏启动页广告 */
    fun loadFullScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?)

    /** 加载半屏启动页广告 */
    fun loadHalfScreenSplashAd(activity: Activity, container: ViewGroup, listener: IAdSplashListener?)

    /** 加载插屏广告 */
    fun loadInterstitialAd(activity: Activity, listener: IAdInterstitialListener?)
}
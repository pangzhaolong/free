package com.dn.sdk.platform.donews;

import android.app.Activity
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.listener.*
import com.dn.sdk.loader.IAdLoader
import com.dn.sdk.loader.SdkType
import com.dn.sdk.platform.donews.helper.*

/**
 * 多牛聚合sdk 加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 15:24
 */
class DoNewsAdLoader : IAdLoader {
    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun loadAndShowSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        DoNewsSplashLoadHelper.loadAndShowAd(activity, adRequest, listener)
    }

    override fun preloadSplashAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ): PreloadSplashAd {
        return DoNewsSplashLoadHelper.preloadAd(activity, adRequest, listener)
    }

    override fun loadAndShowBannerAd(activity: Activity, adRequest: AdRequest, listener: IAdBannerListener?) {
        DoNewsBannerLoadHelper.loadAndShowAd(activity, adRequest, listener)
    }

    override fun loadAndShowInterstitialAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ) {
        DoNewsInterstitialLoadHelper.loadAndShowAd(activity, adRequest, listener)
    }

    override fun loadAndShowRewardVideoAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?) {
        DoNewsRewardVideoLoadHelper.loadAndShowAd(activity, adRequest, listener)
    }

    override fun preloadRewardVideoAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdRewardVideoListener?
    ): PreloadRewardVideoAd {
        return DoNewsRewardVideoLoadHelper.preloadAd(activity, adRequest, listener)
    }

    override fun loadNativeTemplateAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeTemplateListener?) {
        DoNewsNativeTemplateLoadHelper.loadTemplateAd(activity, adRequest, listener)
    }

    override fun loadNativeAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeLoadListener?) {
        DoNewsNativeLoadHelper.loadNativeAd(activity, adRequest, listener)
    }

}

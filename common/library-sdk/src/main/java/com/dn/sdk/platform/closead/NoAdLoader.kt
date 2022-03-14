package com.dn.sdk.platform.closead


import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.listener.*
import com.dn.sdk.loader.IAdLoader
import com.dn.sdk.loader.SdkType
import com.dn.sdk.platform.closead.preload.NoAdPreloadRewardVideo
import com.dn.sdk.platform.closead.preload.NoAdPreloadSplashAd
import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.bean.preload.PreloadSplashAd


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

    override fun loadAndShowSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        listener?.onAdStartLoad()
        listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun preloadSplashAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdSplashListener?
    ): PreloadSplashAd {
        listener?.onAdStartLoad()
        val preloadSplashAd = NoAdPreloadSplashAd()
        preloadSplashAd.setLoadState(PreloadAdState.Error)
        //延迟执行，防止错误先执行，然后才返回预加载对象
        DelayExecutor.delayExec(500) {
            listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
        }
        return preloadSplashAd
    }

    override fun loadAndShowBannerAd(activity: Activity, adRequest: AdRequest, listener: IAdBannerListener?) {
        listener?.onAdStartLoad()
        listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadAndShowInterstitialAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialListener?
    ) {
        listener?.onAdStartLoad()
        listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadAndShowRewardVideoAd(activity: Activity, adRequest: AdRequest, listener: IAdRewardVideoListener?) {
        listener?.onAdStartLoad()
        listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun preloadRewardVideoAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdRewardVideoListener?
    ): PreloadRewardVideoAd {
        listener?.onAdStartLoad()
        val preloadRewardVideoAd = NoAdPreloadRewardVideo()
        preloadRewardVideoAd.setLoadState(PreloadAdState.Error)
        DelayExecutor.delayExec(500) {
            listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
        }
        return preloadRewardVideoAd
    }

    override fun loadNativeTemplateAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeTemplateListener?) {
        listener?.onAdStartLoad()
        listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }

    override fun loadNativeAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeLoadListener?) {
        listener?.onAdStartLoad()
        listener?.onAdError(AdCustomError.CloseAd.code, AdCustomError.CloseAd.errorMsg)
    }
}

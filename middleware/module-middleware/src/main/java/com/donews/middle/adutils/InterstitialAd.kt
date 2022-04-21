package com.donews.middle.adutils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.interstitial.SimpleInterstitialListener
import com.donews.middle.utils.HotStartCacheUtils
import com.donews.yfsdk.loader.AdManager.loadInterstitialAd
import com.donews.yfsdk.manager.AdConfigManager
import com.donews.yfsdk.preload.AdInterstitialCacheUtils

/**
 *
 * 插屏工具类
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object InterstitialAd {

    fun cacheAd(activity: AppCompatActivity) {
        if (AdConfigManager.mNormalAdBean.interstitial.usePreload) {
            AdInterstitialCacheUtils.cache(activity)
        }
    }

    fun showAd(activity: Activity?, listener: SimpleInterstitialListener?) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        if (HotStartCacheUtils.isShowing()) {
            return
        }

        if (AdConfigManager.mNormalAdBean.interstitial.usePreload) {
            AdInterstitialCacheUtils.showAd(activity, listener)
        } else {
            loadInterstitialAd(activity, listener)
        }
    }
}
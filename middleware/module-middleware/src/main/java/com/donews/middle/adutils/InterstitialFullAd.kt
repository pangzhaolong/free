package com.donews.middle.adutils

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.interstitial.SimpleInterstitialFullListener
import com.donews.yfsdk.loader.AdManager
import com.donews.yfsdk.monitor.InterstitialFullAdCheck

/**
 *
 * 插全屏工具类
 * @author dw
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object InterstitialFullAd {

    fun showAd(activity: Activity?, listener: SimpleInterstitialFullListener?) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)
	
        AdManager.loadAndShowInterstitialFullAd(activity, listener)
    }
}
package com.donews.middle.adutils

import android.app.Activity
import android.view.ViewGroup
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.splash.IAdSplashListener
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.yfsdk.loader.AdManager

/**
 *
 * Splash Ad 工具类
 * @author dw
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object SplashAd {

    fun loadSplashAd(activity: Activity?, hotStart: Boolean, container: ViewGroup, listener: IAdSplashListener?, isHalfScreen: Boolean) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadSplashAd(activity, hotStart, container, listener, isHalfScreen)
    }

    fun loadCsjSplashAd(activity: Activity?, hotStart: Boolean, container: ViewGroup, listener: IAdSplashListener?, isHalfScreen: Boolean) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadCsjSplashAd(activity, hotStart, container, isHalfScreen, listener)
    }
}
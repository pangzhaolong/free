package com.donews.middle.adutils

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.interstitialfull.SimpleInterstitialFullListener
import com.donews.middle.abswitch.ABSwitch
import com.donews.yfsdk.loader.AdManager
import java.lang.Exception

/**
 *
 * 插全屏工具类
 * @author dw
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object InterstitialFullAd {

    fun showAd(activity: Activity?, listener: SimpleInterstitialFullListener?) {
        //ab包打开的话。关闭广告
        try {
            if (ABSwitch.Ins().isOpenAB) {
                listener?.onAdError(
                    AdCustomError.CloseAdAll.code,
                    AdCustomError.CloseAdAll.errorMsg
                )
                return
            }
        } catch (e: Exception) {
            listener?.onAdError(
                AdCustomError.CloseAdAll.code,
                AdCustomError.CloseAdAll.errorMsg
            )
            return
        }
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)
	
        AdManager.loadAndShowInterstitialFullAd(activity, listener)
    }
}
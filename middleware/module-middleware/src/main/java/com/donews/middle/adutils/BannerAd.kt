package com.donews.middle.adutils

import android.app.Activity
import android.view.ViewGroup
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.banner.IAdBannerListener
import com.donews.yfsdk.loader.AdManager

/**
 *
 * Banner工具类
 * @author dw
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object BannerAd {

    fun loadAndShowBannerAd(activity: Activity?, adContainer: ViewGroup,
                            widthDp: Float, heightDp: Float, listener: IAdBannerListener?) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadAndShowBannerAd(activity, adContainer, widthDp, heightDp, listener)
    }
}
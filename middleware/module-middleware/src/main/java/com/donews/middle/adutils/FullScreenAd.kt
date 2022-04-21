package com.donews.middle.adutils

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.fullscreenvideo.IAdFullScreenVideoLoadListener
import com.donews.yfsdk.check.FullScreenAdCheck
import com.donews.yfsdk.loader.AdManager

/**
 *
 * 全屏视频广告对外接口
 * @author dw
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object FullScreenAd {

    fun showAd(activity: Activity?, listener: IAdFullScreenVideoLoadListener?) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)


        AdManager.loadFullScreenVideoAd(activity, listener)
    }
}
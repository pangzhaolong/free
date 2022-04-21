package com.donews.middle.adutils

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.feed.template.IAdFeedTemplateListener
import com.donews.yfsdk.loader.AdManager

object FeedNativeAndTemplateAd {

    fun loadFeedTemplateAd(activity: Activity?, widthDp: Float, heightDp: Float, listener: IAdFeedTemplateListener?) {
        if (activity == null || activity.isFinishing) {
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)

        AdManager.loadFeedTemplateAd(activity, widthDp, heightDp, listener)
    }
}
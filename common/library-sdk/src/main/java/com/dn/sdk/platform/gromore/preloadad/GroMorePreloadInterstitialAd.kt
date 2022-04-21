package com.dn.sdk.platform.gromore.preloadad

import android.app.Activity
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAd
import com.bytedance.sdk.openadsdk.TTAdNative
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.loader.SdkType

/**
 * GroMore预加载插屏广告
 *
 * @author lcl
 * @version v1.0
 * @date 2022/3/8
 */
class GroMorePreloadInterstitialAd(private val ad: GMInterstitialAd, val activity: Activity) : PreloadInterstitialAd() {

    override fun getSdkType(): SdkType {
        return SdkType.GRO_MORE
    }

    override fun realShowAd(activity: Activity) {
        ad.showAd(activity)
    }

    override fun realShowAd() {
        ad.showAd(activity)
    }

    override fun realDestroy() {
        ad.destroy()
    }
}
package com.dn.sdk.platform.donews.preloadad

import android.app.Activity
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAd
import com.bytedance.sdk.openadsdk.TTAdNative
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.loader.SdkType
import com.donews.ads.mediation.v2.api.DoNewsAdNative

/**
 * DoNews 预加载插屏广告
 *
 * @author lcl
 * @version v1.0
 * @date 2022/3/8
 */
class DoNewsPreloadInterstitialAd(private val ad: DoNewsAdNative, val activity: Activity) :
    PreloadInterstitialAd() {

    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun realShowAd(activity: Activity) {
        ad.showInterstitial()
    }

    override fun realShowAd() {
        ad.showInterstitial()
    }

    override fun realDestroy() {
        ad.destroy()
    }
}
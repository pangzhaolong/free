package com.dn.sdk.platform.gromore.preloadad

import android.view.ViewGroup
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.loader.SdkType

/**
 *
 *
 * @author lcl
 * @version v1.0
 * @date 2022/3/17
 */
class GroMorePreloadSplashAd(private val ttSplashAd: GMSplashAd, val container: ViewGroup?) : PreloadSplashAd() {
    override fun realShowAd(container: ViewGroup) {
        ttSplashAd.showAd(container)
    }

    override fun realShowAd() {
        if (container != null) {
            ttSplashAd.showAd(container)
        }
    }

    override fun getSdkType(): SdkType {
        return SdkType.GRO_MORE
    }

    override fun realDestroy() {
        ttSplashAd.destroy()
    }
}
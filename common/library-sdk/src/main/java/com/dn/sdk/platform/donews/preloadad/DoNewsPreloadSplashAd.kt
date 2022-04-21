package com.dn.sdk.platform.donews.preloadad

import android.view.ViewGroup
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.loader.SdkType
import com.donews.ads.mediation.v2.api.DoNewsAdNative

/**
 * 多牛预加载开屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 15:14
 */
class DoNewsPreloadSplashAd(private val doNewsAdNative: DoNewsAdNative) : PreloadSplashAd() {
    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun realShowAd(container: ViewGroup) {
        doNewsAdNative.showSplash()
    }


    override fun realShowAd() {
        doNewsAdNative.showSplash()
    }

    override fun realDestroy() {
        doNewsAdNative.destroy()
    }
}
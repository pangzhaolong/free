package com.dn.sdk.platform.donews.preloadad

import com.dn.sdk.bean.preload.PreloadRewardVideoAd
import com.dn.sdk.loader.SdkType
import com.donews.ads.mediation.v2.api.DoNewsAdNative

/**
 * 多牛预加载对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:41
 */
class DoNewsPreloadRewardVideoAd(private val doNewsAdNative: DoNewsAdNative) : PreloadRewardVideoAd() {
    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun realShowAd() {
        doNewsAdNative.showRewardAd()
    }

    override fun realDestroy() {
        doNewsAdNative.destroy()
    }
}
package com.donews.yfsdk.loader

import com.dn.sdk.bean.preload.PreloadInterstitialAd

/**
 * 预加载插屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/14 16:28
 */
interface IPreloadInterstitialAd {

    fun preload(ad: PreloadInterstitialAd)
}
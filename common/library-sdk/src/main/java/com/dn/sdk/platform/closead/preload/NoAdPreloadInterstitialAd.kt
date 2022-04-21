package com.dn.sdk.platform.closead.preload

import android.app.Activity
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.loader.SdkType

/**
 *
 *
 * @author lcl
 * @version v1.0
 * @date 2022/3/8
 */
class NoAdPreloadInterstitialAd: PreloadInterstitialAd() {
    override fun realShowAd(activity: Activity) {

    }

    override fun realShowAd() {

    }

    override fun getSdkType(): SdkType {
        return SdkType.DO_NEWS
    }

    override fun realDestroy() {
    }
}
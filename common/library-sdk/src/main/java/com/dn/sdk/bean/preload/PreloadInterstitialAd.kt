package com.dn.sdk.bean.preload

import android.app.Activity
import com.dn.sdk.bean.AdType

/**
 *
 *
 * @author lcl
 * @version v1.0
 * @date 2022/2/14 16:04
 */
abstract class PreloadInterstitialAd : PreloadAd() {
    override fun getAdType(): AdType {
        return AdType.INTERSTITIAL
    }

    abstract fun realShowAd(activity: Activity)
}
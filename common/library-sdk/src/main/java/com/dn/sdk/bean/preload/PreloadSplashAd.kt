package com.dn.sdk.bean.preload

import android.view.ViewGroup
import com.dn.sdk.bean.AdType

/**
 * 预加载的启屏页广告对象
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/2 14:41
 */
abstract class PreloadSplashAd : PreloadAd() {

    override fun getAdType(): AdType {
        return AdType.SPLASH
    }

    abstract fun realShowAd(container: ViewGroup)
}
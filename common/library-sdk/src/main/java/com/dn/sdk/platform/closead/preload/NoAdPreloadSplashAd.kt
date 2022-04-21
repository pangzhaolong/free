package com.dn.sdk.platform.closead.preload

import android.view.ViewGroup
import com.dn.sdk.loader.SdkType
import com.dn.sdk.bean.preload.PreloadSplashAd

/**
 * 无广告的预加载起屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 14:08
 */
class NoAdPreloadSplashAd : PreloadSplashAd() {
    override fun getSdkType(): SdkType {
        return SdkType.CLOSE_AD
    }

    override fun realShowAd(container: ViewGroup) {
    }

    override fun realShowAd() {

    }

    override fun realDestroy() {

    }
}
package com.dn.sdk.platform.closead.preload

import com.dn.sdk.loader.SdkType
import com.dn.sdk.bean.preload.PreloadRewardVideoAd

/**
 * 无广告预加载激励视频
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 14:09
 */
class NoAdPreloadRewardVideo : PreloadRewardVideoAd() {
    override fun getSdkType(): SdkType {
        return SdkType.CLOSE_AD
    }

    override fun realShowAd() {

    }

    override fun realDestroy() {

    }
}
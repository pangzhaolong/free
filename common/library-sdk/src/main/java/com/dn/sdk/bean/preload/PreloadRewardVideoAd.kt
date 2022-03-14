package com.dn.sdk.bean.preload

import com.dn.sdk.bean.AdType

/**
 * 预加载激励视频基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:30
 */
abstract class PreloadRewardVideoAd : PreloadAd() {

    override fun getAdType(): AdType {
        return AdType.REWARD_VIDEO
    }
}
package com.dn.sdk.bean.preload

import com.dn.sdk.bean.AdType

/**
 * 预加载全屏视频基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:37
 */
abstract class PreloadFullVideoAd : PreloadAd() {
    override fun getAdType(): AdType {
        return AdType.FULL_SCREEN_VIDEO
    }
}
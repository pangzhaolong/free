package com.donews.common.ad.cache

import com.alibaba.android.arouter.launcher.ARouter
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener
import com.donews.common.ad.cache.activity.OnePixelVideoCacheActivity
import com.donews.common.router.RouterActivityPath

/**
 * 广告视频缓存类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/24 9:51
 */
object AdVideoCacheUtils {

    fun startCache() {
        ARouter.getInstance().build(RouterActivityPath.Ad.AD_ONE_PX_CACHE_ACTIVITY)
            .withBoolean("startCache", true)
            .withInt("videoType", 1)
            .navigation()
    }

    fun playRewardVideo(listener: IAdRewardVideoListener) {
        OnePixelVideoCacheActivity.rewardVideoListener = listener
        ARouter.getInstance().build(RouterActivityPath.Ad.AD_ONE_PX_CACHE_ACTIVITY)
            .withBoolean("startCache", false)
            .withInt("videoType", 1)
            .navigation()
    }

}
package com.donews.middle.adutils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.yfsdk.loader.AdManager
import com.donews.yfsdk.preload.RewardVideoAdCache
import com.donews.yfsdk.proxy.RewardAdVideoListenerProxy

/**
 *
 * 激励视频工具类
 * @author dw
 * @version v1.0
 * @date 2021/11/30 18:31
 */
object RewardVideoAd {

    fun preloadAd(activity: AppCompatActivity?, needReportEcpmWhenReward: Boolean) {
        if (activity == null || activity.isFinishing) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ContextError.code},${AdCustomError.ContextError.errorMsg})")
            return
        }

        DnSdkInit.initBeforeLoadAd(activity.application)
        //预加载一个激励视频
//        AdVideoCacheUtils.cacheRewardVideo(activity)
        RewardVideoAdCache.cacheRewardVideo(activity, needReportEcpmWhenReward)
    }

    fun showPreloadRewardVideo(activity: Activity?, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean = false) {
        if (activity == null || activity.isFinishing) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ContextError.code},${AdCustomError.ContextError.errorMsg})")
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        //展示预加载视频
//        AdVideoCacheUtils.showRewardVideo(activity, listener)
//        AdVideoCacheUtils.showRewardVideo(activity, RewardAdVideoListenerProxy(activity, listener, needReportEcpmWhenReward))
//
        RewardVideoAdCache.showRewardVideo(activity, listener, needReportEcpmWhenReward)
    }

    fun loadRewardVideoAd(activity: Activity?, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean = false) {
        if (activity == null || activity.isFinishing) {
            AdLoggerUtils.d("onAdError(${AdCustomError.ContextError.code},${AdCustomError.ContextError.errorMsg})")
            listener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        //实时加载并展示视频
        AdManager.loadRewardVideoAd(activity, RewardAdVideoListenerProxy(activity, listener, needReportEcpmWhenReward))
    }
}
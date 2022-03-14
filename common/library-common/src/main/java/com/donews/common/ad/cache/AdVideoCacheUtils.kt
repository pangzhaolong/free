package com.donews.common.ad.cache

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.manager.config.IAdConfigInitListener
import com.donews.base.utils.ext.isToday
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.donews.common.ad.business.manager.JddAdManager
import com.donews.common.ad.business.monitor.RewardVideoCount

/**
 * 广告视频缓存类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/24 9:51
 */
@SuppressLint("StaticFieldLeak")
object AdVideoCacheUtils {

    private var mActivity: AppCompatActivity? = null

    private var mRewardVideoListener: IAdRewardVideoListener? = null

    private var mServiceEcpmLimitTime: Long = 0L

    private var mCurrentPreload: PreloadRewardVideoInfo? = null

    private var mNextPreload: PreloadRewardVideoInfo? = null


    fun cacheRewardVideo(activity: AppCompatActivity) {
        mActivity = activity
        //如果走 onDestroy，则置空mActivity ，防止内存泄漏
        mActivity?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    mActivity?.lifecycle?.removeObserver(this)
                    mActivity = null
                }
            }
        })
        preload()
    }

    fun showRewardVideo(rewardVideoListener: IAdRewardVideoListener?) {
        mCurrentPreload = mNextPreload
        mRewardVideoListener = rewardVideoListener
        if (mCurrentPreload == null) {
            rewardVideoListener?.onAdError(
                AdCustomError.PreloadAdStatusError.code,
                AdCustomError.PreloadAdStatusError.errorMsg
            )
            return
        } else {
            mCurrentPreload!!.showAd(rewardVideoListener)
        }
    }


    private fun preload() {
        JddAdConfigManager.addListener {
            val jddAdConfigBean = JddAdManager.mAdIdConfigBean
            if (jddAdConfigBean.rewardVideoId.isBlank() && jddAdConfigBean.invalidRewardVideoId.isBlank()) {
                //判断Ecpm 限制导致的 id为 null，则不需要再触发预加载
                mServiceEcpmLimitTime = System.currentTimeMillis()
            }

            if (!RewardVideoCount.checkShouldLoadAd() || mServiceEcpmLimitTime.isToday()) {
                callLimitError()
            } else {
                preloadRewardVideoInfo()
            }
        }
    }


    /** 刷新接口后再预加载 */
    private fun refreshIdAndPreload() {
        if (JddAdManager.mAdConfigBean.userLevelStrategy) {
            JddAdManager.resetInit()
            JddAdManager.addInitListener(object : IAdConfigInitListener {
                override fun initSuccess() {
                    preload()
                }
            })
            JddAdManager.refreshAdIdConfig()
        } else {
            preload()
        }
    }

    private fun callLimitError() {
        mRewardVideoListener?.onAdError(
            AdCustomError.LimitAdError.code,
            AdCustomError.LimitAdError.errorMsg
        )
    }


    private fun preloadRewardVideoInfo() {
        val info = PreloadRewardVideoInfo(mActivity, false)
        info.setPreloadNext({
            preload()
        }, {
            refreshIdAndPreload()
        })
        info.preloadAd()
        mNextPreload = info
    }
}
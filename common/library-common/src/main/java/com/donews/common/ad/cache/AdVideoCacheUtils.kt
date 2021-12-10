package com.donews.common.ad.cache

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadAd
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.manager.config.IAdConfigInitListener
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.loader.IPreloadAdListener
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.donews.common.ad.business.manager.JddAdManager
import com.donews.common.ad.business.monitor.LotteryAdCount
import com.orhanobut.logger.Logger

/**
 * 广告视频缓存类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/24 9:51
 */
object AdVideoCacheUtils {


    /** 是否打印日志 */
    private const val logger: Boolean = true

    /** 日志tag */
    private const val TAG = "RewardVideoPreload"

    /** 最大重试次数 */
    private const val MAX_RETRY_NUMBER = 3

    /** 超时时间 */
    private const val TINE_OUT = 20000


    private var mActivity: AppCompatActivity? = null
    private var mPreloadVideoView: PreloadAd? = null

    /** 是否正在预加载 */
    private var mPreLoading: Boolean = false
    private var mPreLoadSuccess: Boolean = false

    /** 用于标记是否直接拨付，用于可能还没预加载完成就调用播放功能 */
    private var mNeedShow: Boolean = false

    /** 当前预加载次数 */
    private var mRetry: Int = 0

    private var mStartPreloadTime: Long = 0

    private var mRewardVideoListener: IAdRewardVideoListener? = null

    fun cacheRewardVideo(activity: AppCompatActivity) {
        mActivity = activity
        //如果走 onDestroy，则置空mActivity ，防止内存泄漏
        mActivity?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    tag("预加载激励视频错误-----mActivity执行了OnDestroy，目标被销毁")
                    mActivity?.lifecycle?.removeObserver(this)
                    mActivity = null
                }
            }
        })
        preload()
    }

    fun showRewardVideo(rewardVideoListener: IAdRewardVideoListener?) {
        mRewardVideoListener = rewardVideoListener
        tag("调用了展示激励视频广告-------showRewardVideo（）")
        if (mPreloadVideoView == null) {
            //可能正在加载，还没有返回
            if (mPreLoading) {
                mNeedShow = true
                tag("调用了展示激励视频广告,当前预加载对象为null,但在预加载状态中")
            } else {
                mRetry = 0
                mNeedShow = true
                preload()
                tag("调用了展示激励视频广告,当前预加载对象为null,且不在预加载中,重置错误次数,重新启用预加载")
            }
        } else {
            if (mPreLoadSuccess) {
                if (mPreloadVideoView?.getLoadState() == PreloadAdState.Shown) {
                    mPreloadVideoView = null
                    showRewardVideo(rewardVideoListener)
                } else {
                    mPreloadVideoView?.showAd()
                }
                return
            }
            if (!mPreLoading) {
                mRetry = 0
                mNeedShow = true
                mPreloadVideoView = null
                preload()
                tag("不在加载状态，并且没有加载成功，则重新预加载")
            } else {
                val duration = System.currentTimeMillis() - mStartPreloadTime
                if (duration >= TINE_OUT) {
                    //直接返回错误，并且重新预加载一个视频
                    mRewardVideoListener?.onAdError(
                            AdCustomError.PreloadTimesError.code,
                            AdCustomError.PreloadTimesError.errorMsg
                    )
                    mRewardVideoListener = null
                    mRetry = 0
                    mNeedShow = false
                    mPreloadVideoView = null
                    preload()
                    tag("在加载状态,$duration 预加载时间过长。")
                } else {
                    tag("在加载状态,时间过短,$duration 可以等待。")
                    mPreloadVideoView?.showAd()
                }
            }
        }
    }


    private fun preload() {
        JddAdConfigManager.addListener {
            val number = LotteryAdCount.getTodayLotteryCount()
            val settingNumber = JddAdConfigManager.jddAdConfigBean.useInvalidRewardVideoIdWhenLotteryNumber
            preloadRewardVideo(false)
        }
    }

    private fun preloadRewardVideo(invalid: Boolean) {
        if (mActivity == null) {
            tag("预加载激励视频错误-----mActivity为null")
            mPreLoading = false
            mPreLoadSuccess = false
            return
        }
        val mRealActivity = mActivity!!
        mPreLoading = true
        mPreLoadSuccess = false
        tag("预加载激励视频开始-----")

        val preloadViewListener: IPreloadAdListener = object : IPreloadAdListener {
            override fun preloadAd(ad: PreloadAd) {
                mPreloadVideoView = ad
                if (mNeedShow) {
                    mPreloadVideoView?.showAd()
                    mNeedShow = false
                }
            }
        }

        val adRewardVideoListener: IAdRewardVideoListener = object : IAdRewardVideoListener {

            override fun onAdStatus(code: Int, any: Any?) {

            }

            override fun onAdLoad() {
                //加载成功,则重置加载次数
                mRewardVideoListener?.onAdLoad()
                tag("预加载激励视频成功------onAdLoad()")
            }

            override fun onVideoCached() {
                //加载成功,则重置加载次数
                mPreLoading = false
                mPreLoadSuccess = true
                mRetry = 0
                tag("预加载激励视频成功------onVideoCached()")
                mRewardVideoListener?.onVideoCached()
            }

            override fun onAdShow() {
                mRewardVideoListener?.onAdShow()
                tag("预加载激励视频展示------onAdShow()")

            }

            override fun onAdVideoClick() {
                mRewardVideoListener?.onAdVideoClick()
                tag("预加载激励视频被点击-----------onAdVideoClick()")
            }

            override fun onRewardVerify(result: Boolean) {
                mRewardVideoListener?.onRewardVerify(result)
                tag("预加载激励视频获取奖励回调-----------onRewardVerify($result)")
            }

            override fun onAdClose() {
                mRewardVideoListener?.onAdClose()
                mRewardVideoListener = null
                mPreloadVideoView = null
                tag("预加载激励视频关闭-----------onAdClose()")
                //加载下一个激励视频
                refreshIdAndPreload()
            }


            override fun onVideoComplete() {
                mRewardVideoListener?.onVideoComplete()
                tag("预加载激励视频播放完成-----------onVideoComplete()")
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                mPreloadVideoView = null
                if (mPreLoading) {
                    mPreLoading = false
                    mPreLoadSuccess = false
                    if (code != AdCustomError.CloseAd.code) {
                        //如果不是关闭广告，则需要重试继续预加载
                        mRetry++
                        if (mRetry < MAX_RETRY_NUMBER) {
                            tag("预加载激励视频出现错误-----------onError($code,$errorMsg),重新预加载。重试次数$mRetry")
                            preload()
                        } else {
                            tag("预加载激励视频出现错误-----------onError($code,$errorMsg),重新预加载。重试次数$mRetry")
                            mRewardVideoListener?.onAdError(code, errorMsg)
                        }
                    } else {
                        mRewardVideoListener?.onAdError(code, errorMsg)
                    }
                }
            }
        }

        if (!invalid) {
            mStartPreloadTime = System.currentTimeMillis()
            AdManager.preloadRewardVideoAd(mRealActivity, preloadViewListener, adRewardVideoListener)
        } else {
            tag("使用无效激励视频")
            mStartPreloadTime = System.currentTimeMillis()
            AdManager.preloadInvalidRewardVideoAd(mRealActivity, preloadViewListener, adRewardVideoListener)
        }
    }

    private fun tag(msg: String) {
        if (logger) {
            Logger.t(TAG)
                    .d(msg)
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
}
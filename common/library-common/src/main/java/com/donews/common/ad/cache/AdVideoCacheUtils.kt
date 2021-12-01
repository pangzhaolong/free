package com.donews.common.ad.cache

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.sdk.sdk.ErrorConstant
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener
import com.dn.sdk.sdk.interfaces.view.PreloadVideoView
import com.donews.common.ad.business.callback.JddAdConfigManager
import com.donews.common.ad.business.loader.AdManager
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
    private var mPreloadVideoView: PreloadVideoView? = null

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
                mPreloadVideoView?.show()
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
                    mRewardVideoListener?.onError(ErrorConstant.ERROR_LOADING_TIME, "预加载时间过长")
                    mRewardVideoListener = null
                    mRetry = 0
                    mNeedShow = false
                    mPreloadVideoView = null
                    preload()
                    tag("在加载状态,$duration 预加载时间过长。")
                } else {
                    tag("在加载状态,时间过短,$duration 可以等待。")
                    mPreloadVideoView?.show()
                }
            }
        }
    }


    private fun preload() {
        JddAdConfigManager.addListener {
            val number = LotteryAdCount.getTodayLotteryCount()
            val settingNumber = JddAdConfigManager.jddAdConfigBean.useInvalidRewardVideoIdWhenLotteryNumber
            preloadRewardVideo(number > settingNumber)
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

        val preloadViewListener: IAdPreloadVideoViewListener = IAdPreloadVideoViewListener { videoView ->
            mPreloadVideoView = videoView
            if (mNeedShow) {
                mPreloadVideoView?.show()
                mNeedShow = false
            }
        }

        val adRewardVideoListener: IAdRewardVideoListener = object : IAdRewardVideoListener {
            override fun onLoad() {
                //加载成功,则重置加载次数
                mPreLoading = false
                mPreLoadSuccess = true
                mRetry = 0
                tag("预加载激励视频成功------onLoad()")
                mRewardVideoListener?.onLoad()
            }

            override fun onLoadFail(code: Int, error: String?) {
                mPreLoading = false
                mPreLoadSuccess = false
                mPreloadVideoView = null
                mRewardVideoListener?.onLoadFail(code, error)
                tag("预加载激励视频失败---onLoadFail($code,$error)")
            }

            override fun onLoadTimeout() {
                mPreLoading = false
                mPreLoadSuccess = false
                mPreloadVideoView = null
                mRewardVideoListener?.onLoadTimeout()
                tag("预加载激励视频失败---onLoadTimeout()")
            }

            override fun onLoadCached() {
                mRewardVideoListener?.onLoadCached()
                tag("预加载激励视频缓存成功--------onLoadCached（）")
            }

            override fun onRewardAdShow() {
                mRewardVideoListener?.onRewardAdShow()
                tag("预加载激励视频展示----------onRewardAdShow()")
            }

            override fun onRewardBarClick() {
                mRewardVideoListener?.onRewardBarClick()
                tag("预加载激励视频被点击-----------onRewardBarClick()")
            }

            override fun onRewardedClosed() {
                mRewardVideoListener?.onRewardedClosed()
                mRewardVideoListener = null
                mPreloadVideoView = null

                tag("预加载激励视频关闭-----------onRewardedClosed()")

                //加载下一个激励视频
                preload()
            }

            override fun onRewardVideoComplete() {
                mRewardVideoListener?.onRewardVideoComplete()
                tag("预加载激励视频播放完成-----------onRewardVideoComplete()")
            }

            override fun onRewardVideoError() {
                mRewardVideoListener?.onRewardVideoError()
                tag("预加载激励视频错误-----------onRewardVideoError()")
            }

            override fun onRewardVideoAdShowFail(code: Int, message: String?) {
                mRewardVideoListener?.onRewardVideoAdShowFail(code, message)
                tag("预加载激励视频展示错误-----------onRewardVideoAdShowFail($code,$message)")
            }

            override fun onRewardVerify(result: Boolean) {
                mRewardVideoListener?.onRewardVerify(result)
                tag("预加载激励视频获取奖励回调-----------onRewardVerify($result)")
            }

            override fun onSkippedRewardVideo() {
                mRewardVideoListener?.onSkippedRewardVideo()
                tag("预加载激励视频被跳过-----------onSkippedRewardVideo()")
            }

            override fun onError(code: Int, msg: String?) {
                mRewardVideoListener?.onError(code, msg)
                mPreloadVideoView = null
                //这是错误汇总，可能出现预加载成功，但是播放失败出现此问题
                if (mPreLoading) {
                    mPreLoading = false
                    mPreLoadSuccess = false
                    //如果不是云控关闭了广告，则需要重试加载广告,最多3次
                    if (code != ErrorConstant.ERROR_CODE_NO_AD) {
                        mRetry++
                        if (mRetry < MAX_RETRY_NUMBER) {
                            preload()
                        }
                    }
                }
                tag("预加载激励视频出现错误-----------onError($code,$msg)")
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

}
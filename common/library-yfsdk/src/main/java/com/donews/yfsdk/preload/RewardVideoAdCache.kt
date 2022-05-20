package com.donews.yfsdk.preload

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadAd
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.base.utils.ToastUtil
import com.donews.yfsdk.check.RewardVideoCheck
import com.donews.yfsdk.loader.AdManager
import com.donews.yfsdk.loader.IPreloadAdListener
import com.donews.yfsdk.manager.AdConfigManager
import com.donews.yfsdk.proxy.RewardAdVideoListenerProxy

/**
 * 广告视频缓存类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/24 9:51
 */
@SuppressLint("StaticFieldLeak")
object RewardVideoAdCache {

    private var mActivity: AppCompatActivity? = null
    private var mCurrPreloadAd: PreloadAd? = null
    private var mNextPreloadAd: PreloadAd? = null

    private var mPreAdIdIsBlank: Boolean = false

    private var mNeedShowWhenAdIsLoading = false

    private const val PRELOAD_TIME_OUT = 15000

    private var mListener: IAdRewardVideoListener? = null

    /** 预加载状态 */
    private var mStartPreloadTime: Long = 0L

    fun cacheRewardVideo(activity: AppCompatActivity, needReportEcpmWhenReward: Boolean) {
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

        preloadRewardVideo(activity, false, null, needReportEcpmWhenReward)
    }

    fun preloadRewardVideo(activity: Activity?, needShow: Boolean, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean) {
        if (activity == null) {
            mListener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        val preloadViewListener: IPreloadAdListener = object : IPreloadAdListener {
            override fun preloadAd(ad: PreloadAd) {
                mNextPreloadAd = ad
            }
        }

        val adRewardVideoListener: IAdRewardVideoListener = object : IAdRewardVideoListener {

            override fun onAdStartLoad() {
                mListener?.onAdStartLoad()
                AdLoggerUtils.d("预加载激励视频开始------onAdStartLoad()")
            }

            override fun onAdStatus(code: Int, any: Any?) {
                mListener?.onAdStatus(code, any)
                AdLoggerUtils.d("预加载激励视频状态------onAdStatus(${code},${any})")
            }

            override fun onAdLoad() {
                //加载成功,则重置加载次数
                mListener?.onAdLoad()
                AdLoggerUtils.d("预加载激励视频成功------onAdLoad()")
            }

            override fun onVideoCached() {
                //加载成功,则重置加载次数
                AdLoggerUtils.d("预加载激励视频成功------onVideoCached()")
                mListener?.onVideoCached()
                if (needShow || mNeedShowWhenAdIsLoading) {
                    mNextPreloadAd?.showAd()
                    mNeedShowWhenAdIsLoading = false
                }
            }

            override fun onAdShow() {
                mListener?.onAdShow()
                AdLoggerUtils.d("预加载激励视频展示------onAdShow()")
            }

            override fun onAdVideoClick() {
                mListener?.onAdVideoClick()
                AdLoggerUtils.d("预加载激励视频被点击-----------onAdVideoClick()")
            }

            override fun onRewardVerify(result: Boolean) {
                mListener?.onRewardVerify(result)
                AdLoggerUtils.d("预加载激励视频获取奖励回调-----------onRewardVerify($result)")
                preloadRewardVideo(activity, false, listener, needReportEcpmWhenReward)
            }

            override fun onAdClose() {
                mListener?.onAdClose()
                if (needShow) {
                    mNextPreloadAd?.destroy()
                    mNextPreloadAd = null
                } else {
                    mCurrPreloadAd?.destroy()
                    mCurrPreloadAd = null
                }
                AdLoggerUtils.d("预加载激励视频关闭-----------onAdClose()")
            }

            override fun onVideoComplete() {
                mListener?.onVideoComplete()
                AdLoggerUtils.d("预加载激励视频播放完成-----------onVideoComplete()")
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                mNextPreloadAd?.destroy()
                mNextPreloadAd = null
                AdLoggerUtils.d("预加载激励视频出现错误-----------onError($code,$errorMsg)，广告关闭")
                mListener?.onAdError(code, errorMsg)
            }

            override fun addReportEcpmParamsWhenReward(params: EcpmParam) {
                mListener?.addReportEcpmParamsWhenReward(params)
            }

            override fun reportEcpmSuccessWhenReward(response: EcpmResponse) {
                mListener?.reportEcpmSuccessWhenReward(response)
            }

            override fun reportEcpmFailWhenReward() {
                mListener?.reportEcpmFailWhenReward()
            }
        }

        mStartPreloadTime = System.currentTimeMillis()
        AdManager.preloadRewardVideoAd(activity,
                preloadViewListener,
                RewardAdVideoListenerProxy(activity, adRewardVideoListener, needReportEcpmWhenReward))
    }

    fun showRewardVideo(activity: Activity?, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean = false) {
        mListener = listener
        mCurrPreloadAd = mNextPreloadAd
        if (mCurrPreloadAd == null) {
            listener?.onAdError(AdCustomError.PreloadAdStatusError.code, AdCustomError.PreloadAdStatusError.errorMsg)
            preloadRewardVideo(activity, mPreAdIdIsBlank, listener, needReportEcpmWhenReward)
        } else {
            showPreLoadRewardAd(activity, listener, needReportEcpmWhenReward)
        }
        mPreAdIdIsBlank = false
    }

    fun showPreLoadRewardAd(activity: Activity?, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean) {
        mCurrPreloadAd?.let {
            when (it.getLoadState()) {
                PreloadAdState.Success -> {
                    if (activity == null || activity.isFinishing) {
                        return
                    }
                    it.showAd()
                }
                PreloadAdState.Loading -> {
                    val duration = System.currentTimeMillis() - mStartPreloadTime
                    if (duration >= PRELOAD_TIME_OUT) {
                        listener?.onAdError(AdCustomError.PreloadTimesError.code, AdCustomError.PreloadTimesError.errorMsg)
                        mCurrPreloadAd?.destroy()
                        preloadRewardVideo(activity, true, listener, needReportEcpmWhenReward)
                    } else {
                        mNeedShowWhenAdIsLoading = true
                        ToastUtil.show(activity, "视频正在加载，请稍等片刻!")
                    }
                }
                else -> {
                    listener?.onAdError(AdCustomError.PreloadAdStatusError.code, AdCustomError.PreloadAdStatusError.errorMsg)
                    mCurrPreloadAd?.destroy()
                    preloadRewardVideo(activity, true, listener, needReportEcpmWhenReward)
                }
            }
        }
    }

}
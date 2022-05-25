package com.donews.yfsdk.preload

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadAd
import com.dn.sdk.listener.IPreloadAdListener
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.base.utils.ToastUtil
import com.donews.yfsdk.loader.AdManager
import com.donews.yfsdk.proxy.RewardAdVideoListenerProxy
import java.lang.ref.WeakReference

/**
 * 广告视频缓存类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/24 9:51
 */
@SuppressLint("StaticFieldLeak")
object RewardVideoAdCache {

    private const val MESSAGE_ID: Int = 100003
    private const val MESSAGE_ID_BACK_UP: Int = 100004

    private var mActivity: AppCompatActivity? = null
    private var mCurrPreloadAd: PreloadAd? = null
    private var mNextPreloadAd: PreloadAd? = null

    private var mPreAdIdIsBlank: Boolean = false

    private var mNeedShowWhenAdIsLoading = false

    private var mIsAdShowing: Boolean = false
    private var mRetryCount: Int = 0

    private const val PRELOAD_TIME_OUT = 15000

    private const val DEFAULT: Int = -1;
    private const val DN: Int = 0
    private const val GM: Int = 1
    private var mCurPlayAdType: Int = DN;                   //0: Dn聚合广告；1：GroMore兜底广告
    private var mIsAdCached = false
    private var mCurPreloadAdType: Int = DN

    private var mHandler: MyHandler? = MyHandler(this)

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

        preloadRewardVideo(activity, false, null, needReportEcpmWhenReward, false)
    }

    fun preloadRV() {
        mActivity?.runOnUiThread(Runnable {
            preloadRewardVideo(mActivity, false, null, false, false)
        })
    }

    fun preloadGroMore() {
        mActivity?.runOnUiThread(Runnable {
            preloadRewardVideo(mActivity, false, null, false, true)
        })
    }

    private class MyHandler constructor(context: RewardVideoAdCache?) : Handler(Looper.myLooper()!!) {
        private var reference: WeakReference<RewardVideoAdCache?> = WeakReference<RewardVideoAdCache?>(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MESSAGE_ID -> if (reference.get() != null) {
                    reference.get()!!.preloadRV()
                }
                MESSAGE_ID_BACK_UP -> if (reference.get() != null) {
                    reference.get()!!.preloadGroMore()
                }
            }
        }
    }

    var rewardAdVideoListenerProxy: RewardAdVideoListenerProxy? = null
    fun preloadRewardVideo(activity: Activity?, needShow: Boolean, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean, uesGroMoreProtect: Boolean) {
        if (mIsAdCached || mCurPlayAdType == GM) {
            return
        }

        if (rewardAdVideoListenerProxy == null) {
            rewardAdVideoListenerProxy = RewardAdVideoListenerProxy(activity, null, needReportEcpmWhenReward)
        }
        if (listener != null) {
            rewardAdVideoListenerProxy?.listener = listener
        }
        rewardAdVideoListenerProxy?.activity = activity
        rewardAdVideoListenerProxy?.needReportEcpmWhenReward = needReportEcpmWhenReward

        if (activity == null) {
            rewardAdVideoListenerProxy?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }

        val preloadViewListener: IPreloadAdListener = object : IPreloadAdListener {
            override fun onLoadAd(ad: PreloadAd) {
                mNextPreloadAd = ad
            }
        }

        val adRewardVideoListener: IAdRewardVideoListener = object : IAdRewardVideoListener {

            override fun onAdStartLoad() {
                rewardAdVideoListenerProxy?.onAdStartLoad()
                AdLoggerUtils.d("预加载激励视频开始------onAdStartLoad()")
            }

            override fun onAdStatus(code: Int, any: Any?) {
                rewardAdVideoListenerProxy?.onAdStatus(code, any)
                AdLoggerUtils.d("预加载激励视频状态------onAdStatus(${code},${any})")
            }

            override fun onAdLoad() {
                //加载成功,则重置加载次数
                rewardAdVideoListenerProxy?.onAdLoad()
                AdLoggerUtils.d("预加载激励视频成功------onAdLoad()")
            }

            override fun onVideoCached() {
                //加载成功,则重置加载次数
                AdLoggerUtils.d("预加载激励视频成功------onVideoCached()")
                rewardAdVideoListenerProxy?.onVideoCached()
                if (!AdManager.isGroMoreRewardAdReady() && (needShow || mNeedShowWhenAdIsLoading)) {
                    mNextPreloadAd?.showAd()
                    mNeedShowWhenAdIsLoading = false
                }
                mIsAdCached = true;
                mRetryCount = 0
            }

            override fun onAdShow() {
                rewardAdVideoListenerProxy?.onAdShow()
                AdLoggerUtils.d("预加载激励视频展示------onAdShow()")
                mCurrPreloadAd = mNextPreloadAd
                mIsAdShowing = true
                mIsAdCached = false
                mHandler?.removeMessages(MESSAGE_ID)
                mHandler?.sendEmptyMessageDelayed(MESSAGE_ID, 5000)
            }

            override fun onAdVideoClick() {
                rewardAdVideoListenerProxy?.onAdVideoClick()
                AdLoggerUtils.d("预加载激励视频被点击-----------onAdVideoClick()")
            }

            override fun onAdSkipped() {
                rewardAdVideoListenerProxy?.onAdSkipped()
                AdLoggerUtils.d("预加载激励视频被跳过-----------onAdSkipped()")
            }

            override fun onRewardVerify(result: Boolean) {
                rewardAdVideoListenerProxy?.onRewardVerify(result)
                AdLoggerUtils.d("预加载激励视频获取奖励回调-----------onRewardVerify($result)")
            }

            override fun onAdClose() {
                rewardAdVideoListenerProxy?.onAdClose()
                if (needShow) {
                    mCurrPreloadAd?.destroy()
                    mCurrPreloadAd = null
                }
                mIsAdShowing = false
                AdLoggerUtils.d("预加载激励视频关闭-----------onAdClose()")
                if (!mIsAdCached && mCurPlayAdType == GM) {
                    mHandler?.removeMessages(MESSAGE_ID)
                    mHandler?.sendEmptyMessageDelayed(MESSAGE_ID, 200)
                }
                mCurPlayAdType = DEFAULT
            }

            override fun onVideoComplete() {
                rewardAdVideoListenerProxy?.onVideoComplete()
                AdLoggerUtils.d("预加载激励视频播放完成-----------onVideoComplete()")
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                mNextPreloadAd?.destroy()
                mNextPreloadAd = null
                AdLoggerUtils.d("预加载激励视频出现错误-----------onError($code,$errorMsg)，广告关闭")

                if (code != AdCustomError.CloseAdAll.code &&
                        code != AdCustomError.CloseAd.code &&
                        code != AdCustomError.CloseAdOne.code &&
                        code != AdCustomError.ParamsAdIdNullOrBlank.code) {
                    if (!mIsAdCached) {
                        if (mCurPreloadAdType == DN) {
                            mHandler?.removeMessages(MESSAGE_ID_BACK_UP)
                            mHandler?.sendEmptyMessageDelayed(MESSAGE_ID_BACK_UP, 1000)
                        }
                    }
                }

                if (!mIsAdShowing) {
                    rewardAdVideoListenerProxy?.onAdError(code, errorMsg)
                } else {
                    if (mRetryCount >= 0) {
                        return
                    }
                    mRetryCount++
                    mHandler?.removeMessages(MESSAGE_ID_BACK_UP)
                    mHandler?.sendEmptyMessageDelayed(MESSAGE_ID_BACK_UP, 1000)
                }
            }

            override fun addReportEcpmParamsWhenReward(params: EcpmParam) {
                rewardAdVideoListenerProxy?.addReportEcpmParamsWhenReward(params)
            }

            override fun reportEcpmSuccessWhenReward(response: EcpmResponse) {
                rewardAdVideoListenerProxy?.reportEcpmSuccessWhenReward(response)
            }

            override fun reportEcpmFailWhenReward() {
                rewardAdVideoListenerProxy?.reportEcpmFailWhenReward()
            }
        }

        mStartPreloadTime = System.currentTimeMillis()
        if (!uesGroMoreProtect) {
            mCurPreloadAdType = DN
            AdManager.preloadRewardVideoAd(activity, preloadViewListener, adRewardVideoListener)
        } else {
            mCurPreloadAdType = GM
            AdManager.loadGroMoreRewardedAd(activity, adRewardVideoListener)
        }
    }

    fun showRewardVideo(activity: Activity?, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean = false) {
        if (rewardAdVideoListenerProxy == null) {
            rewardAdVideoListenerProxy = RewardAdVideoListenerProxy(activity, null, needReportEcpmWhenReward)
        }

        rewardAdVideoListenerProxy?.activity = activity
        rewardAdVideoListenerProxy?.listener = listener
        rewardAdVideoListenerProxy?.needReportEcpmWhenReward = needReportEcpmWhenReward

        mCurrPreloadAd = mNextPreloadAd
        mCurPlayAdType = DN;
        if (mCurrPreloadAd == null) {
            if (AdManager.isGroMoreRewardAdReady()) {
                if (activity != null) {
                    AdLoggerUtils.d("GroMore保底激励视频展示")
                    AdManager.showGroMoreRewardedAd(activity, null)
                    mCurPlayAdType = GM;
                }
            } else {
                rewardAdVideoListenerProxy?.onAdError(AdCustomError.PreloadAdStatusError.code, AdCustomError.PreloadAdStatusError.errorMsg)
                preloadRewardVideo(activity, mPreAdIdIsBlank, listener, needReportEcpmWhenReward, false)
            }
        } else {
            showPreLoadRewardAd(activity, listener, needReportEcpmWhenReward)
        }
        mPreAdIdIsBlank = false
    }

    fun showPreLoadRewardAd(activity: Activity?, listener: IAdRewardVideoListener?, needReportEcpmWhenReward: Boolean) {
        if (AdManager.isGroMoreRewardAdReady()) {
            AdLoggerUtils.d("GroMore保底激励视频存在，即将展示")
            AdManager.showGroMoreRewardedAd(activity!!, null)
            mCurPlayAdType = GM;
            return
        }
        mCurPlayAdType = DN;
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
                        rewardAdVideoListenerProxy?.onAdError(AdCustomError.PreloadTimesError.code, AdCustomError.PreloadTimesError.errorMsg)
                        mCurrPreloadAd?.destroy()
                        preloadRewardVideo(activity, true, listener, needReportEcpmWhenReward, false)
                    } else {
                        mNeedShowWhenAdIsLoading = true
                        ToastUtil.show(activity, "视频正在加载，请稍等片刻!")
                    }
                }
                else -> {
                    rewardAdVideoListenerProxy?.onAdError(AdCustomError.PreloadAdStatusError.code, AdCustomError.PreloadAdStatusError.errorMsg)
                    mCurrPreloadAd?.destroy()
                    preloadRewardVideo(activity, true, listener, needReportEcpmWhenReward, false)
                }
            }
        }
    }

}
package com.donews.common.ad.cache

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.donews.yfsdk.loader.AdManager
import com.donews.yfsdk.loader.IPreloadInterstitialAd

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/14 17:25
 */
class PreloadInterstitialInfo(private val activity: AppCompatActivity?) {


    companion object {
        const val PRELOAD_TIME_OUT = 15000
    }

    /** 预加载状态 */
    private var mPreloading: Boolean = false
    private var mNeedShow: Boolean = false
    private var mStartPreloadTime: Long = 0L

    private var mPreloadAd: PreloadInterstitialAd? = null


//    private var mRetryCount: Int = 0;

    /** 广告回调 */
    private var mListener: IAdInterstitialListener? = null

    private var mPreloadNextAd: (() -> Unit)? = null


    private var mShowActivity: Activity? = null

    fun preload() {
        preloadAd()
    }

    fun showAd(activity: Activity, listener: IAdInterstitialListener?) {
        mShowActivity = activity
        mListener = listener
        if (mPreloadAd == null) {
            if (mPreloading) {
                //正在预加载中，但是还没返回预加载对象
                mNeedShow = true
            } else {
                mListener?.onAdError(
                        AdCustomError.PreloadAdStatusError.code,
                        AdCustomError.PreloadAdStatusError.errorMsg)
                preloadAd()
            }
        } else {
            mPreloadAd?.let {
                when (it.getLoadState()) {
                    PreloadAdState.Success -> {
                        it.realShowAd(activity)
                        mListener?.onAdShow()
                    }
                    PreloadAdState.Loading -> {
                        val duration = System.currentTimeMillis() - mStartPreloadTime
                        if (duration >= PRELOAD_TIME_OUT) {
                            //直接返回错误，并且重新预加载一个视频
                            mListener?.onAdError(
                                    AdCustomError.PreloadTimesError.code,
                                    AdCustomError.PreloadTimesError.errorMsg)
                            it.destroy()
                            mListener = null
                            mNeedShow = false
                            mPreloadNextAd?.invoke()
                        } else {
                            it.realShowAd(activity)
                            mListener?.onAdShow()
                        }
                    }
                    else -> {
                        mListener?.onAdError(AdCustomError.PreloadAdStatusError.code, it.getLoadState().msg)
                        it.destroy()
                        mListener = null
                        mNeedShow = false
                        mPreloadNextAd?.invoke()
                    }
                }
            }
        }
    }

    fun setPreloadNext(preload: () -> Unit) {
        mPreloadNextAd = preload
    }

    private fun preloadAd() {
        if (activity == null) {
            mListener?.onAdError(AdCustomError.ContextError.code, AdCustomError.ContextError.errorMsg)
            return
        }
        val preloadViewListener: IPreloadInterstitialAd = object : IPreloadInterstitialAd {
            override fun preload(ad: PreloadInterstitialAd) {
                mPreloadAd = ad
            }
        }

        val interstitialListener = object : IAdInterstitialListener {

            override fun onAdStartLoad() {
                mListener?.onAdStartLoad()
            }

            override fun onAdStatus(code: Int, any: Any?) {
                mListener?.onAdStatus(code, any)
            }

            override fun onAdLoad() {
                mListener?.onAdLoad()
                if (mNeedShow) {
                    mPreloadAd?.showAd()
                }
            }

            override fun onAdShow() {
                mListener?.onAdShow()
            }

            override fun onAdExposure() {
                mListener?.onAdExposure()
            }

            override fun onAdClicked() {
                mListener?.onAdClicked()
            }

            override fun onAdClosed() {
                mListener?.onAdClosed()
                mPreloadNextAd?.invoke()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                mPreloading = false
                mPreloadAd?.destroy()
                mListener?.onAdError(code, errorMsg)
                mListener = null
                /*if (mPreloading) {
                    mPreloading = false
                    if (code != AdCustomError.CloseAd.code) {
                        retry()
                    }
                } else {
                    retry()
                }*/
            }
        }
        mStartPreloadTime = System.currentTimeMillis()
        AdManager.preloadInterstitialAd(activity, preloadViewListener, interstitialListener)
    }

    /*private fun retry() {
        mRetryCount++
        if (mRetryCount < 2) {
            preloadAd()
        } else {
            mPreloading = false
            mListener = null
            mPreloadAd?.destroy()
        }
    }*/
}
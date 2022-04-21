package com.donews.yfsdk.preload

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.donews.common.ad.cache.PreloadInterstitialInfo

/**
 *
 * 插屏预加载工具类
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/14 16:36
 */
@SuppressLint("StaticFieldLeak")
object AdInterstitialCacheUtils {

    private var mActivity: AppCompatActivity? = null

    private var mCurrentPreload: PreloadInterstitialInfo? = null

    private var mNextPreload: PreloadInterstitialInfo? = null

    private var mInterstitialListener: IAdInterstitialListener? = null

    fun cache(activity: AppCompatActivity) {
        mActivity = activity
        mActivity?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    mActivity?.lifecycle?.removeObserver(this)
                    mActivity = null
                }
            }
        })

//        if (AdConfigManager.mNormalAdBean.interstitial.sdkChannel == 1) {
        preloadInterstitialInfo()
//        }
    }

    fun showAd(activity: Activity, listener: IAdInterstitialListener?) {
        //先更新一次显示时间
        mCurrentPreload = mNextPreload
        mInterstitialListener = listener
        if (mCurrentPreload == null) {
            listener?.onAdError(
                    AdCustomError.PreloadAdStatusError.code,
                    AdCustomError.PreloadAdStatusError.errorMsg
            )
            return
        } else {
            mCurrentPreload!!.showAd(activity, listener)
        }
    }

    private fun preloadInterstitialInfo() {
        val info = PreloadInterstitialInfo(mActivity)
        info.setPreloadNext { preloadInterstitialInfo() }
        info.preload()
        mNextPreload = info
    }
}
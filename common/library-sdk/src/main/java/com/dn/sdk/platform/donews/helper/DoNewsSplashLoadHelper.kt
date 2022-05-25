package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.dn.sdk.listener.splash.IAdSplashListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadSplashAd
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD
import com.donews.utilslibrary.utils.DensityUtils

/**
 * 多牛sdk v2加载开屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 14:04
 */
object DoNewsSplashLoadHelper : BaseHelper() {

    /** 加载和显示广告 */
    fun loadAndShowAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        runOnUiThread(activity) {
            listener?.onAdStartLoad()
        }
        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdIdNullOrBlank.code,
                    AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                )
            }
            return
        }

        if (adRequest.mAdContainer == null) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdContainerNull.code,
                    AdCustomError.ParamsAdContainerNull.errorMsg
                )
            }
            return
        }

        if (adRequest.mWidthDp == 0f) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdWidthDpError.code,
                    AdCustomError.ParamsAdWidthDpError.errorMsg
                )
            }
            return
        }

        if (adRequest.mHeightDp == 0f) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdHeightDpError.code,
                    AdCustomError.ParamsAdHeightDpError.errorMsg
                )
            }
            return
        }


        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        //生命周期监听
        bindLifecycle(activity, doNewsAdNative)

        val doNewsSplashListener = object : DoNewsAdNative.SplashListener {

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                }
            }

            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    listener?.onAdStatus(code, any)
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdClicked() {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdExposure() {
                runOnUiThread(activity) {
                    listener?.onAdExposure()
                }
            }

            override fun onAdDismissed() {
                runOnUiThread(activity) {
                    listener?.onAdDismiss()
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            //广告位id
            .setPositionId(adRequest.mAdId)
            //传入的用来展示广告的容器，此布局建议用FrameLayout或者 Relativelayout
            .setView(adRequest.mAdContainer)
            //开屏此值传递px单位的值
            .setExpressViewWidth(DensityUtils.dp2px(adRequest.mWidthDp).toFloat())
            .setExpressViewHeight(DensityUtils.dp2px(adRequest.mHeightDp).toFloat())
            //设置超时时间5000代表5秒，时间建议大于等于5秒以上，如果GroMore广告，请按照gromore后台合理配置
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        doNewsAdNative.loadAndShowSplash(activity, doNewsAd, doNewsSplashListener)
    }

    /** 预加载广告 */
    var doNewsPreloadSplashAd: DoNewsAdNative? = null
    var doNewsPreloadSplashAdStats: PreloadAdState = PreloadAdState.Init
    fun preloadAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
        runOnUiThread(activity) {
            listener?.onAdStartLoad()
        }
        if (doNewsPreloadSplashAd == null) {
            doNewsPreloadSplashAd = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        }

        doNewsPreloadSplashAdStats = PreloadAdState.Loading

        bindLifecycle(activity, doNewsPreloadSplashAd) {
            doNewsPreloadSplashAd?.destroy()
            doNewsPreloadSplashAd = null
        }

        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAdStats = PreloadAdState.Error
                    listener?.onAdError(
                            AdCustomError.ParamsAdIdNullOrBlank.code,
                            AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                    )
                }
            }
            doNewsPreloadSplashAd?.destroy()
            doNewsPreloadSplashAd = null
            return
        }

        if (adRequest.mAdContainer == null) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAdStats = PreloadAdState.Error
                    listener?.onAdError(
                            AdCustomError.ParamsAdContainerNull.code,
                            AdCustomError.ParamsAdContainerNull.errorMsg
                    )
                }
            }
            doNewsPreloadSplashAd?.destroy()
            doNewsPreloadSplashAd = null
            return
        }

        if (adRequest.mWidthDp == 0f) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAdStats = PreloadAdState.Error
                    listener?.onAdError(
                            AdCustomError.ParamsAdWidthDpError.code,
                            AdCustomError.ParamsAdWidthDpError.errorMsg
                    )
                }
            }
            doNewsPreloadSplashAd?.destroy()
            doNewsPreloadSplashAd = null
            return
        }

        if (adRequest.mHeightDp == 0f) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAdStats = PreloadAdState.Error
                    listener?.onAdError(
                            AdCustomError.ParamsAdHeightDpError.code,
                            AdCustomError.ParamsAdHeightDpError.errorMsg
                    )
                }
            }
            doNewsPreloadSplashAd?.destroy()
            doNewsPreloadSplashAd = null
            return
        }

        val doNewsSplashListener = object : DoNewsAdNative.SplashListener {

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                    doNewsPreloadSplashAdStats = PreloadAdState.Success
                }
            }

            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    listener?.onAdStatus(code, any)
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdClicked() {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdExposure() {
                runOnUiThread(activity) {
                    listener?.onAdExposure()
                }
            }

            override fun onAdDismissed() {
                runOnUiThread(activity) {
                    listener?.onAdDismiss()
                    doNewsPreloadSplashAdStats = PreloadAdState.Shown
                }
                doNewsPreloadSplashAd?.destroy()
                doNewsPreloadSplashAd = null
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    activity.runOnUiThread {
                        doNewsPreloadSplashAdStats = PreloadAdState.Error
                        listener?.onAdError(code, errorMsg)
                    }
                }
                doNewsPreloadSplashAd?.destroy()
                doNewsPreloadSplashAd = null
            }
        }

        val doNewsAd = DoNewsAD.Builder()
                //广告位id
                .setPositionId(adRequest.mAdId)
                //传入的用来展示广告的容器，此布局建议用FrameLayout或者 Relativelayout
                .setView(adRequest.mAdContainer)
                //设置超时时间5000代表5秒，时间建议大于等于5秒以上，如果GroMore广告，请按照gromore后台合理配置
                .setTimeOut(adRequest.mAdRequestTimeOut)
                .setExpressViewWidth(DensityUtils.dp2px(adRequest.mWidthDp).toFloat())
                .setExpressViewHeight(DensityUtils.dp2px(adRequest.mHeightDp).toFloat())
                .build()
            doNewsPreloadSplashAd?.loadSplashAd(activity, doNewsAd, doNewsSplashListener)
    }

    fun isAdReady():Boolean {
        return doNewsPreloadSplashAd != null && doNewsPreloadSplashAdStats == PreloadAdState.Success
    }

    fun showSplash() {
        doNewsPreloadSplashAd?.showSplash()
    }
}
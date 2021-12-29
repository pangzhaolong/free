package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadSplashAd
import com.dn.sdk.listener.IAdSplashListener
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DnUnionBean
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD

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
        listener?.onAdStartLoad()
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
                    if (code == 10 && any is DnUnionBean) {
                        listener?.onAdStatus(code, AdStatus(any))
                    } else {
                        listener?.onAdStatus(code, any)
                    }
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
            //设置超时时间5000代表5秒，时间建议大于等于5秒以上，如果GroMore广告，请按照gromore后台合理配置
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        doNewsAdNative.loadAndShowSplash(activity, doNewsAd, doNewsSplashListener)
    }

    /** 预加载广告 */
    fun preloadAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?): PreloadSplashAd {
        listener?.onAdStartLoad()
        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()

        //封装的预加载对象
        val doNewsPreloadSplashAd = DoNewsPreloadSplashAd(doNewsAdNative)
        doNewsPreloadSplashAd.setLoadState(PreloadAdState.Loading)

        bindLifecycle(activity, doNewsAdNative) {
            doNewsPreloadSplashAd.destroy()
        }


        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdIdNullOrBlank.code,
                        AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                    )
                }
            }
            return doNewsPreloadSplashAd
        }

        if (adRequest.mAdContainer == null) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdContainerNull.code,
                        AdCustomError.ParamsAdContainerNull.errorMsg
                    )
                }
            }
            return doNewsPreloadSplashAd
        }

        if (adRequest.mWidthDp == 0f) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdWidthDpError.code,
                        AdCustomError.ParamsAdWidthDpError.errorMsg
                    )
                }
            }
            return doNewsPreloadSplashAd
        }

        if (adRequest.mHeightDp == 0f) {
            runOnUiThread(activity) {
                DelayExecutor.delayExec {
                    doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
                    listener?.onAdError(
                        AdCustomError.ParamsAdHeightDpError.code,
                        AdCustomError.ParamsAdHeightDpError.errorMsg
                    )
                }
            }
            return doNewsPreloadSplashAd
        }


        val doNewsSplashListener = object : DoNewsAdNative.SplashListener {

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                    doNewsPreloadSplashAd.setLoadState(PreloadAdState.Success)
                    //加载成功后，判断是否需要立即调用展示
                    if (doNewsPreloadSplashAd.isNeedShow()) {
                        doNewsPreloadSplashAd.showAd()
                    }
                }
            }

            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    if (code == 10 && any is DnUnionBean) {
                        listener?.onAdStatus(code, AdStatus(any))
                    } else {
                        listener?.onAdStatus(code, any)
                    }
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    doNewsPreloadSplashAd.setLoadState(PreloadAdState.Shown)
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
                    activity.runOnUiThread {
                        doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
                        listener?.onAdError(code, errorMsg)
                    }
                }
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            //广告位id
            .setPositionId(adRequest.mAdId)
            //传入的用来展示广告的容器，此布局建议用FrameLayout或者 Relativelayout
            .setView(adRequest.mAdContainer)
            //设置超时时间5000代表5秒，时间建议大于等于5秒以上，如果GroMore广告，请按照gromore后台合理配置
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        DelayExecutor.delayExec(100) {
            doNewsAdNative.loadSplashAd(activity, doNewsAd, doNewsSplashListener)
        }
        return doNewsPreloadSplashAd
    }
}
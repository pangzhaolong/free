package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadSplashAd
import com.dn.sdk.listener.IAdSplashListener
import com.dn.sdk.bean.preload.PreloadSplashAd
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
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
        if (adRequest.mAdId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return
        }

        if (adRequest.mAdContainer == null) {
            listener?.onAdError(
                AdCustomError.ParamsAdContainerNull.code,
                AdCustomError.ParamsAdContainerNull.errorMsg
            )
            return
        }

        if (adRequest.mWidthDp == 0f) {
            listener?.onAdError(
                AdCustomError.ParamsAdWidthDpError.code,
                AdCustomError.ParamsAdWidthDpError.errorMsg
            )
            return
        }

        if (adRequest.mHeightDp == 0f) {
            listener?.onAdError(
                AdCustomError.ParamsAdHeightDpError.code,
                AdCustomError.ParamsAdHeightDpError.errorMsg
            )
            return
        }


        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        //生命周期监听
        bindLifecycle(activity, doNewsAdNative)

        val doNewsSplashListener = object : DoNewsAdNative.SplashListener {

            override fun onAdLoad() {
                listener?.onAdLoad()
            }

            override fun onAdStatus(code: Int, any: Any?) {
                listener?.onAdStatus(code, any)
            }

            override fun onAdShow() {
                listener?.onAdShow()
            }

            override fun onAdClicked() {
                listener?.onAdClicked()
            }

            override fun onAdExposure() {
                listener?.onAdExposure()
            }

            override fun onAdDismissed() {
                listener?.onAdDismiss()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                listener?.onAdError(code, errorMsg)
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
        listener?.onAdStartLoad()
        doNewsAdNative.loadAndShowSplash(activity, doNewsAd, doNewsSplashListener)
    }

    /** 预加载广告 */
    fun preloadAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?): PreloadSplashAd {

        val doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()

        //封装的预加载对象
        val doNewsPreloadSplashAd = DoNewsPreloadSplashAd(doNewsAdNative)
        doNewsPreloadSplashAd.setLoadState(PreloadAdState.Loading)

        bindLifecycle(activity, doNewsAdNative) {
            doNewsPreloadSplashAd.destroy()
        }


        if (adRequest.mAdId.isBlank()) {
            doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
            )
            return doNewsPreloadSplashAd
        }

        if (adRequest.mAdContainer == null) {
            doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
            listener?.onAdError(
                AdCustomError.ParamsAdContainerNull.code,
                AdCustomError.ParamsAdContainerNull.errorMsg
            )
            return doNewsPreloadSplashAd
        }

        if (adRequest.mWidthDp == 0f) {
            doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
            listener?.onAdError(
                AdCustomError.ParamsAdWidthDpError.code,
                AdCustomError.ParamsAdWidthDpError.errorMsg
            )
            return doNewsPreloadSplashAd
        }

        if (adRequest.mHeightDp == 0f) {
            doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
            listener?.onAdError(
                AdCustomError.ParamsAdHeightDpError.code,
                AdCustomError.ParamsAdHeightDpError.errorMsg
            )
            return doNewsPreloadSplashAd
        }


        val doNewsSplashListener = object : DoNewsAdNative.SplashListener {

            override fun onAdLoad() {
                listener?.onAdLoad()
                doNewsPreloadSplashAd.setLoadState(PreloadAdState.Success)
                //加载成功后，判断是否需要立即调用展示
                if (doNewsPreloadSplashAd.isNeedShow()) {
                    doNewsPreloadSplashAd.showAd()
                }
            }

            override fun onAdStatus(code: Int, any: Any?) {
                listener?.onAdStatus(code, any)
            }

            override fun onAdShow() {
                listener?.onAdShow()
            }

            override fun onAdClicked() {
                listener?.onAdClicked()
            }

            override fun onAdExposure() {
                listener?.onAdExposure()
            }

            override fun onAdDismissed() {
                listener?.onAdDismiss()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                doNewsPreloadSplashAd.setLoadState(PreloadAdState.Error)
                listener?.onAdError(code, errorMsg)
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
        listener?.onAdStartLoad()
        doNewsAdNative.loadSplashAd(activity, doNewsAd, doNewsSplashListener)
        return doNewsPreloadSplashAd
    }
}
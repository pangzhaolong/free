package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.DelayExecutor
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.AdStatus
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadInterstitialAd
import com.dn.sdk.listener.interstitial.IAdInterstitialFullScreenListener
import com.dn.sdk.listener.interstitial.IAdInterstitialListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.donews.preloadad.DoNewsPreloadInterstitialAd
import com.dn.sdk.platform.gromore.preloadad.GroMorePreloadInterstitialAd
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DnUnionBean
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD

/**
 * 多牛v2 插屏全屏广告加载帮助类(插全屏广告)
 *
 * @author lcl
 * @version v1.0
 * @date 2021/3/11
 */
object DoNewsInterstitialFullScreenLoadHelper : BaseHelper() {

    /** 加载和显示广告，插屏广告必须传递广告id，和广告宽高属性 */
    fun loadAndShowAd(
        activity: Activity,
        adRequest: AdRequest,
        listener: IAdInterstitialFullScreenListener?
    ) {
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

        val doNewsRewardvideoAD: DoNewsAD = DoNewsAD.Builder()
            .setPositionId(adRequest.mAdId)
            .setExpressViewWidth(adRequest.mWidthDp)///设置宽高 （插全屏类型下_插屏广告使 用,必填）
            .setExpressViewHeight(adRequest.mHeightDp)///设置宽高 （插全屏类型下_插屏广告 使用，必填））
            .setUserID(adRequest.mUserId)//用户id 必填参数
            .setRewardName(adRequest.mRewardName)//插全屏有的广告有奖励接口，此字段为发放奖 励名称
            .setRewardAmount(adRequest.mRewardAmount)//插全屏有的广告有奖励接口，此字段为发放奖励 数量
            .setOrientation(adRequest.mOrientation)//1竖屏 2横屏 必填参数
            .build()
        val doNewsNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        val doNewsInterstitialListener = object : DoNewsAdNative.InterstitialFullAdListener {
            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    if (code == 10 && any is DnUnionBean) {
                        listener?.onAdStatus(code, AdStatus(any))
                    } else {
                        listener?.onAdStatus(code, any)
                    }
                }
            }

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                }
            }

            override fun onAdCached() {
                runOnUiThread(activity) {
                    listener?.onAdCached()
                    //展示广告
                    doNewsNative.showInterstitialFullAd()
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

            override fun onAdShowFail(p0: Int, p1: String?) {
                runOnUiThread(activity) {
                    listener?.onAdShowFail(p0, p1 ?: "未知异常")
                }
            }

            override fun onAdVideoError(p0: Int, p1: String?) {
                runOnUiThread(activity) {
                    listener?.onAdVideoError(p0, p1 ?: "未知异常")
                }
            }

            override fun onAdComplete() {
                runOnUiThread(activity) {
                    listener?.onAdComplete()
                }
            }

            override fun onAdClose() {
                runOnUiThread(activity) {
                    listener?.onAdClose()
                }
            }

            override fun onRewardVerify(p0: Boolean) {
                runOnUiThread(activity) {
                    listener?.onRewardVerify(p0)
                }
            }

            override fun onSkippedVideo() {
                runOnUiThread(activity) {
                    listener?.onSkippedVideo()
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg ?: "未知错误")
                }
            }
        }

        bindLifecycle(activity, doNewsNative)
        doNewsNative.loadInterstitialFullAd(
            activity,
            doNewsRewardvideoAD,
            doNewsInterstitialListener
        )
    }
}
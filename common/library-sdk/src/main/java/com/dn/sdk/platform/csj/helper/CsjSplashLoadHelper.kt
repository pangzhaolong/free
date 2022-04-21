package com.dn.sdk.platform.csj.helper

import android.app.Activity
import android.view.View
import com.bytedance.sdk.openadsdk.*
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.splash.IAdSplashListener
import com.dn.sdk.platform.BaseHelper

/**
 * 穿山甲 开屏加载
 *
 *  make in dw
 *  on 2022/04/06
 */
object CsjSplashLoadHelper : BaseHelper() {

    fun loadSplashAd(activity: Activity, adRequest: AdRequest, listener: IAdSplashListener?) {
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

        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)

        val adSlot = AdSlot.Builder()
                .setCodeId(adRequest.mAdId)
                //不区分渲染方式，要求开发者同时设置setImageAcceptedSize（单位：px）和setExpressViewAcceptedSize（单位：dp ）接口，不同时设置可能会导致展示异常。
                .setImageAcceptedSize(adRequest.widthPx.toInt(), adRequest.heightPx.toInt())
                .setExpressViewAcceptedSize(adRequest.mWidthDp, adRequest.mHeightDp)
                .setAdLoadType(TTAdLoadType.LOAD)//推荐使用，用于标注此次的广告请求用途为预加载（当做缓存）还是实时加载，方便后续为开发者优化相关策略
                .build()

        val adInteractionListener = object : TTSplashAd.AdInteractionListener {
            override fun onAdClicked(p0: View?, p1: Int) {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdShow(p0: View?, p1: Int) {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdSkip() {
                runOnUiThread(activity) {
                    listener?.onAdDismiss()
                }
            }

            override fun onAdTimeOver() {
                runOnUiThread(activity) {
                    listener?.onAdDismiss()
                }
            }
        }

        val doNewsFullScreenListener = object : TTAdNative.SplashAdListener {
            //广告加载失败
            override fun onError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }

            override fun onTimeout() {
                runOnUiThread(activity) {
                    listener?.onAdError(AdCustomError.LoadTimeOutError.code, AdCustomError.LoadTimeOutError.errorMsg)
                }
            }

            override fun onSplashAdLoad(p0: TTSplashAd?) {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                    p0?.setSplashInteractionListener(adInteractionListener)
                    if (!activity.isFinishing) {
                        adRequest.mAdContainer?.removeAllViews()
                        adRequest.mAdContainer?.addView(p0?.splashView)
                    }
                }
            }
        }
        mTTAdNative.loadSplashAd(adSlot, doNewsFullScreenListener)
    }
}
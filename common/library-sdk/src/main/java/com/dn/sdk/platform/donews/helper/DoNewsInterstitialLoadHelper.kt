package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.IAdInterstitialListener
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD

/**
 * 多牛v2 加载插屏广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 16:01
 */
object DoNewsInterstitialLoadHelper : BaseHelper() {

    /** 加载和显示广告，插屏广告必须传递广告id，和广告宽高属性 */
    fun loadAndShowAd(activity: Activity, adRequest: AdRequest, listener: IAdInterstitialListener?) {
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

        val doNewsNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        bindLifecycle(activity, doNewsNative)

        val doNewsInterstitialListener = object : DoNewsAdNative.DonewsInterstitialADListener {
            override fun onAdStatus(code: Int, any: Any?) {
                runOnUiThread(activity) {
                    listener?.onAdStatus(code, any)
                }
            }

            override fun onAdLoad() {
                runOnUiThread(activity) {
                    listener?.onAdLoad()
                }
            }

            override fun onAdShow() {
                runOnUiThread(activity) {
                    listener?.onAdShow()
                }
            }

            override fun onAdExposure() {
                runOnUiThread(activity) {
                    listener?.onAdExposure()
                }
            }

            override fun onAdClicked() {
                runOnUiThread(activity) {
                    listener?.onAdClicked()
                }
            }

            override fun onAdClosed() {
                runOnUiThread(activity) {
                    listener?.onAdClosed()
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            .setPositionId(adRequest.mAdId)
            .setExpressViewWidth(adRequest.mWidthDp)
            .setExpressViewHeight(adRequest.mHeightDp)
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        doNewsNative.loadAndShowInterstitial(activity, doNewsAd, doNewsInterstitialListener)
    }

}
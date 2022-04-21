package com.dn.sdk.platform.donews.helper

import android.app.Activity
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.INativeAdData
import com.dn.sdk.listener.feed.natives.IAdFeedLoadListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.donews.natives.DoNewsFeedData
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD
import com.donews.ads.mediation.v2.framework.listener.DoNewsAdNativeData

/**
 * 信息流自渲染加载广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 17:02
 */
object DoNewsFeedLoadHelper : BaseHelper() {

    fun loadFeedAd(activity: Activity, adRequest: AdRequest, listener: IAdFeedLoadListener?) {
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

        if (adRequest.mAdCount > 5) {
            runOnUiThread(activity) {
                listener?.onAdError(
                    AdCustomError.ParamsAdCountError.code,
                    AdCustomError.ParamsAdCountError.errorMsg
                )
            }
            return
        }

        val doNewsNative = DoNewsAdManagerHolder.get().createDoNewsAdNative()
        bindLifecycle(activity, doNewsNative)

        val doNewsAd = DoNewsAD.Builder()
            .setPositionId(adRequest.mAdId)
            //请求的数量，不超过
            .setAdCount(adRequest.mAdCount)
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()

        val doNewsNativesListener = object : DoNewsAdNative.DoNewsNativesListener {
            override fun onAdLoad(data: MutableList<DoNewsAdNativeData>?) {
                runOnUiThread(activity) {
                    listener?.let {
                        val result = mutableListOf<INativeAdData>()
                        if (data != null) {
                            for (doNewsData in data) {
                                result.add(DoNewsFeedData(adRequest, doNewsData))
                            }
                        }
                        it.onAdLoad(result)
                    }
                }
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }
        }
        doNewsNative.loadFeed(activity, doNewsAd, doNewsNativesListener)
    }

}
package com.dn.sdk.platform.csj.helper

import android.app.Activity
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTDrawFeedAd
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTDrawFeedAdData
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.draw.natives.IAdDrawNativeLoadListener
import com.dn.sdk.listener.draw.natives.TrackDrawNativeLoadListenerProxy
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.csj.bean.CsjDrawAd

/**
 *  穿山甲 自渲染Draw流加载器
 *
 *  make in st
 *  on 2021/12/27 18:17
 */
object CsjDrawNativeLoadHelper : BaseHelper() {

    fun loadDrawAd(activity: Activity, adRequest: AdRequest, listenerNative: IAdDrawNativeLoadListener?) {
        runOnUiThread(activity) {
            listenerNative?.onAdStartLoad()
        }
        if (adRequest.mAdId.isBlank()) {
            runOnUiThread(activity) {
                listenerNative?.onAdError(
                        AdCustomError.ParamsAdIdNullOrBlank.code,
                        AdCustomError.ParamsAdIdNullOrBlank.errorMsg
                )
            }
            return
        }

        if (adRequest.mAdCount > 5) {
            runOnUiThread(activity) {
                listenerNative?.onAdError(
                        AdCustomError.ParamsAdCountError.code,
                        AdCustomError.ParamsAdCountError.errorMsg
                )
            }
            return
        }

        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
        val adSlot = AdSlot.Builder()
                .setCodeId(adRequest.mAdId)//广告位id
                .setExpressViewAcceptedSize(adRequest.mWidthDp, adRequest.mHeightDp)
                .setAdCount(adRequest.mAdCount)//请求广告数量为1到3条
                .build()

        val countTrackImpl = if (listenerNative is TrackDrawNativeLoadListenerProxy) {
            listenerNative.countTrack
        } else {
            CountTrackImpl(adRequest)
        }

        val doNewsNativeExpressListener = object : TTAdNative.DrawFeedAdListener {

            //广告加载失败
            override fun onError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listenerNative?.onAdError(code, errorMsg)
                }
            }

            //广告加载成功
            override fun onDrawFeedAdLoad(ads: MutableList<TTDrawFeedAd>?) {
                runOnUiThread(activity) {
                    listenerNative?.let {
                        val result = mutableListOf<ITTDrawFeedAdData>()
                        if (ads != null) {
                            for (doNewsData in ads) {
                                result.add(CsjDrawAd(adRequest, countTrackImpl, doNewsData))
                            }
                        }
                        it.onAdLoad(result)
                    }
                }
            }
        }

        mTTAdNative.loadDrawFeedAd(adSlot, doNewsNativeExpressListener)
    }

}
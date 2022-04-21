package com.dn.sdk.platform.csj.helper

import android.app.Activity
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.natives.ITTNativeExpressAdData
import com.dn.sdk.listener.draw.template.IAdDrawTemplateLoadListener
import com.dn.sdk.platform.BaseHelper
import com.dn.sdk.platform.csj.bean.CsjDrawTemplateAd

/**
 *  穿山甲 Draw 模板流加载
 *
 *  make in st
 *  on 2021/12/27 18:17
 */
object CsjDrawTemplateLoadHelper : BaseHelper() {

    fun loadDrawTemplateAd(activity: Activity, adRequest: AdRequest, listener: IAdDrawTemplateLoadListener?) {
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

        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
        val adSlot = AdSlot.Builder()
            .setSupportDeepLink(true)
            .setCodeId(adRequest.mAdId)//广告位id
            .setExpressViewAcceptedSize(adRequest.mWidthDp, adRequest.mHeightDp)
            .setAdCount(adRequest.mAdCount)//请求广告数量为1到3条
            .build()

        val doNewsNativeExpressListener = object : TTAdNative.NativeExpressAdListener {
            //广告加载失败
            override fun onError(code: Int, errorMsg: String?) {
                runOnUiThread(activity) {
                    listener?.onAdError(code, errorMsg)
                }
            }

            //广告加载成功
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                runOnUiThread(activity) {
                    listener?.let {
                        val result = mutableListOf<ITTNativeExpressAdData>()
                        if (ads != null) {
                            for (doNewsData in ads) {
                                result.add(CsjDrawTemplateAd(doNewsData))
                            }
                        }
                        it.onAdLoad(result)
                    }
                }
            }
        }
        mTTAdNative.loadExpressDrawFeedAd(adSlot, doNewsNativeExpressListener)
    }

}
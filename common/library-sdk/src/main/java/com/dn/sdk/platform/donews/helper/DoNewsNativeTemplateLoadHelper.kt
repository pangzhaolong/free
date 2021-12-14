package com.dn.sdk.platform.donews.helper

import android.app.Activity
import android.view.View
import com.dn.sdk.AdCustomError
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.IAdNativeTemplateListener
import com.donews.ads.mediation.v2.api.DoNewsAdManagerHolder
import com.donews.ads.mediation.v2.api.DoNewsAdNative
import com.donews.ads.mediation.v2.framework.bean.DoNewsAD

/**
 * 信息流模板广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/2 17:03
 */
object DoNewsNativeTemplateLoadHelper : BaseHelper() {

    /** 加载模板广告 */
    fun loadTemplateAd(activity: Activity, adRequest: AdRequest, listener: IAdNativeTemplateListener?) {
        listener?.onAdStartLoad()

        if (adRequest.mAdId.isBlank()) {
            listener?.onAdError(
                AdCustomError.ParamsAdIdNullOrBlank.code,
                AdCustomError.ParamsAdIdNullOrBlank.errorMsg
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

        val doNewsTemplateListener = object : DoNewsAdNative.DoNewsTemplateListener {
            override fun onAdStatus(code: Int, any: Any?) {
                listener?.onAdStatus(code, any)
            }

            override fun onAdLoad(views: MutableList<View>?) {
                listener?.let {
                    val result = mutableListOf<View>()
                    if (views != null) {
                        result.addAll(views)
                    }
                    it.onAdLoad(result)
                }
            }

            override fun onAdShow() {
                listener?.onAdShow()
            }

            override fun onAdExposure() {
                listener?.onAdExposure()
            }

            override fun onAdClicked() {
                listener?.onAdClicked()
            }

            override fun onAdClose() {
                listener?.onAdClose()
            }

            override fun onAdError(code: Int, errorMsg: String?) {
                listener?.onAdError(code, errorMsg)
            }
        }

        val doNewsAd = DoNewsAD.Builder()
            //广告位id
            .setPositionId(adRequest.mAdId)
            .setExpressViewWidth(adRequest.mWidthDp)
            .setExpressViewHeight(adRequest.mHeightDp)
            .setTimeOut(adRequest.mAdRequestTimeOut)
            .build()
        doNewsAdNative.loadTemplate(activity, doNewsAd, doNewsTemplateListener)
    }
}
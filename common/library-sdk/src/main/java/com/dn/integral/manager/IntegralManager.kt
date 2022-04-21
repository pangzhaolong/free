package com.dn.integral.manager

import com.dn.integral.bean.IntegralError
import com.dn.sdk.listener.IAdIntegralLoadListener
import com.dn.integral.bean.DoNewsIntegralAdData
import com.donews.ads.mediation.v2.integral.DnIntegralNativeAd
import com.donews.ads.mediation.v2.integral.DoNewsIntegralHolder
import com.donews.ads.mediation.v2.integral.api.DnIntegralHttpCallBack
import com.donews.ads.mediation.v2.integral.api.DnIntegralIntegralError

import com.donews.utilslibrary.utils.StringUtils

/**
 * 积分墙管理器
 *
 * @author: cymbi
 * @data: 2021/12/27
 */
object IntegralManager {
    fun getIntegralList(loadListener: IAdIntegralLoadListener) {
        DoNewsIntegralHolder.getInstance().getIntegralList(object : DnIntegralHttpCallBack {
            override fun onSuccess(integralAds: List<DnIntegralNativeAd>) {
                val result = mutableListOf<DoNewsIntegralAdData>()
                for (ad in integralAds) {
                    result.add(DoNewsIntegralAdData(ad))
                }
                loadListener.onSuccess(result)
            }

            override fun onError(errorCallback: DnIntegralIntegralError) {
                var message = ""
                if (!StringUtils.isNullOrEmpty(errorCallback.message)) {
                    message = errorCallback.message
                }
                loadListener.onError(
                    IntegralError(
                        errorCallback.code, message
                    )
                )
            }
        })
    }
}
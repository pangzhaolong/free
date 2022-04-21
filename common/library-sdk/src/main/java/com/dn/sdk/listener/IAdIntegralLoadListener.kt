package com.dn.sdk.listener

import com.dn.integral.bean.IntegralError
import com.dn.integral.bean.DoNewsIntegralAdData

/**
 * 积分墙加载数据监听器
 *
 * @author: cymbi
 * @data: 2021/12/27
 */
interface IAdIntegralLoadListener {
    fun onSuccess(integralAds: List<DoNewsIntegralAdData?>?)

    fun onError(errorCallback: IntegralError?)
}
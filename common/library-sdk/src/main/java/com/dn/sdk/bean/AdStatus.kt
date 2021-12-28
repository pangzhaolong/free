package com.dn.sdk.bean

import com.donews.ads.mediation.v2.framework.bean.DnUnionBean

/**
 * 数据状态封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/28 10:30
 */
class AdStatus(private val dnUnionBean: DnUnionBean) {

    val reqId: String = dnUnionBean.reqId
    val appId: String = dnUnionBean.appId
    val positionId: String = dnUnionBean.positionId
    val groMorePositionId: String = dnUnionBean.groMorePostionId
    val currentPositionId: String = dnUnionBean.currentPostionId
    val platFormType: String = dnUnionBean.platFormType
    val currentEcpm: String = dnUnionBean.currentEcpm
}
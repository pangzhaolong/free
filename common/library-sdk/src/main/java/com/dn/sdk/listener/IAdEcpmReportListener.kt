package com.dn.sdk.listener

import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse

/**
 * Ecpm上报监听器，根据Ecpm获取用户奖励
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/21 15:21
 */
interface IAdEcpmReportListener {
    /** 添加上报ecpm 参数 */
    fun addReportEcpmParamsWhenReward(params: EcpmParam)

    /** 上报ecpm成功 */
    fun reportEcpmSuccessWhenReward(response: EcpmResponse)

    /** 上报ecpm失败 */
    fun reportEcpmFailWhenReward()
}
package com.dn.sdk.sdk.interfaces.idconfig

import com.dn.sdk.sdk.platform.IAdIdConfigCallback
import com.dn.sdk.sdk.platform.IPlatform

/**
 * 广告id配置接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 18:12
 */
interface IAdIdConfigManager<T> : IAdIdConfig {
    /** 添加一个初始化监听器，当广告id配置已经完成，则直接回调，否则等到初始化完成再回调 */
    fun addInitListener(listener: IAdIdConfigCallback)

    /** 是否初始化完成配置 */
    fun isInitConfig(): Boolean

    /** 默认的配置 */
    fun getDefaultConfig(): T

    /** 根据策略返回平台信息 */
    fun getPlatform(): IPlatform
}
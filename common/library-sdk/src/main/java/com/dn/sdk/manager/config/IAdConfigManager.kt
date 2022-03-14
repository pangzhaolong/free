package com.dn.sdk.manager.config

import com.dn.sdk.platform.IPlatform

/**
 * 基础Ad配置Manager
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 13:20
 */
interface IAdConfigManager {

    /**
     * 初始化
     */
    fun init()

    /** 添加一个初始化监听器，当广告id配置已经完成，则直接回调，否则等到初始化完成再回调 */
    fun addInitListener(listener: IAdConfigInitListener)

    /** 是否初始化成功 */
    fun isInitSuccess(): Boolean

    /** 根据策略返回广告加载平台信息 */
    fun getPlatform(): IPlatform
}
package com.dn.sdk.sdk.interfaces.idconfig

/**
 * 初始化配置接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 10:04
 */
interface IAdIdConfig {
    /**
     * 初始化广告id配置
     * @param configPath String 广告id配置接口
     */
    fun initAdIdConfig(configPath: String)
}
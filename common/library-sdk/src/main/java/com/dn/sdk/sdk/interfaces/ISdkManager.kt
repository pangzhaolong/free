package com.dn.sdk.sdk.interfaces

import android.app.Application

/**
 * SDK 初始化接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:49
 */
interface ISdkManager {
    /**
     * 初始化SDK 接口
     * @param context Application 上下文对象
     * @param openDebug Boolean 是否开启debug模式
     * @param groMoreAppId String GroMore平台AppId
     */
    fun initSDK(context: Application, openDebug: Boolean, groMoreAppId: String)

    /**
     * 设置一些SDK 需要的接口
     * @param channel String 渠道名
     * @param userId String 用户id
     * @param oaid String OAID
     */
    fun setInfo(channel: String, userId: String, oaid: String)
}
package com.dn.sdk.manager.sdk

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
     * @param channelName String app渠道名称
     * @param openDebug Boolean 是否开启debug模式
     */
    fun initSDK(context: Application, channelName: String, openDebug: Boolean)
}
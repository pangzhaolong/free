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

    fun initSDK(context: Application, openDebug: Boolean, groMoreAppId: String)

    fun setInfo(channel: String, userId: String, oaid: String)
}
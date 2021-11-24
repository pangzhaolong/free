package com.dn.sdk.sdk.utils

import com.dn.sdk.sdk.bean.RequestInfo
import com.orhanobut.logger.Logger
import java.lang.StringBuilder

/**
 * 日志打印
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/23 10:31
 */
object AdLoggerUtils {

    private const val TAG = "DoNewsAd"

    private var loggable: Boolean = false

    fun setLoggable(logger: Boolean) {
        loggable = logger
    }

    fun d(msg: String) {
        if (loggable) {
            Logger.t(TAG).d(msg)
        }
    }

    fun e(msg: String) {
        if (loggable) {
            Logger.t(TAG).e(msg)
        }
    }

    fun createMsg(requestInfo: RequestInfo, customMsg: String): String {
        val builder = StringBuilder()
        builder.append("广告类型 = ").append(requestInfo.adType.description).append(",")
        builder.append("广告平台 = ").append(requestInfo.platform.getLoader().sdkType.description).append(",")
        builder.append("广告请求id = ").append(requestInfo.adId).append(",")
        builder.append("广告AdKey = ").append(requestInfo.appIdKey).append(",")
        builder.append("customMsg = ").append(customMsg)
        return builder.toString()
    }
}
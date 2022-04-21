package com.dn.sdk.utils

import android.util.Log
import com.dn.sdk.bean.AdRequest
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
            Log.d(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (loggable) {
            Log.e(TAG, msg)
        }
    }

    fun createMsg(adRequest: AdRequest, customMsg: String): String {
        val builder = StringBuilder()
        builder.append("广告类型 = ").append(adRequest.mAdType.msg).append(",")
        builder.append("广告平台 = ").append(adRequest.mPlatform.getLoader().getSdkType().msg).append(",")
        builder.append("广告请求id = ").append(adRequest.mAdId).append(",")
//        builder.append("广告AdKey = ").append(adRequest.mAdKey).append(",")
        builder.append("customMsg = ").append(customMsg)
        return builder.toString()
    }
}
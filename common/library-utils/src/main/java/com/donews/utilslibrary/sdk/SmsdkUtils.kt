package com.donews.utilslibrary.sdk

import com.ishumei.smantifraud.SmAntiFraud

/**
 *
 * 数美sdk工具类
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 18:21
 */


// 获得数美的deviceID
fun getShuMeiDeviceId(): String {
    var deviceId = ""
    try {
        deviceId = SmAntiFraud.getDeviceId()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return deviceId
}
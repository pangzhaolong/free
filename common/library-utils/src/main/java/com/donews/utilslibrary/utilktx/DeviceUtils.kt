package com.donews.utilslibrary.utilktx

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager

/**
 *
 * 设备相关信息获取
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 16:23
 */

/** 获取deviceId */
@SuppressLint("MissingPermission", "HardwareIds")
fun getDeviceId(): String {
    var imei = ""
    try {
        val application = ApplicationInject.getApplication()
        val tm: TelephonyManager = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            imei = tm.deviceId
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return imei
}

/** 获取AndroidId */
fun getAndroidId(): String {
    var androidId = ""
    try {
        val application = ApplicationInject.getApplication()
        androidId = Settings.System.getString(application.contentResolver, Settings.System.ANDROID_ID)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return androidId
}

/** 获取macAddress */
@SuppressLint("MissingPermission", "HardwareIds")
fun getMacAddress(): String {
    var macAddress = ""
    try {
        val application: Application = ApplicationInject.getApplication()
        val context = application.applicationContext
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.connectionInfo?.let {
            macAddress = it.macAddress
        }
    } catch (t: Throwable) {
        t.printStackTrace()
    }
    return macAddress
}



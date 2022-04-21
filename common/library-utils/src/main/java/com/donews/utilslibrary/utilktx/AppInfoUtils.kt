package com.donews.utilslibrary.utilktx

import android.app.Application
import android.os.Build

/**
 * app相关信息获取类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 16:46
 */


fun getVersionCode(): Long {
    var versionCode: Long = -1
    try {
        val app: Application = ApplicationInject.getApplication()
        val packageManager = app.packageManager
        val packageInfo = packageManager.getPackageInfo(app.packageName, 0)
        packageInfo?.let {
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                it.longVersionCode
            } else {
                it.versionCode.toLong()
            }
        }

    } catch (t: Throwable) {
        t.printStackTrace()
    }
    return versionCode
}

fun getVersionName(): String {
    var versionName = ""
    try {
        val app: Application = ApplicationInject.getApplication()
        val packageManager = app.packageManager
        val packageInfo = packageManager.getPackageInfo(app.packageName, 0)
        packageInfo?.let {
            versionName = it.versionName
        }
    } catch (t: Throwable) {
        t.printStackTrace()
    }
    return versionName
}

fun getPackageName(): String {
    var packageName = ""
    try {
        val app: Application = ApplicationInject.getApplication()
        val packageManager = app.packageManager
        val packageInfo = packageManager.getPackageInfo(app.packageName, 0)
        packageInfo?.let {
            packageName = it.packageName
        }
    } catch (t: Throwable) {
        t.printStackTrace()
    }
    return packageName
}
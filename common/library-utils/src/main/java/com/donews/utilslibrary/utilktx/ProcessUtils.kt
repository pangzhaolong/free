package com.donews.utilslibrary.utilktx

import android.content.Context
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 *
 * 进程相关
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 18:07
 */

/**
 * 判断当前进程是否是主进程
 *
 * @return true 是主进程,false,不是主进程
 */
fun isMainProcess(context: Context): Boolean {
    val processName = getProcessName(Process.myPid()) ?: return false
    return processName == context.packageName
}


/**
 * 获取当前进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
    return null
}
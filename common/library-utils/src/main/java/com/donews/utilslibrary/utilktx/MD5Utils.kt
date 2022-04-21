package com.donews.utilslibrary.utilktx

import java.lang.Exception
import java.security.MessageDigest

/**
 *  MD5 相关方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/15 9:35
 */

fun getFileMD5(b: ByteArray?): String {
    if (b == null) {
        return ""
    }
    val digest: MessageDigest
    val sb = StringBuffer("")
    try {
        digest = MessageDigest.getInstance("MD5")
        digest.update(b, 0, b.size)
        val buff = digest.digest()
        var i: Int
        for (offset in buff.indices) {
            i = buff[offset].toInt()
            if (i < 0) i += 256
            if (i < 16) sb.append("0")
            sb.append(Integer.toHexString(i))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
    return sb.toString()
}
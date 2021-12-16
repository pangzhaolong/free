package com.donews.base.utils.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/30 18:35
 */

/** 判断这个时间是否今天 */
fun Long.isToday(): Boolean {
    val mDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    val curDate = Date(this)
    val today = Date()
    return mDataFormat.format(curDate) == mDataFormat.format(today)
}

fun Long.toDataString(): String {
    val mDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)
    val curDate = Date(this)
    return mDataFormat.format(curDate)
}
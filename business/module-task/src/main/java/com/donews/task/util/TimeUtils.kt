package com.donews.task.util

import java.util.*

/**
 *  make in st
 *  on 2022/5/7 16:01
 */
object TimeUtils {

    /**
     * 毫秒值转两位数(时:分:秒)格式(00:00:00)
     */
    fun stringForTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString()
    }

    //毫秒值转两位数(分:秒)格式（00:00）
    fun stringForTimeNoHour(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        return Formatter().format("%02d:%02d", minutes, seconds).toString()
    }

}
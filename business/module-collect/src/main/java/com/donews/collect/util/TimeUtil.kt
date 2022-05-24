package com.donews.collect.util

import android.widget.TextView
import java.util.*

/**
 *  make in st
 *  on 2022/5/17 15:25
 */
object TimeUtil {

    /**
     * 将时间戳转化为对应的时间  日-时-分-秒
     */
    fun timeConversion(mTime: Long,tvDay:TextView,tvHour:TextView,tvMinute:TextView,tvSecond:TextView): String {
        var time = mTime
        var day: Long = 0
        var hour: Long = 0
        var minutes: Long = 0
        var second: Long = 0
        val dayTmp = time % (3600 * 24)
        val hourTmp = time % 3600
        if (time >= 86400) {
            day = time / (3600 * 24)
            if (dayTmp != 0L) {
                time -= day * 24 * 60 * 60
                if (time in 3600..86399) {
                    hour = time / 3600
                    if (hourTmp != 0L) {
                        if (hourTmp >= 60) {
                            minutes = hourTmp / 60
                            if (hourTmp % 60 != 0L) {
                                second = hourTmp % 60
                            }
                        } else if (hourTmp < 60) {
                            second = hourTmp
                        }
                    }
                } else if (time < 3600) {
                    minutes = time / 60
                    if (time % 60 != 0L) {
                        second = time % 60
                    }
                }
            }
        } else if (time in 3600..86399) {
            hour = time / 3600
            if (hourTmp != 0L) {
                if (hourTmp >= 60) {
                    minutes = hourTmp / 60
                    if (hourTmp % 60 != 0L) {
                        second = hourTmp % 60
                    }
                } else if (hourTmp < 60) {
                    second = hourTmp
                }
            }
        } else if (time < 3600) {
            minutes = time / 60
            if (time % 60 != 0L) {
                second = time % 60
            }
        }
        tvDay.text = (if (day < 10) "0$day" else day).toString()
        tvHour.text = (if (hour < 10) "0$hour" else hour).toString()
        tvMinute.text = (if (minutes < 10) "0$minutes" else minutes).toString()
        tvSecond.text = (if (second < 10) "0$second" else second).toString()
        return (if (day < 10) "0$day" else day).toString() + "天" + (if (hour < 10) "0$hour" else hour) + "时" + (if (minutes < 10) "0$minutes" else minutes) + "分" + (if (second < 10) "0$second" else second) + "秒"
    }

    //毫秒值转两位数(分:秒)格式（00:00）
    fun stringForTimeNoHour(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        return Formatter().format("%02d:%02d", minutes, seconds).toString()
    }

}
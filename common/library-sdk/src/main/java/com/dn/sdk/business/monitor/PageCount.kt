package com.dn.sdk.business.monitor

import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

/**
 * 页面计数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/28 11:36
 */
object PageCount {

    private const val MMKV_NAME = "pageCount"
    private val mmkv = MMKV.mmkvWithID(MMKV_NAME, MMKV.MULTI_PROCESS_MODE)
    private val mDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    private const val SPLIT = ","

    /**
     * 页面显示数+1
     * @param pageTag String
     */
    fun pageResume(pageTag: String) {
        var dataString = mmkv?.decodeString(pageTag, "")
        var dateTime = System.currentTimeMillis()
        var resumeNumber: Int
        var adShowNumber = 0
        if (dataString.isNullOrBlank()) {
            resumeNumber = 1
            dataString = "$dateTime$SPLIT$resumeNumber$SPLIT$adShowNumber"
            mmkv?.encode(pageTag, dataString)
        } else {
            val list = dataString.split(SPLIT)
            dateTime = list[0].toLong()
            resumeNumber = list[1].toInt()
            adShowNumber = list[2].toInt()
            if (isToday(dateTime)) {
                resumeNumber += 1
            } else {
                resumeNumber = 1
                adShowNumber = 0
            }
            dateTime = System.currentTimeMillis()
            dataString = "$dateTime$SPLIT$resumeNumber$SPLIT$adShowNumber"
            mmkv?.encode(pageTag, dataString)
        }
    }

    /**
     * 更新广告次数
     * @param pageTag String
     */
    fun showAdSuccess(pageTag: String) {
        var dataString = mmkv?.decodeString(pageTag, "")
        var dateTime = System.currentTimeMillis()
        var resumeNumber = 0
        var adShowNumber: Int
        if (dataString.isNullOrBlank()) {
            adShowNumber = 1
            dataString = "$dateTime$SPLIT$resumeNumber$SPLIT$adShowNumber"
            mmkv?.encode(pageTag, dataString)
        } else {
            val list = dataString.split(SPLIT)
            dateTime = list[0].toLong()
            resumeNumber = list[1].toInt()
            adShowNumber = list[2].toInt()
            if (isToday(dateTime)) {
                if (resumeNumber == 0) {
                    resumeNumber += 1
                }
                adShowNumber += 1
            } else {
                resumeNumber = 1
                adShowNumber = 1
            }
            dateTime = System.currentTimeMillis()
            dataString = "$dateTime$SPLIT$resumeNumber$SPLIT$adShowNumber"
            mmkv?.encode(pageTag, dataString)
        }
    }

    /**
     * 获取当日页面显示次数
     * @param pageTag String 页面tag
     * @return Int
     */
    fun getResumeNumber(pageTag: String): Int {
        val dataString = mmkv?.decodeString(pageTag, "")
        return if (dataString.isNullOrBlank()) {
            0
        } else {
            val list = dataString.split(SPLIT)
            list[1].toInt()
        }
    }

    /**
     * 获取当日页面显示广告数量
     * @param pageTag String 页面tag
     * @return Int
     */
    fun getAdShowNumber(pageTag: String): Int {
        val dataString = mmkv?.decodeString(pageTag, "")
        return if (dataString.isNullOrBlank()) {
            0
        } else {
            val list = dataString.split(SPLIT)
            list[2].toInt()
        }
    }


    /**
     * 判断是否为今天
     * @param time Long 时间戳 毫秒
     * @return Boolean true，是今天，false 不是今天
     */
    private fun isToday(time: Long): Boolean {
        val curDate = Date(time)
        val today = Date()
        return mDataFormat.format(curDate) == mDataFormat.format(today)
    }
}
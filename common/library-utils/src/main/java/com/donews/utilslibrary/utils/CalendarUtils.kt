package com.donews.utilslibrary.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.util.Log
import java.util.*

/**
 * 日历添加提醒Utils
 *
 *  日历使用需要添加以下权限，并且6.0以上需要主动申请权限
 *
 *  <uses-permission android:name="android.permission.READ_CALENDAR" />
 *  <uses-permission android:name="android.permission.WRITE_CALENDAR" />
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/18 16:42
 */

@SuppressWarnings("unused")
object CalendarUtils {

    private val TAG = CalendarUtils::class.java.simpleName

    private val CALENDAR_URL = CalendarContract.Calendars.CONTENT_URI
    private val CALENDAR_EVENT_URL = CalendarContract.Events.CONTENT_URI
    private val CALENDAR_REMINDER_URL = CalendarContract.Reminders.CONTENT_URI

    private const val CALENDARS_NAME = "jdd"
    private const val CALENDARS_ACCOUNT_NAME = "jdd@jdd.com"
    private const val CALENDARS_ACCOUNT_TYPE = "com.android.jdd"
    private const val CALENDARS_DISPLAY_NAME = "jdd账户"


    //region 日历账户相关方法
    /**
     * 检查是否存在现有日历账户，存在则返回账户id，否则返回-1
     */
    fun checkCalendarAccount(context: Context): Long {
        var result = -1L
        val userCourse = context.contentResolver.query(CALENDAR_URL, null, null, null, null)
        try {
            return userCourse?.let {
                val count = it.count
                if (count > 0) {
                    if (it.moveToFirst()) {
                        result = it.getLong(it.getColumnIndex(CalendarContract.Calendars._ID))
                    }
                }
                result
            } ?: run {
                result
            }
        } catch (e: Throwable) {
            Log.e(TAG, "检测日历账户错误", e)
            return result
        } finally {
            userCourse?.close()
        }
    }

    /***
     * 添加一个日志账号，成功返回账号id，错误返回-1
     */
    fun addCalendarAccount(context: Context): Long {
        val timeZone = TimeZone.getDefault()
        val contentValues = ContentValues().apply {
            put(CalendarContract.Calendars.NAME, CALENDARS_NAME)
            put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME)
            put(CalendarContract.Calendars.VISIBLE, 1)
            put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
            put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
            put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME)
            put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        }

        val calendarUri = CALENDAR_URL.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
            .build()
        val result = context.contentResolver.insert(calendarUri, contentValues)
        val id = result?.let {
            ContentUris.parseId(it)
        } ?: run {
            Log.i(TAG, "添加日历账户${CALENDARS_ACCOUNT_NAME}失败")
            -1
        }
        return id
    }

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     */
    fun checkAndAddCalendarAccount(context: Context): Long {
        val oldId = checkCalendarAccount(context)
        if (oldId > 0) {
            return oldId
        }
        val addId = addCalendarAccount(context)
        return if (addId > 0) {
            checkCalendarAccount(context)
        } else {
            -1
        }
    }
    //endregion

    //region 日程相关方法
    /**
     * 添加日历事件提醒
     *
     * @param context Context 上下文对象
     * @param title String 日程标题
     * @param desc String 日程说明
     * @param reminderTime Long 提示时间,单位毫秒
     * @param duration Long 提示持续时间，单位分钟
     * @param rrule String 重复规则
     * @param previousDate Long 提前previous Date时间提醒,单位分钟
     */
    fun addCalendarEvent(
        context: Context,
        title: String,
        desc: String,
        reminderTime: Long,
        duration: Long,
        rule: String? = null,
        vararg previousDate: Long
    ) {
        val calId = checkAndAddCalendarAccount(context)
        if (calId <= 0) {
            return
        }

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = reminderTime
        val startTime = calendar.time.time
        calendar.timeInMillis = startTime + duration * 60 * 1000
        val endTime = calendar.time.time

        val event = ContentValues().apply {
            put("title", title)
            put("description", desc)
            put("calendar_id", calId)
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)

            if (!rule.isNullOrBlank()) {
                put(CalendarContract.Events.RRULE, rule)
            }
            //设置闹钟提醒
            put(CalendarContract.Events.HAS_ALARM, 1)
            //这个是时区，必须有
            put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")

        }

        val eventUri = context.contentResolver.insert(CALENDAR_EVENT_URL, event)
        if (eventUri == null) {
            Log.i(TAG, "添加日程${title}失败")
            return
        }

        val eventId = ContentUris.parseId(eventUri)

        //提前几分钟提醒
        if (previousDate.isNotEmpty()) {
            for (time in previousDate) {
                val reminderEvent = ContentValues().apply {
                    put(CalendarContract.Reminders.EVENT_ID, eventId)
                    put(CalendarContract.Reminders.MINUTES, time)
                    put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                }
                val uri = context.contentResolver.insert(CALENDAR_REMINDER_URL, reminderEvent)
                if (uri == null) {
                    Log.i(TAG, "添加 提前提醒${title}分钟失败")
                }
            }
        }
    }

    /**
     * 删除日程
     * @param context Context 上下文对象
     * @param title String 日程标题
     */
    fun deleteCalendarEvent(context: Context, title: String) {
        val course = context.contentResolver.query(CALENDAR_EVENT_URL, null, null, null, null)
        course?.use { courseUse ->
            val count = courseUse.count
            if (count <= 0) {
                return
            }
            courseUse.moveToFirst()
            while (!courseUse.isAfterLast) {
                val eventTitle = courseUse.getString(courseUse.getColumnIndex("title"))
                if (!eventTitle.isNullOrBlank() && title == eventTitle) {
                    val id = courseUse.getLong(courseUse.getColumnIndex(CalendarContract.Calendars._ID))
                    val deleteUri = ContentUris.withAppendedId(CALENDAR_EVENT_URL, id)
                    val rows = context.contentResolver.delete(deleteUri, null, null)
                    if (rows == -1) {
                        Log.i(TAG, "删除日程${title}失败")
                    }
                }
                courseUse.moveToNext()
            }
        }
    }
    //endregion
}
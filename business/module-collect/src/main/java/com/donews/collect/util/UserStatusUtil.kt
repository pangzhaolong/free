package com.donews.collect.util

import android.util.Log
import com.donews.utilslibrary.utils.AppInfo

/**
 *  make in st
 *  on 2022/5/18 14:59
 */
object UserStatusUtil {

    fun isNewUser():Boolean{
        //2022-05-11 10:46:11
        Log.i("timeC--->","--a--->${AppInfo.getUserRegisterTime()}")

        return false
    }

}
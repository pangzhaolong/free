package com.donews.yfsdk.utils

import com.tencent.mmkv.MMKV

object AppUtil {

    private const val AD_MMKV_NAME = "ad_app_status"

    private val MMKV_AD_APP_INSTALL_TIME = "ad_app_install_time"
    private val MMKV_AD_APP_INSTALL_FLAG = "ad_app_install_flag"

    private var mmkv = MMKV.mmkvWithID(AD_MMKV_NAME, MMKV.MULTI_PROCESS_MODE)

    fun saveAppInstallTime() {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(AD_MMKV_NAME, MMKV.MULTI_PROCESS_MODE)
        }
        if (mmkv != null) {
            val firstOpenApp = mmkv!!.decodeBool(MMKV_AD_APP_INSTALL_FLAG, true)
            if (firstOpenApp) {
                mmkv!!.encode(MMKV_AD_APP_INSTALL_TIME, System.currentTimeMillis())
                mmkv!!.encode(MMKV_AD_APP_INSTALL_FLAG, false)
            }
        }
    }


    fun getAppInstallTime(): Long {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(AD_MMKV_NAME, MMKV.MULTI_PROCESS_MODE)
        }
        return if (mmkv != null) {
            mmkv!!.decodeLong(MMKV_AD_APP_INSTALL_TIME, 0L)
        } else {
            0L
        }
    }
}
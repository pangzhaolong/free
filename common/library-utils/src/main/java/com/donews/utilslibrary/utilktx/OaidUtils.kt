package com.donews.utilslibrary.utilktx

import com.tencent.mmkv.MMKV

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 17:50
 */
object OaidUtils {

    private val mmkv = MMKV.mmkvWithID("oaid", MMKV.MULTI_PROCESS_MODE)!!

    private const val KEY_OAID = "oaid_code"

    fun saveOaid(oaid: String) {
        mmkv.encode(KEY_OAID, oaid)
    }

    fun getOaid(): String {
        val result = mmkv.decodeString(KEY_OAID, "")
        return if (result.isNullOrBlank()) "" else result
    }
}
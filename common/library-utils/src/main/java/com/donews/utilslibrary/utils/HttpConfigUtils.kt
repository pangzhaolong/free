package com.donews.utilslibrary.utils

/**
 * 扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/29 16:58
 */

/** 地址添加配置后缀 */
fun String.withConfigParams(withToken: Boolean = false): String {
    var params = JsonUtils.getCommonJson(false)
    val tag = SPUtils.getInformain(KeySharePreferences.USER_TAG, null)
    if (tag != null) {
        params = "$params&$tag"
    }
    return "$this${params}"
}
package com.donews.utilslibrary.sdk

import com.donews.utilslibrary.utilktx.ApplicationInject
import com.bytedance.hume.readapk.HumeSDK

/**
 * 头条分包渠道
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/21 18:01
 */

fun getTouTiaoChannelName(): String {
    val application = ApplicationInject.getApplication()
    return try {
        HumeSDK.getChannel(application)
    } catch (e: Exception) {
        ""
    }
}
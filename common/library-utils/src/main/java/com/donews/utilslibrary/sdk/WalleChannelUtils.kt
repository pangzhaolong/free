package com.donews.utilslibrary.sdk

import com.donews.utilslibrary.utilktx.ApplicationInject
import com.donews.utilslibrary.BuildConfig
import com.meituan.android.walle.ChannelInfo
import com.meituan.android.walle.WalleChannelReader

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 17:11
 */

/** 获取渠道名称,默认渠道为dev开发渠道 */
fun getChannelName(): String {
    val application = ApplicationInject.getApplication()
    val channelInfo: ChannelInfo? = WalleChannelReader.getChannelInfo(application)
    var channel = ""
    channelInfo?.let {
        channel = it.channel
    } ?: run {
        val wallChannel = WalleChannelReader.getChannel(application)
        if (wallChannel != null) {
            channel = wallChannel
        }
    }
    return if (channel.isNotBlank()) channel else BuildConfig.APP_IDENTIFICATION
}
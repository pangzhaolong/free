package com.donews.common.ad.business.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * 广告开关配置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/1 16:52
 */
@Parcelize
data class JddAdOpenConfigBean(
    @SerializedName("openAd")
    var openAd: Boolean = true,
    @SerializedName("userLevelStrategy")
    var userLevelStrategy: Boolean = false
) : Parcelable
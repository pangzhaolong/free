package com.donews.yfsdk.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class DrawInfoBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("CsjIdNew")
        var CsjIdNew: String = "",
        @SerializedName("CsjIdOld")
        var CsjIdOld: String = "",
        @SerializedName("CsjIdInvalid")
        var CsjIdInvalid: String = "",
        @SerializedName("KsIdNew")
        var KsIdNew: String = "",
        @SerializedName("KsIdOld")
        var KsIdOld: String = "",
        @SerializedName("KsIdInvalid")
        var KsIdInvalid: String = "",
        @SerializedName("sdkChannel")
        var sdkChannel: Int = 0,
        @SerializedName("preload")
        var preload: Int = 10,
        @SerializedName("switchAfter")
        var switchAfter: Int = 10
) : BaseCustomViewModel(), Parcelable
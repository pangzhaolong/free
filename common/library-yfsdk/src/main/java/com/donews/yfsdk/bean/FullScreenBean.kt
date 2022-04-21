package com.donews.yfsdk.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class FullScreenBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("GmIdNew")
        var GmIdNew: String = "",
        @SerializedName("GmIdOld")
        var GmIdOld: String = "",
        @SerializedName("GmIdInvalid")
        var GmIdInvalid: String = "",
        @SerializedName("preload")
        var preload: Int = 10,
        @SerializedName("switchAfter")
        var switchAfter: Int = 10
) : BaseCustomViewModel(), Parcelable

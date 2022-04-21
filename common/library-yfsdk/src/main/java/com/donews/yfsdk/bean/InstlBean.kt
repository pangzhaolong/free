package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class InstlBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("DnIdNew")
        var DnIdNew: String = "",
        @SerializedName("DnIdOld")
        var DnIdOld: String = "",
        @SerializedName("DnIdInvalid")
        var DnIdInvalid: String = "",
        @SerializedName("switchAfter")
        var switchAfter: Int = 10,
        @SerializedName("GmIdNew")
        var GmIdNew: String = "",
        @SerializedName("GmIdOld")
        var GmIdOld: String = "",
        @SerializedName("GmIdInvalid")
        var GmIdInvalid: String = "",
        @SerializedName("usePreload")
        var usePreload: Boolean = false,
        @SerializedName("startTime")
        var startTime: Int = 0,
        @SerializedName("interval")
        var interval: Int = 20
) : BaseCustomViewModel(), Parcelable
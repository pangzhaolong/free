package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class InfoBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("DnIdNew")
        var DnIdNew: String = "",
        @SerializedName("DnIdOld")
        var DnIdOld: String = "",
        @SerializedName("DnIdInvalid")
        var DnIdInvalid: String = "",
        @SerializedName("preload")
        var preload: Boolean = false,
        @SerializedName("startTime")
        var startTime: Int = 1,
        @SerializedName("switchAfter")
        var switchAfter: Int = 1,
        @SerializedName("interval")
        var interval: Int = 10
) : BaseCustomViewModel(), Parcelable

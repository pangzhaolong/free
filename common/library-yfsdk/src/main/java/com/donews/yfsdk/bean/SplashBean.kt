package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class SplashBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("DnIdNew")
        var DnIdNew: String = "",
        @SerializedName("DnIdOld")
        var DnIdOld: String = "",
        @SerializedName("DnIdInvalid")
        var DnIdInvalid: String = "",
        @SerializedName("protectId")
        var protectId: String = "887745758",
        @SerializedName("switchAfter")
        var switchAfter: Int = 10,
        @SerializedName("preload")
        var preload: Int = 10
) : BaseCustomViewModel(), Parcelable
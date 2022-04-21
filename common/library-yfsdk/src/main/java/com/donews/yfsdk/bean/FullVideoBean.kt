package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class FullVideoBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("GMId1")
        var GMId1: String = "",
        @SerializedName("GMId2")
        var GMId2: String = "",
        @SerializedName("preload")
        var preload: Int = 10,
        @SerializedName("switchAfter")
        var switchAfter: Int = 10
) : BaseCustomViewModel(), Parcelable

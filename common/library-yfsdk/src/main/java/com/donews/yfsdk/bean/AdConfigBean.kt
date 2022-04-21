package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdConfigBean(
        @SerializedName("splash")
        var splash: String = "",
        @SerializedName("reward")
        var reward: String = "",
        @SerializedName("interstitial")
        var interstitial: String = "",
        @SerializedName("interstitialFull")
        var interstitialFull: String = "",
        @SerializedName("banner")
        var banner: String = "",
        @SerializedName("information")
        var information: String = "",
        @SerializedName("full")
        var full: String = "",
        @SerializedName("drawInformation")
        var drawInformation: String = ""
) : BaseCustomViewModel(), Parcelable
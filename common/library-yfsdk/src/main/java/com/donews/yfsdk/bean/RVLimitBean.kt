package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class RVLimitBean(
        @SerializedName("H1Watch")
        var H1Watch: Int = 10,
        @SerializedName("H1WatchEx")
        var H1WatchEx: Int = 30,
        @SerializedName("D1Watch")
        var D1Watch: Int = 100,
        @SerializedName("D1WatchEx")
        var D1WatchEx: Int = 0,
        @SerializedName("ClickWatch")
        var ClickWatch: Int = 10,
        @SerializedName("Click")
        var Click: Int = 35,
        @SerializedName("ClickEx")
        var ClickEx: Int = 50,
        @SerializedName("OneAdClickRepeat")
        var OneAdClickRepeat: Int = 3,
        @SerializedName("OneAdClickRepeatEx")
        var OneAdClickRepeatEx: Int = 8
) : BaseCustomViewModel(), Parcelable
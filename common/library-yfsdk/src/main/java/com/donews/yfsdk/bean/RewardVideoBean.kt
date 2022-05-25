package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class RewardVideoBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("protectedId")
        var protectedId : String = "",
        @SerializedName("limit")
        var limit: RVLimitBean,
        @SerializedName("fail")
        var fail: RVFailBean
) : BaseCustomViewModel(), Parcelable
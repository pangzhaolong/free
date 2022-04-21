package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class RVFailBean(
        @SerializedName("fail")
        var fail: Int = 1,
        @SerializedName("lxFail")
        var lxFail: Int = 9
) : BaseCustomViewModel(), Parcelable

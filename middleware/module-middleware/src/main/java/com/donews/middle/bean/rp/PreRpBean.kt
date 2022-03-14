package com.donews.middle.bean.rp

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName

class PreRpBean : BaseCustomViewModel() {

    @SerializedName("pre_score")
    var pre_score: Float = 0.0f

    @SerializedName("pre_id")
    var pre_id: String = ""

}
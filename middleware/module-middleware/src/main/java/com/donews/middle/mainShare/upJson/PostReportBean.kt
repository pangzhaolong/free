package com.donews.middle.mainShare.upJson
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 17:09
 */
data class PostReportBean(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("type")
    var type: String = "",
    @SerializedName("timestamp")
    var timestamp: String = ""
): BaseCustomViewModel()
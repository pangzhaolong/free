package com.donews.middle.mainShare.upJson
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 17:09
 */
data class PostCardBean(
    @SerializedName("card_id")
    var card_id: String = ""
): BaseCustomViewModel()
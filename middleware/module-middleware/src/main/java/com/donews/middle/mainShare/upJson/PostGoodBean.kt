package com.donews.middle.mainShare.upJson
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 17:09
 */
data class PostGoodBean(
    @SerializedName("goods_id")
    var goods_id: String = ""
): BaseCustomViewModel()
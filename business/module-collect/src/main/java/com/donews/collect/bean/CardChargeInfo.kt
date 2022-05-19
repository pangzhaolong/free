package com.donews.collect.bean
import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/17 20:13
 */
data class CardChargeInfo(
    @SerializedName("uni_progress")
    var uniProgress: Int = 0,
    @SerializedName("uni_times")
    var uniTimes: Int = 0
): BaseLiveDataModel()
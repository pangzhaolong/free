package com.donews.collect.bean
import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/17 18:56
 */
data class DrawCardInfo(
    @SerializedName("card_times")
    var cardTimes: Int = 0,
    @SerializedName("img")
    var img: String = "",
    @SerializedName("no")
    var no: Int = 0
): BaseLiveDataModel()
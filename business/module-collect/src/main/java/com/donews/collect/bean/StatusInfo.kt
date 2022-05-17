package com.donews.collect.bean
import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/16 16:34
 */
data class StatusInfo(
    @SerializedName("card_id")
    var cardId: String = "",
    @SerializedName("card_times")
    var cardTimes: Int = 0,
    @SerializedName("fragments")
    var fragments: List<Fragment> = arrayListOf(),
    @SerializedName("goods_info")
    var goodsInfo: GoodsInfo,
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("time_out")
    var timeOut: Int = 0,
    @SerializedName("uni_progress")
    var uniProgress: Int = 0,
    @SerializedName("uni_times")
    var uniTimes: Int = 0
): BaseLiveDataModel()

data class Fragment(
    @SerializedName("hold_num")
    var holdNum: Int = 0,
    @SerializedName("img")
    var img: String = "",
    @SerializedName("need_num")
    var needNum: Int = 0,
    @SerializedName("no")
    var no: Int = 0
):BaseLiveDataModel()

data class GoodsInfo(
    @SerializedName("goods_id")
    var goodsId: String = "",
    @SerializedName("main_pic")
    var mainPic: String = "",
    @SerializedName("title")
    var title: String = ""
):BaseLiveDataModel()
package com.donews.collect.bean
import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/16 16:34
 */
data class StatusInfo(
    @SerializedName("card_id")
    var cardId: String,
    @SerializedName("card_times")
    var cardTimes: Int,
    @SerializedName("fragments")
    var fragments: List<Fragment>,
    @SerializedName("goods_info")
    var goodsInfo: GoodsInfo,
    @SerializedName("status")
    var status: Int,
    @SerializedName("time_out")
    var timeOut: Int,
    @SerializedName("uni_progress")
    var uniProgress: Int,
    @SerializedName("uni_times")
    var uniTimes: Int
): BaseLiveDataModel()

data class Fragment(
    @SerializedName("hold_num")
    var holdNum: Int,
    @SerializedName("img")
    var img: String,
    @SerializedName("need_num")
    var needNum: Int,
    @SerializedName("no")
    var no: Int
):BaseLiveDataModel()

data class GoodsInfo(
    @SerializedName("goods_id")
    var goodsId: String,
    @SerializedName("main_pic")
    var mainPic: String,
    @SerializedName("title")
    var title: String
):BaseLiveDataModel()
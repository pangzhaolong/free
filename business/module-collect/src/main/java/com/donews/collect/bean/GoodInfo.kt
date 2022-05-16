package com.donews.collect.bean
import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/16 16:44
 */
data class GoodInfo(
    @SerializedName("list")
    var list: List<GoodBean> = arrayListOf()
): BaseLiveDataModel()

data class GoodBean(
    @SerializedName("goods_id")
    var goodsId: String = "",
    @SerializedName("main_pic")
    var mainPic: String = "",
    @SerializedName("title")
    var title: String = ""
):BaseLiveDataModel()
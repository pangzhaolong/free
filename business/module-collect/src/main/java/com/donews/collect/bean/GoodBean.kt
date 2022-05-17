package com.donews.collect.bean

import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName

/**
 *  make in st
 *  on 2022/5/17 17:27
 */
data class GoodBean(
    @SerializedName("goods_id")
    var goodsId: String = "",
    @SerializedName("main_pic")
    var mainPic: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("isSelect")
    var isSelect: Boolean = false
): BaseLiveDataModel()
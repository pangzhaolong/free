package com.donews.collect.bean
import com.donews.base.model.BaseLiveDataModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2021/12/6 11:21
 */
data class DanMuInfo(
    @SerializedName("list")
    var list: List<DanMuBean> = arrayListOf()
):BaseLiveDataModel()

data class DanMuBean(
    @SerializedName("avatar")
    var avatar: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("goods_name")
    var goods_name: String = ""
):BaseLiveDataModel()
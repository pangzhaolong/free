package com.donews.task.bean
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 09:39
 */
data class TaskConfigInfo(
    @SerializedName("img")
    var img: Img
): BaseCustomViewModel()

data class Img(
    @SerializedName("luckPanImg")
    var luckPanImg: String = "",//活动页底部幸运转盘图片配置
    @SerializedName("luckCollectImg")
    var luckCollectImg: String = "",//活动页底部集卡图片配置
): BaseCustomViewModel()
package com.donews.task.bean
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 09:39
 */
data class TaskConfigInfo(
    @SerializedName("ad")
    var ad: Ad,
    @SerializedName("box")
    var box: Box
): BaseCustomViewModel()

data class Ad(
    @SerializedName("mMaxCountTime")
    var mMaxCountTime: Int = 10,//冷却倒计时最大值10s
    @SerializedName("todaySeeAdMaxNum")
    var todaySeeAdMaxNum: Int = 3//今日看广告最大数
): BaseCustomViewModel()

data class Box(
    @SerializedName("boxMaxOpenNum")
    var boxMaxOpenNum: Int = 5,//宝箱最大开启数
    @SerializedName("boxMaxTime")
    var boxMaxTime: Int = 120//宝箱每次倒计时时间总长
): BaseCustomViewModel()
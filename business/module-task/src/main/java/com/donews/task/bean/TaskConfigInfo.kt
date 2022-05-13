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
    var box: Box,
    @SerializedName("exchange")
    var exchange: Exchange,
    @SerializedName("img")
    var img: Img
): BaseCustomViewModel()

data class Ad(
    @SerializedName("mMaxCountTime")
    var mMaxCountTime: Int = 180,//冷却倒计时最大值180s
    @SerializedName("todaySeeAdMaxNum")
    var todaySeeAdMaxNum: Int = 3//今日看广告最大数
): BaseCustomViewModel()

data class Box(
    @SerializedName("boxMaxOpenNum")
    var boxMaxOpenNum: Int = 5,//宝箱最大开启数
    @SerializedName("boxMaxTime")
    var boxMaxTime: Int = 120//宝箱每次倒计时时间总长
): BaseCustomViewModel()

data class Exchange(
    @SerializedName("exchangeActiveNum")
    var exchangeActiveNum: Int = 15,//活跃度兑换金币默认15活跃度
): BaseCustomViewModel()

data class Img(
    @SerializedName("luckPanImg")
    var luckPanImg: String = "",//活动页底部幸运转盘图片配置
    @SerializedName("luckCollectImg")
    var luckCollectImg: String = "",//活动页底部集卡图片配置
): BaseCustomViewModel()
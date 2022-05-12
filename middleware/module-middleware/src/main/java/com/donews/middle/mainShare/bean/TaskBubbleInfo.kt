package com.donews.middle.mainShare.bean
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 15:17
 */
data class TaskBubbleInfo(
    @SerializedName("ex")
    var ex: Ex? = null,
    @SerializedName("list")
    var list: List<BubbleBean> = arrayListOf()
): BaseCustomViewModel()

data class BubbleBean(
    @SerializedName("cd")
    var cd: Int = 0,//cd时间，剩余多少秒
    @SerializedName("desc")
    var desc: String = "",//活跃度任务描述
    @SerializedName("done")
    var done: Int = 0,//完成数
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("id")
    var id: Int = 0,//任务id
    @SerializedName("status")
    var status: Int = 0,//任务状态 0 未完成 1 完成可领取 2 已领取
    @SerializedName("title")
    var title: String = "",//活跃度任务名称
    @SerializedName("total")
    var total: Int = 0,//总数
    @SerializedName("type")
    var type: String
):BaseCustomViewModel()

data class Ex(
    @SerializedName("active")
    var active: Int = 15,//活跃度15兑换
    @SerializedName("coin")
    var coin: Int = 1500//1500金币
):BaseCustomViewModel()
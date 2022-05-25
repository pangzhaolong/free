package com.donews.task.bean
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/25 16:56
 */
data class TaskCenterInfo(
    @SerializedName("activeExchange")
    var activeExchange: ActiveExchange,
    @SerializedName("items")
    var items: List<Item> = arrayListOf()
): BaseCustomViewModel()

data class ActiveExchange(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("coin")
    var coin: Int = 0
): BaseCustomViewModel()

data class Item(
    @SerializedName("cd")
    var cd: Int = 0,
    @SerializedName("desc")
    var desc: Any? = null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("open")
    var `open`: Boolean = false,
    @SerializedName("repeat")
    var repeat: Int = 0,
    @SerializedName("task_type")
    var taskType: String = "",
    @SerializedName("times")
    var times: Int = 0,
    @SerializedName("title")
    var title: String = ""
): BaseCustomViewModel()
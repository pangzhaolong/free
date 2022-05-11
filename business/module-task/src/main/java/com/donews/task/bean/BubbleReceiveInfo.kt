package com.donews.task.bean
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *  make in st
 *  on 2022/5/11 17:02
 */
data class BubbleReceiveInfo(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("lucky")
    var lucky: Int = 0
): BaseCustomViewModel()
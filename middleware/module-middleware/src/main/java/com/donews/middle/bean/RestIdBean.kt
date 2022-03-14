package com.donews.middle.bean

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName

class RestIdBean(restId: String, pre_id: String) : BaseCustomViewModel() {

    @SerializedName("rest_id")
    val rest_id: String = restId

    @SerializedName("pre_id")
    val pre_id: String = pre_id

    override fun toString(): String {
        return "RestIdBean(rest_id='$rest_id', pre_id='$pre_id')"
    }

}
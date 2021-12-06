package com.donews.middle.bean

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName

class RestIdBean(restId: String) : BaseCustomViewModel() {

    @SerializedName("rest_id")
    val rest_id: String = restId

    override fun toString(): String {
        return "RestIdBean(rest_id='$rest_id')"
    }

}
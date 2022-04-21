package com.dn.sdk.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 * ecpm 激励上报结果
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/21 15:29
 */
@Parcelize
data class EcpmResponse(
    /** 奖励类型*/
    @SerializedName("award_type")
    var awardType: String = "",
    /** 奖励数量*/
    @SerializedName("incr")
    var incr: String = "",
    @SerializedName("items")
    var items: List<Item> = arrayListOf()
) : Parcelable


@Parcelize
data class Item(
    @SerializedName("award_type")
    var awardType: String,
    @SerializedName("incr")
    var incr: String
) : Parcelable
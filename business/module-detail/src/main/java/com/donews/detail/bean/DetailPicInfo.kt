package com.donews.detail.bean

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 * 详情图片信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/14 14:35
 */
data class DetailPicInfo(
    @SerializedName("hotAreaList")
    var hotAreaList: List<Any>,
    @SerializedName("img")
    var img: String,
    @SerializedName("width")
    var width: Int,
    @SerializedName("height")
    var height: Int
) : BaseCustomViewModel()

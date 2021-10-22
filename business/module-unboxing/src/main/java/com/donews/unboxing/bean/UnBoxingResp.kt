package com.donews.unboxing.bean

import com.google.gson.annotations.SerializedName

/**
 * 返回数据对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/22 10:08
 */
data class UnBoxingResp(
    /** 头像 */
    @SerializedName("list")
    var data: List<UnboxingBean> = arrayListOf()
)
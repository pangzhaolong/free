package com.donews.detail.bean

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *高效转链接口对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/15 10:13
 */
data class PrivilegeLinkInfo(
    /** 商品优惠券推广链接 */
    @SerializedName("coupon_click_url")
    var couponClickUrl: String,
    /** 商品淘客链接 */
    @SerializedName("item_url")
    var itemUrl: String,
    /** 淘口令 */
    @SerializedName("tpwd")
    var tpwd: String,
    /** 针对iOS14版本，增加对应能解析的长口令 */
    @SerializedName("long_tpwd")
    var longTpwd: String,
    /** 短链接 */
    @SerializedName("short_url")
    var shortUrl: String
) : BaseCustomViewModel()
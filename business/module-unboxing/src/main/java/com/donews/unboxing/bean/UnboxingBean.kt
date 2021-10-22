package com.donews.unboxing.bean

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 * 晒单信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 19:21
 */
data class UnboxingBean(
    /** id */
    @SerializedName("id")
    var id: Long = 0L,
    /** 头像 */
    @SerializedName("avatar")
    var avatar: String = "",
    /** 用户名 */
    @SerializedName("user_name")
    var userName: String = "",
    /** 城市 */
    @SerializedName("city")
    var city: String = "",
    /** 城市 */
    @SerializedName("message")
    var message: String = "",
    /** 发布时间 秒级时间戳 */
    @SerializedName("publish_at")
    var publishAt: Long = 0L,
    /** 图片列表 */
    @SerializedName("images")
    var images: List<String> = listOf(),
    /** 商品id */
    @SerializedName("goods_id")
    var goodsId: String = "",
    /** 商品图片 */
    @SerializedName("goods_pic")
    var goodsPic: String = "",
    /** 商品标题 */
    @SerializedName("title")
    var title: String = "",
    /** 价格 */
    @SerializedName("price")
    var price: Double = 0.0,
    /** 期数 */
    @SerializedName("period")
    var period: String = "",
    /** 中奖码 */
    @SerializedName("code")
    var code: String = "",
    /** 点赞数 */
    @SerializedName("zan")
    var zan: Int = 0
) : BaseCustomViewModel()
package com.donews.mail.entitys.resps

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  9.9包邮页面列表返回数据实体
 */
class MailPackHomeListResp : BaseCustomViewModel() {
    var list: MutableList<MailPackHomeListItemResp> = mutableListOf()
}

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  9.9包邮页面列表的每项数据item实体
 */
data class MailPackHomeListItemResp(
    /** 活动结束时间 */
    @SerializedName("activity_end_time")
    var activityEndTime: String? = "",
    /** 活动开始时间 */
    @SerializedName("activity_start_time")
    var activityStartTime: String? = "",
    /** 活动类型，1-无活动，2-淘抢购，3-聚划算 */
    @SerializedName("activity_type")
    var activityType: Int? = 0,
    /** 券后价 */
    @SerializedName("actual_price")
    var actualPrice: Float? = 0F,
    /** 是否是品牌商品 */
    @SerializedName("brand")
    var brand: Int? = 0,
    /** 品牌id */
    @SerializedName("brand_id")
    var brandId: String? = "",
    /** 品牌名称 */
    @SerializedName("brand_name")
    var brandName: String? = "",
    /** 商品在大淘客的分类id */
    @SerializedName("cid")
    var cid: String? = "",
    /** 优惠卷结束时间 */
    @SerializedName("coupon_end_time")
    var couponEndTime: String? = "",
    /** 优惠卷价格 */
    @SerializedName("coupon_price")
    var couponPrice: Float? = 0F,
    /** 优惠卷结束时间 */
    @SerializedName("coupon_start_time")
    var couponStartTime: String? = "",
    /** 优惠卷的总数 */
    @SerializedName("coupon_total_num")
    var couponTotalNum: Int? = 0,
    /** 商品上架时间 */
    @SerializedName("create_time")
    var createTime: String? = "",
    /** 当天销量 */
    @SerializedName("daily_sales")
    var dailySales: Int? = 0,
    /** 推广文案 */
    @SerializedName("desc")
    var desc: String? = "",
    /** 折扣力度 */
    @SerializedName("discounts")
    var discounts: Float? = 0F,
    /** 淘宝标题 */
    @SerializedName("dtitle")
    var dtitle: String? = "",
    /** 预估淘礼金 */
    @SerializedName("estimate_amount")
    var estimateAmount: Float? = 0F,
    /** 偏远地区包邮，0.不包邮，1.包邮 */
    @SerializedName("freeship_remote_district")
    var freeshipRemoteDistrict: Int? = 0,
    /** 淘宝商品id */
    @SerializedName("goods_id")
    var goodsId: String? = "",
    /** 立减金额，若无立减金额，则显示0 */
    @SerializedName("hz_quan_over")
    var hzQuanOver: Int? = 0,
    /** 商品id */
    @SerializedName("id")
    var id: String? = "",
    /** 商品主图链接 */
    @SerializedName("main_pic")
    var mainPic: String? = "",
    /** 营销主图链接 */
    @SerializedName("marketing_main_pic")
    var marketingMainPic: String? = "",
    /** 30天销量 */
    @SerializedName("month_sales")
    var monthSales: Int? = 0,
    /** 精选分类 */
    @SerializedName("nine_cid")
    var nineCid: String? = "",
    /** 商品原价 */
    @SerializedName("original_price")
    var originalPrice: Float? = 0F,
    /** 定金，若无定金，则显示0 */
    @SerializedName("quan_m_link")
    var quanMLink: Int? = 0,
    /** 淘宝卖家id */
    @SerializedName("seller_id")
    var sellerId: String? = "",
    /** 淘宝店铺等级 */
    @SerializedName("shop_level")
    var shopLevel: Int? = 0,
    /** 店铺名称 */
    @SerializedName("shop_name")
    var shopName: String? = "",
    /** 店铺类型，1-天猫，0-淘宝 */
    @SerializedName("shop_type")
    var shopType: Int? = 0,
    /** 商品在淘宝的分类id */
    @SerializedName("tbcid")
    var tbcid: String? = "",
    /** 淘宝标题 */
    @SerializedName("title")
    var title: String? = "",
    /** 2小时销量 */
    @SerializedName("two_hours_sales")
    var twoHoursSales: Int? = 0,
    /** 商品视频（新增字段） */
    @SerializedName("video")
    var video: String? = "",
    /** 0.不包运费险 1.包运费险 */
    @SerializedName("yunfeixian")
    var yunfeixian: Int? = 0
) : BaseCustomViewModel()

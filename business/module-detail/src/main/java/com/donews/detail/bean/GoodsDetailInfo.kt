package com.donews.detail.bean

import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 * 商品详情信息对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/12 14:54
 */
data class GoodsDetailInfo(
    @SerializedName("id")
    var id: String,
    @SerializedName("goods_id")
    var goodsId: String,
    @SerializedName("item_link")
    var itemLink: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("dtitle")
    var dtitle: String,
    @SerializedName("special_text")
    var specialText: List<Any>,
    @SerializedName("desc")
    var desc: String,
    @SerializedName("cid")
    var cid: String,
    @SerializedName("subcid")
    var subcid: List<Any>,
    @SerializedName("tbcid")
    var tbcid: String,
    @SerializedName("main_pic")
    var mainPic: String,
    @SerializedName("marketing_main_pic")
    var marketingMainPic: String,
    @SerializedName("video")
    var video: String,
    @SerializedName("original_price")
    var originalPrice: Double,
    @SerializedName("actual_price")
    var actualPrice: Double,
    @SerializedName("discounts")
    var discounts: Double,
    @SerializedName("coupon_link")
    var couponLink: String,
    @SerializedName("coupon_total_num")
    var couponTotalNum: Int,
    @SerializedName("coupon_receive_num")
    var couponReceiveNum: Int,
    @SerializedName("coupon_end_time")
    var couponEndTime: String,
    @SerializedName("coupon_start_time")
    var couponStartTime: String,
    @SerializedName("coupon_price")
    var couponPrice: Int,
    @SerializedName("coupon_conditions")
    var couponConditions: String,
    @SerializedName("month_sales")
    var monthSales: Int,
    @SerializedName("two_hours_sales")
    var twoHoursSales: Int,
    @SerializedName("daily_sales")
    var dailySales: Int,
    @SerializedName("brand")
    var brand: Int,
    @SerializedName("brand_id")
    var brandId: String,
    @SerializedName("brand_name")
    var brandName: String,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("activity_type")
    var activityType: Int,
    @SerializedName("activity_start_time")
    var activityStartTime: String,
    @SerializedName("activity_end_time")
    var activityEndTime: String,
    @SerializedName("shop_type")
    var shopType: Int,
    @SerializedName("gold_sellers")
    var goldSellers: Int,
    @SerializedName("seller_id")
    var sellerId: String,
    @SerializedName("shop_name")
    var shopName: String,
    @SerializedName("shop_level")
    var shopLevel: Int,
    @SerializedName("desc_score")
    var descScore: Double,
    @SerializedName("dsr_score")
    var dsrScore: Double,
    @SerializedName("dsr_percent")
    var dsrPercent: Int,
    @SerializedName("ship_score")
    var shipScore: Double,
    @SerializedName("ship_percent")
    var shipPercent: Int,
    @SerializedName("service_score")
    var serviceScore: Double,
    @SerializedName("service_percent")
    var servicePercent: Int,
    @SerializedName("hot_push")
    var hotPush: Int,
    @SerializedName("team_name")
    var teamName: String,
    @SerializedName("detail_pics")
    var detailPics: String,
    @SerializedName("imgs")
    var imgs: String,
    @SerializedName("reimgs")
    var reimgs: String,
    @SerializedName("quan_m_link")
    var quanMLink: Int,
    @SerializedName("hz_quan_over")
    var hzQuanOver: Int,
    @SerializedName("yunfeixian")
    var yunfeixian: Int,
    @SerializedName("estimate_amount")
    var estimateAmount: Int,
    @SerializedName("shop_logo")
    var shopLogo: String,
    @SerializedName("freeship_remote_district")
    var freeshipRemoteDistrict: Int,
    @SerializedName("is_subdivision")
    var isSubdivision: Int,
    @SerializedName("subdivision_id")
    var subdivisionId: String,
    @SerializedName("subdivision_name")
    var subdivisionName: String,
    @SerializedName("subdivision_rank")
    var subdivisionRank: Int,
    @SerializedName("sales24h")
    var sales24h: Int,
    @SerializedName("lowest")
    var lowest: Int,
    @SerializedName("coupon_id")
    var couponId: String
) : BaseCustomViewModel()
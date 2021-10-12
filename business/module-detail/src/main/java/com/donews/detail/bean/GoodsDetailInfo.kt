package com.donews.detail.bean
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
    val id: String,
    @SerializedName("goods_id")
    val goodsId: String,
    @SerializedName("item_link")
    val itemLink: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("dtitle")
    val dtitle: String,
    @SerializedName("special_text")
    val specialText: List<String>,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("cid")
    val cid: String,
    @SerializedName("subcid")
    val subcid: List<String>,
    @SerializedName("tbcid")
    val tbcid: String,
    @SerializedName("main_pic")
    val mainPic: String,
    @SerializedName("marketing_main_pic")
    val marketingMainPic: String,
    @SerializedName("video")
    val video: String,
    @SerializedName("original_price")
    val originalPrice: Int,
    @SerializedName("actual_price")
    val actualPrice: Int,
    @SerializedName("discounts")
    val discounts: Int,
    @SerializedName("coupon_link")
    val couponLink: String,
    @SerializedName("coupon_total_num")
    val couponTotalNum: Int,
    @SerializedName("coupon_receive_num")
    val couponReceiveNum: Int,
    @SerializedName("coupon_end_time")
    val couponEndTime: String,
    @SerializedName("coupon_start_time")
    val couponStartTime: String,
    @SerializedName("coupon_price")
    val couponPrice: Int,
    @SerializedName("coupon_conditions")
    val couponConditions: String,
    @SerializedName("month_sales")
    val monthSales: Int,
    @SerializedName("two_hours_sales")
    val twoHoursSales: Int,
    @SerializedName("daily_sales")
    val dailySales: Int,
    @SerializedName("brand")
    val brand: Int,
    @SerializedName("brand_id")
    val brandId: String,
    @SerializedName("brand_name")
    val brandName: String,
    @SerializedName("create_time")
    val createTime: String,
    @SerializedName("activity_type")
    val activityType: Int,
    @SerializedName("activity_start_time")
    val activityStartTime: String,
    @SerializedName("activity_end_time")
    val activityEndTime: String,
    @SerializedName("shop_type")
    val shopType: Int,
    @SerializedName("gold_sellers")
    val goldSellers: Int,
    @SerializedName("seller_id")
    val sellerId: String,
    @SerializedName("shop_name")
    val shopName: String,
    @SerializedName("shop_level")
    val shopLevel: Int,
    @SerializedName("desc_score")
    val descScore: Int,
    @SerializedName("dsr_score")
    val dsrScore: Int,
    @SerializedName("dsr_percent")
    val dsrPercent: Int,
    @SerializedName("ship_score")
    val shipScore: Int,
    @SerializedName("ship_percent")
    val shipPercent: Int,
    @SerializedName("service_score")
    val serviceScore: Int,
    @SerializedName("service_percent")
    val servicePercent: Int,
    @SerializedName("hot_push")
    val hotPush: Int,
    @SerializedName("team_name")
    val teamName: String,
    @SerializedName("detail_pics")
    val detailPics: String,
    @SerializedName("imgs")
    val imgs: String,
    @SerializedName("reimgs")
    val reimgs: String,
    @SerializedName("quan_m_link")
    val quanMLink: Int,
    @SerializedName("hz_quan_over")
    val hzQuanOver: Int,
    @SerializedName("yunfeixian")
    val yunfeixian: Int,
    @SerializedName("estimate_amount")
    val estimateAmount: Int,
    @SerializedName("shop_logo")
    val shopLogo: String,
    @SerializedName("freeship_remote_district")
    val freeshipRemoteDistrict: Int,
    @SerializedName("is_subdivision")
    val isSubdivision: Int,
    @SerializedName("subdivision_id")
    val subdivisionId: String,
    @SerializedName("subdivision_name")
    val subdivisionName: String,
    @SerializedName("subdivision_rank")
    val subdivisionRank: Int,
    @SerializedName("sales24h")
    val sales24h: Int,
    @SerializedName("lowest")
    val lowest: Int,
    @SerializedName("coupon_id")
    val couponId: String
)
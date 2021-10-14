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
    /** 商品id 若查询结果id=-1，则说明该商品非大淘客平台商品，数据为淘宝直接返回的数据，由于淘宝数据的缓存时间相对较长，会出现商品信息和详情信息不一致的情况*/
    @SerializedName("id")
    var id: String,
    /** 淘宝商品id */
    @SerializedName("goods_id")
    var goodsId: String,
    /** 商品淘宝链接 */
    @SerializedName("item_link")
    var itemLink: String,
    /** 淘宝标题 */
    @SerializedName("title")
    var title: String,
    /** 大淘客短标题 */
    @SerializedName("dtitle")
    var dtitle: String,
    /** 特色文案（商品卖点） */
    @SerializedName("special_text")
    var specialText: List<String>,
    /** 推广文案 */
    @SerializedName("desc")
    var desc: String,
    /** 商品在大淘客的分类id */
    @SerializedName("cid")
    var cid: String,
    /** 商品在大淘客的二级分类id，该分类为前端分类，一个商品可能有多个二级分类id */
    @SerializedName("subcid")
    var subcid: List<Int>,
    /**  商品在淘宝的分类id */
    @SerializedName("tbcid")
    var tbcid: String,
    /** 商品主图链接 */
    @SerializedName("main_pic")
    var mainPic: String,
    /** 营销主图链接 */
    @SerializedName("marketing_main_pic")
    var marketingMainPic: String,
    /**商品视频（新增字段）*/
    @SerializedName("video")
    var video: String,
    /** 商品原价 */
    @SerializedName("original_price")
    var originalPrice: Float,
    /** 券后价 */
    @SerializedName("actual_price")
    var actualPrice: Float,
    /** 折扣力度 */
    @SerializedName("discounts")
    var discounts: Float,
    /** 优惠券链接 */
    @SerializedName("coupon_link")
    var couponLink: String,
    /** 券总量 */
    @SerializedName("coupon_total_num")
    var couponTotalNum: Int,
    /** 领券量 */
    @SerializedName("coupon_receive_num")
    var couponReceiveNum: Int,
    /**  优惠券结束时间 */
    @SerializedName("coupon_end_time")
    var couponEndTime: String,
    /** 优惠券开始时间 */
    @SerializedName("coupon_start_time")
    var couponStartTime: String,
    /**  优惠券金额 */
    @SerializedName("coupon_price")
    var couponPrice: Float,
    /** 优惠券使用条件 */
    @SerializedName("coupon_conditions")
    var couponConditions: String,
    /** 30天销量 */
    @SerializedName("month_sales")
    var monthSales: Int,
    /** 2小时销量 */
    @SerializedName("two_hours_sales")
    var twoHoursSales: Int,
    @SerializedName("daily_sales")
    /** 当天销量 */
    var dailySales: Int,
    @SerializedName("brand")
    /** 是否是品牌商品 */
    var brand: Int,
    @SerializedName("brand_id")
    /** 品牌id */
    var brandId: String,
    @SerializedName("brand_name")
    /**  品牌名称 */
    var brandName: String,
    /** 商品上架时间 */
    @SerializedName("create_time")
    var createTime: String,
    /** 活动类型，1-无活动，2-淘抢购，3-聚划算 */
    @SerializedName("activity_type")
    var activityType: Int,
    /** 活动开始时间 */
    @SerializedName("activity_start_time")
    var activityStartTime: String,
    /** 活动结束时间 */
    @SerializedName("activity_end_time")
    var activityEndTime: String,
    /** 店铺类型，1-天猫，0-淘宝 */
    @SerializedName("shop_type")
    var shopType: Int,
    /** 是否是金牌卖家，1-是，0-非金牌卖家 */
    @SerializedName("gold_sellers")
    var goldSellers: Int,
    /** 淘宝卖家id，也是店铺id。店铺转链可用此字段 */
    @SerializedName("seller_id")
    var sellerId: String,
    /** 店铺名称 */
    @SerializedName("shop_name")
    var shopName: String,
    /** 淘宝店铺等级 */
    @SerializedName("shop_level")
    var shopLevel: Int,
    /** 描述分 */
    @SerializedName("desc_score")
    var descScore: Float,
    /** 描述相符 */
    @SerializedName("dsr_score")
    var dsrScore: Float,
    /**  描述同行比 */
    @SerializedName("dsr_percent")
    var dsrPercent: Float,
    /** 物流服务 */
    @SerializedName("ship_score")
    var shipScore: Float,
    /** 物流同行比 */
    @SerializedName("ship_percent")
    var shipPercent: Float,
    /** 服务态度 */
    @SerializedName("service_score")
    var serviceScore: Float,
    /** 服务同行比 */
    @SerializedName("service_percent")
    var servicePercent: Float,
    @SerializedName("hot_push")
    /** 热推值 */
    var hotPush: Int,
    @SerializedName("team_name")
    /** 放单人名称 */
    var teamName: String,
    @SerializedName("detail_pics")
    /** 商品详情图（需要做适配） */
    var detailPics: String,
    /** 淘宝轮播图 */
    @SerializedName("imgs")
    var imgs: String,
    /** 相关商品图 */
    @SerializedName("reimgs")
    var reimgs: String,
    /** 定金，若无定金，则显示0 */
    @SerializedName("quan_m_link")
    var quanMLink: Int,
    /**  立减，若无立减金额，则显示0*/
    @SerializedName("hz_quan_over")
    var hzQuanOver: Int,
    /**  0.不包运费险 1.包运费险*/
    @SerializedName("yunfeixian")
    var yunfeixian: Int,
    /** 预估淘礼金 */
    @SerializedName("estimate_amount")
    var estimateAmount: Float,
    @SerializedName("shop_logo")
    var shopLogo: String,
    @SerializedName("freeship_remote_district")
    /** 偏远地区包邮，0.不包邮，1.包邮 */
    var freeshipRemoteDistrict: Int,
    @SerializedName("is_subdivision")
    /** 该商品是否有细分类目：0不是，1是 */
    var isSubdivision: Int,
    @SerializedName("subdivision_id")
    /** 该商品所属细分类目id */
    var subdivisionId: String,
    /** 该商品所属细分类目名称 */
    @SerializedName("subdivision_name")
    var subdivisionName: String,
    /** 该商品所属细分类目排名 */
    @SerializedName("subdivision_rank")
    var subdivisionRank: Int,
    /** 24小时销量（8/17新增字段） */
    @SerializedName("sales24h")
    var sales24h: Int,
    /** 是否近30天历史最低价，0-否；1-是(8/18新增字段) */
    @SerializedName("lowest")
    var lowest: Int,
    /** 优惠券ID(9/15新增字段) */
    @SerializedName("coupon_id")
    var couponId: String
) : BaseCustomViewModel()
package com.donews.main.entitys.resps

import com.google.gson.annotations.SerializedName


/**
 * 退出拦截配置对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/18 19:55
 */
data class ExitInterceptConfig(
    /** 是否拦截退出，true拦截，false 不拦截 */
    @SerializedName("intercept")
    var intercept: Boolean = true,
    @SerializedName("notLotteryConfig")
    var notLotteryConfig: NotLotteryConfig = NotLotteryConfig(),
    @SerializedName("openRedPacketConfig")
    var openRedPacketConfig: OpenRedPacketConfig = OpenRedPacketConfig(),
    @SerializedName("continueLotteryConfig")
    var continueLotteryConfig: ContinueLotteryConfig = ContinueLotteryConfig(),
    /** 日程开始时间 */
    @SerializedName("calendarRemindStartTime")
    var calendarRemindStartTime: String = "10:00:00",
    /** 日程提醒持续时间 */
    @SerializedName("calendarRemindDuration")
    var calendarRemindDuration: Int = 30,
)

data class NotLotteryConfig(
    /** 最小抽中概率 */
    @SerializedName("minProbability")
    var minProbability: Double = 0.880,
    /** 最大抽中概率 */
    @SerializedName("maxProbability")
    var maxProbability: Double = 0.996,
    /** 获取推荐商品的接口路径 */
    @SerializedName("recommendGoodsPath")
    var recommendGoodsPath: String = "",
    /** 按钮延迟显示的延迟时间，单位秒 */
    @SerializedName("closeBtnLazyShow")
    var closeBtnLazyShow: Int = 3,
    /** 点击关闭显示的广告类型 */
    @SerializedName("adType")
    var adType: Int = 2,
)

data class OpenRedPacketConfig(
    /** 按钮延迟显示的延迟时间，单位秒 */
    @SerializedName("closeBtnLazyShow")
    var closeBtnLazyShow: Int = 3
)

data class ContinueLotteryConfig(
    /** 再抽几次最下值 */
    @SerializedName("minLotteryTimes")
    var minLotteryTimes: Int = 1,
    /** 再抽几次最大值 */
    @SerializedName("maxLotteryTimes")
    var maxLotteryTimes: Int = 3,
    /** 提升概率最小值 */
    @SerializedName("minProbability")
    var minProbability: Double = 0.5,
    /** 提升概率最大值 */
    @SerializedName("maxProbability")
    var maxProbability: Double = 0.88,
    /** 推荐商品获取接口路径地址 */
    @SerializedName("recommendGoodsPath")
    var recommendGoodsPath: String = ""
)



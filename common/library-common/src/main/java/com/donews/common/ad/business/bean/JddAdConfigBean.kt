package com.donews.common.ad.business.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 奖多多广告场景配置策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:25
 */
@Parcelize
data class JddAdConfigBean(
    /** 自动刷新间隔时间，单位：秒*/
    @SerializedName("refreshInterval")
    var refreshInterval: Int = 20,
    /** 冷启动广告开关 */
    @SerializedName("coldStartAdEnable")
    var coldStartAdEnable: Boolean = true,
    /**   冷启动开屏样式,1半屏，2全屏 */
    @SerializedName("coldStartSplashStyle")
    var coldStartSplashStyle: Int = 1,
    /** 冷启动使用双开屏策略，true 使用， false 不使用 */
    @SerializedName("coldStartDoubleSplashOpen")
    var coldStartDoubleSplashOpen: Boolean = true,
    /** 冷启动使用双开屏策略时间，用户注册48小时后 */
    @SerializedName("coldStartDoubleSplash")
    var coldStartDoubleSplash: Int = 48 * 60,

    /** 热启动广告开关 */
    var hotStartAdEnable: Boolean = true,
    /** 热启动开屏样式,1半屏，2全屏 */
    @SerializedName("hotStartSplashStyle")
    var hotStartSplashStyle: Int = 2,
    /** 热启动广告出现间隔配置 为15s    */
    @SerializedName("hotStartSplashInterval")
    var hotStartSplashInterval: Int = 15,
    /**热启动使用双开屏策略，true 使用， false 不使用 */
    @SerializedName("hotStartDoubleSplashOpen")
    var hotStartDoubleSplashOpen: Boolean = true,
    /** 热启动使用双开屏策略时间，用户注册48小时后 */
    @SerializedName("hotStartDoubleSplash")
    var hotStartDoubleSplash: Int = 48 * 60,


    /** 无效用户不同意隐私协议最终拒绝的时候广告开关*/
    @SerializedName("disagreePrivacyPolicyAdEnable")
    var disagreePrivacyPolicyAdEnable: Boolean = true,
    /** 无效用户不同意隐私协议最终拒绝的时候延迟 5s 出现广告 */
    @SerializedName("disagreePrivacyPolicyInterval")
    var disagreePrivacyPolicyInterval: Int = 5,
    /** 无效用户不同意隐私协议最终拒绝的时候出现广告类型，1插屏，2激励视频 */
    @SerializedName("disagreePrivacyPolicyAdType")
    var disagreePrivacyPolicyAdType: Int = 1,

    /** 用户连续进入app，但是无抽奖行为时,第3次退出APP后拉起广告 */
    @SerializedName("notLotteryExitAppTimes")
    var notLotteryExitAppTimes: Int = 3,

    /** 用户连续进入app，但是无抽奖行为时,第3次退出APP后拉起广告类型，1插屏，2激励视频 */
    @SerializedName("notLotteryExitAppAdType")
    var notLotteryExitAppAdType: Int = 1,

    /** 用户安装app 12 小时过后的  开启页面插屏广告 */
    @SerializedName("interstitialStartTime")
    var interstitialStartTime: Int = 12,

    @SerializedName("interstitialLotteryStartTime")
    /** 用户抽奖达到一定次数后，开启插屏广告 */
    var interstitialLotteryStartTime: Int = 6,

    /** 所有插屏广告间隔最小间隔时间 */
    @SerializedName("interstitialDuration")
    var interstitialDuration: Int = 30,

    /** 当某页面持续时间未操作，则显示插屏（单位秒） */
    @SerializedName("NoOperationDuration")
    var noOperationDuration: Int = 10,

    /** 用户关闭激励时，跟随出现一个全屏图文插屏。（当日激励视频播放次数) */
    @SerializedName("playRewardVideoTimes")
    var playRewardVideoTimes: Int = 10,

    /** 用户未抽奖时退出app弹出框关闭按钮点击时出现的广告开关 */
    @SerializedName("notLotteryExitAppDialogAdEnable")
    var notLotteryExitAppDialogAdEnable: Boolean = false,
    /** 用户未抽奖时退出app弹出框关闭按钮点击时出现的广告 和用户连续进入app，但是无抽奖行为时,第3次退出APP后拉起广告 是否互斥 */
    @SerializedName("notLotteryExitAppDialogAdMutex")
    var notLotteryExitAppDialogAdMutex: Boolean = false,
    /**  用户未抽奖时退出app弹出框关闭按钮点击时出现的广告类型 ，1插屏，2全屏视频   */
    @SerializedName("notLotteryExitAppDialogAdType")
    var notLotteryExitAppDialogAdType: Int = 1,

    /**  用户每日最大观看激励视频次数*/
    @SerializedName("todayMaxRewardVideoNumber")
    var todayMaxRewardVideoNumber: Int = 100,

    /**  用户每小时最大观看次数*/
    @SerializedName("hourRewardVideoNumber")
    var hourRewardVideoNumber: Int = 30,

    /** 达到次数则限制 x 时长 （单位毫秒，默认30分钟） */
    @SerializedName("hourLimitTime")
    var hourLimitTime: Long = 1800000,

    /**  用户每小时观看次数 */
    @SerializedName("hourClickAdMaxNumber")
    var hourClickAdMaxNumber: Int = 20,

    /** 点击率 */
    @SerializedName("hourClickRate")
    var hourClickRate: Float = 0.35f,

    /** 用户每天次数大于 HourClickAdMaxNumber 并且点击率 大于 HourClickRate ，则限制 x 时长 （单位毫秒,默认30分） */
    @SerializedName("hourClickLimitTime")
    var hourClickLimitTime: Long = 1800000,
) : BaseCustomViewModel(), Parcelable
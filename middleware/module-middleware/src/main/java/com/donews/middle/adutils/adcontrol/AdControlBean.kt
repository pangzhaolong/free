package com.donews.middle.adutils.adcontrol

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 趣白拿广告场景配置策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:25
 */
@Parcelize
data class AdControlBean(
        /** 自动刷新间隔时间，单位：秒*/
        @SerializedName("refreshInterval")
        var refreshInterval: Int = 20,
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

        @SerializedName("interstitialLotteryStartTime")
        /** 用户抽奖达到一定次数后，开启插屏广告 */
        var interstitialLotteryStartTime: Int = 6,

        /** 当某页面持续时间未操作，则显示插屏（单位秒） */
        @SerializedName("noOperationDuration")
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

        /** 页面切换是否使用插全屏 */
        @SerializedName("useInstlFullWhenSwitch")
        var useInstlFullWhenSwitch: Boolean = false

        /** 开屏广告是否使用预加载方式 */
        @SerializedName("splashAdUsePreload")
        var splashAdUsePreload: Boolean = true,

        /** 是否展示抽奖页激励视频加载失败重试弹窗 */
        @SerializedName("showRewardVideoRetryDialog")
        var showRewardVideoRetryDialog: Boolean = true,

        /** 开屏页广告增加跳过按钮透明浮窗，单位：毫秒 */
        @SerializedName("splashAdUseMask")
        var splashAdUseMask: Long = 2000
) : BaseCustomViewModel(), Parcelable
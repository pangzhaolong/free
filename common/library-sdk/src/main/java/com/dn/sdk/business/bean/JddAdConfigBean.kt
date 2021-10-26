package com.dn.sdk.business.bean

import com.google.gson.annotations.SerializedName

/**
 * 奖多多广告场景配置策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:25
 */
data class JddAdConfigBean(
    /**   冷启动开屏样式,1半屏，2全屏 */
    @SerializedName("coldStartSplashStyle")
    var coldStartSplashStyle: Int = 1,
    /** 冷启动使用双开屏策略，true 使用， false 不使用 */
    @SerializedName("coldStartDoubleSplashOpen")
    var coldStartDoubleSplashOpen: Boolean = true,
    /** 冷启动使用双开屏策略时间，用户注册48小时后 */
    @SerializedName("coldStartDoubleSplash")
    var coldStartDoubleSplash: Int = 48,

    /** 热启动开屏样式,1半屏，2全屏 */
    @SerializedName("hotStartSplashStyle")
    var hotStartSplashStyle: Int = 2,
    /** 热启动广告出现间隔配置 为15s    */
    @SerializedName("hotStartSplashInterval")
    var hotStartSplashInterval: Int = 15,

    /** 无效用户不同意隐私协议最终拒绝的时候延迟 5s 出现广告 */
    @SerializedName("disagreePrivacyPolicyInterval")
    var disagreePrivacyPolicyInterval: Int = 5,
    /** 无效用户不同意隐私协议最终拒绝的时候出现广告类型，1插屏，2激励视频 */
    @SerializedName("disagreePrivacyPolicyAdType")
    var disagreePrivacyPolicyAdType: Int = 1,

    /** 用户连续进入app，但是无抽奖行为时,第3次退出APP后拉起广告 */
    @SerializedName("NotLotteryExitAppTimes")
    var notLotteryExitAppTimes: Int = 3,

    /** 用户连续进入app，但是无抽奖行为时,第3次退出APP后拉起广告类型，1插屏，2激励视频 */
    @SerializedName("NotLotteryExitAppAdType")
    var notLotteryExitAppAdType: Int = 1,

    /** 当某页面持续时间未操作，则显示插屏（单位秒） */
    @SerializedName("NoOperationDuration")
    var noOperationDuration: Int = 10,

    /** 用户关闭激励时，跟随出现一个全屏图文插屏。（当日激励视频播放次数) */
    @SerializedName("playRewardVideoTimes")
    var playRewardVideoTimes: Int = 10
)
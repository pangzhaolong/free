package com.donews.common.ad.business.bean

import android.os.Parcel
import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName

/**
 * 奖多多广告场景配置策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:25
 */
data class JddAdConfigBean(

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
    var coldStartDoubleSplash: Int = 48,

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
    var hotStartDoubleSplash: Int = 48,


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

    /** 每个页面第二次显示在显示插屏 */
    @SerializedName("pageShowTimes")
    var pageShowTimes: Int = 2,

    /** 每个页面每天最多可显示插屏数量 */
    @SerializedName("pageInterstitialShowTimes")
    var pageInterstitialShowTimes: Int = 3,

    /** 用户注册 48小时 时间过后的  开启页面插屏广告 */
    @SerializedName("interstitialStartTime")
    var interstitialStartTime: Int = 48,

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
    var notLotteryExitAppDialogAdType: Int = 1

) : BaseCustomViewModel(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (coldStartAdEnable) 1 else 0)
        parcel.writeInt(coldStartSplashStyle)
        parcel.writeByte(if (coldStartDoubleSplashOpen) 1 else 0)
        parcel.writeInt(coldStartDoubleSplash)
        parcel.writeByte(if (hotStartAdEnable) 1 else 0)
        parcel.writeInt(hotStartSplashStyle)
        parcel.writeInt(hotStartSplashInterval)
        parcel.writeByte(if (hotStartDoubleSplashOpen) 1 else 0)
        parcel.writeInt(hotStartDoubleSplash)
        parcel.writeByte(if (disagreePrivacyPolicyAdEnable) 1 else 0)
        parcel.writeInt(disagreePrivacyPolicyInterval)
        parcel.writeInt(disagreePrivacyPolicyAdType)
        parcel.writeInt(notLotteryExitAppTimes)
        parcel.writeInt(notLotteryExitAppAdType)
        parcel.writeInt(pageShowTimes)
        parcel.writeInt(pageInterstitialShowTimes)
        parcel.writeInt(interstitialStartTime)
        parcel.writeInt(noOperationDuration)
        parcel.writeInt(playRewardVideoTimes)
        parcel.writeByte(if (notLotteryExitAppDialogAdEnable) 1 else 0)
        parcel.writeByte(if (notLotteryExitAppDialogAdMutex) 1 else 0)
        parcel.writeInt(notLotteryExitAppDialogAdType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JddAdConfigBean> {
        override fun createFromParcel(parcel: Parcel): JddAdConfigBean {
            return JddAdConfigBean(parcel)
        }

        override fun newArray(size: Int): Array<JddAdConfigBean?> {
            return arrayOfNulls(size)
        }
    }

}
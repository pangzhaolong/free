package com.donews.yfsdk.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/1 17:41
 */
@Parcelize
class NormalBean(
        @SerializedName("enable")
        var enable: Boolean = true,
        @SerializedName("invalidLayer")
        var invalidLayer: String = "",
        @SerializedName("refreshInterval")
        var refreshInterval: Int = 20,
        @SerializedName("rewardVideo")
        var rewardVideo: RewardVideoBean,
        @SerializedName("splash")
        var splash: SplashBean,
        @SerializedName("interstitialFull")
        var interstitialFull: InstlFullBean,
        @SerializedName("information")
        var information: InfoBean,
        @SerializedName("banner")
        var banner: BannerBean,
        @SerializedName("drawInformation")
        var drawInformation: DrawInfoBean
) : BaseCustomViewModel(), Parcelable




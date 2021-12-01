package com.donews.common.ad.business.bean

import android.os.Parcelable
import com.dn.sdk.sdk.bean.BaseAdIdConfigBean
import com.donews.common.ad.business.constant.*
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
open class NewBaseAdIdConfigBean : BaseAdIdConfigBean(), Parcelable {

    @SerializedName("splashId")
    var splashId: String = ""

    @SerializedName("interstitialId")
    var interstitialId: String = ""

    @SerializedName("rewardVideoId")
    var rewardVideoId: String = ""

    @SerializedName("invalidRewardVideoId")
    var invalidRewardVideoId: String = ""

    override fun getAdIdByKey(key: String): String {
        return when (key) {
            NEW_SPLASH_ID -> splashId
            NEW_INTERSTITIAL_ID -> interstitialId
            NEW_REWARD_VIDEO_ID -> rewardVideoId
            NEW_INVALID_REWARD_VIDEO_ID -> invalidRewardVideoId
            else -> ""
        }
    }
}
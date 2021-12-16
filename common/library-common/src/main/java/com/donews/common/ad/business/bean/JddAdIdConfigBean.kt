package com.donews.common.ad.business.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.StringBuilder


/**
 * 新的广告位id配置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/1 16:52
 */
@Parcelize
class JddAdIdConfigBean : BaseAdIdConfigBean(), Parcelable {
    init {
        splashId = "158534"
        interstitialId = "158536"
        rewardVideoId = "158530"
        invalidRewardVideoId = "158532"
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("splashId = $splashId ,")
        stringBuilder.append("interstitialId = $interstitialId ,")
        stringBuilder.append("rewardVideoId = $rewardVideoId ,")
        stringBuilder.append("invalidRewardVideoId = $invalidRewardVideoId ,")
        return stringBuilder.toString()
    }
}
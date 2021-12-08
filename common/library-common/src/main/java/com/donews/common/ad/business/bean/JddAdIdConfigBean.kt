package com.donews.common.ad.business.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


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
}
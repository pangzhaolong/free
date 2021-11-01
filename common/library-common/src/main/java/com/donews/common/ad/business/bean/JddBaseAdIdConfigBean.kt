package com.donews.common.ad.business.bean

import com.dn.sdk.sdk.bean.BaseAdIdConfigBean
import com.donews.common.ad.business.constant.*

/**
 * 奖多多基础广告id配置类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:22
 */
open class JddBaseAdIdConfigBean : BaseAdIdConfigBean() {

    var invalidInterstitialId = ""
    var invalidRewardVideoId = ""
    var splashId = ""
    var splashMinimumCodeId = ""
    var interstitialId = ""
    var rewardVideoId = ""

    override fun getAdIdByKey(key: String): String {
        return when (key) {
            INVALID_INTERSTITIAL -> invalidInterstitialId
            INVALID_REWARD_VIDEO -> invalidRewardVideoId
            SPLASH -> splashId
            SPLASH_MINIMUM_CODE_ID -> splashMinimumCodeId
            INTERSTITIAL -> interstitialId
            REWARD_VIDEO -> rewardVideoId
            else -> ""
        }
    }
}
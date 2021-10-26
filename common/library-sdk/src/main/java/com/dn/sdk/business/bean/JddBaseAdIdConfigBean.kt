package com.dn.sdk.business.bean

import com.dn.sdk.business.constant.*
import com.dn.sdk.sdk.bean.BaseAdIdConfigBean

/**
 * 奖多多基础广告id配置类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:22
 */
class JddBaseAdIdConfigBean : BaseAdIdConfigBean() {

    var invalidInterstitialId = "946943378"
    var invalidRewardVideoId = "946943379"
    var splashId = "887601190"
    var interstitialId = "946943377"
    var rewardVideoId = "946943369"

    override fun getAdIdByKey(key: String): String {
        return when (key) {
            INVALID_INTERSTITIAL -> invalidInterstitialId
            INVALID_REWARD_VIDEO -> invalidRewardVideoId
            SPLASH -> splashId
            INTERSTITIAL -> interstitialId
            REWARD_VIDEO -> rewardVideoId
            else -> ""
        }
    }
}
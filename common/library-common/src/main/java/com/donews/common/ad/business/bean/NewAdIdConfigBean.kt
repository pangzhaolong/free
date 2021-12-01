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
class NewAdIdConfigBean() : NewBaseAdIdConfigBean(), Parcelable {
    init {
        splashId = "92914"
        interstitialId = "92513"
        rewardVideoId = "91918"
        invalidRewardVideoId = "92540"
    }
}
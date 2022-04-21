package com.donews.yfsdk.bean

import android.os.Parcelable
import com.donews.common.contract.BaseCustomViewModel
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
class RewardVideoId(
        @SerializedName("reward_video_id")
        var reward_video_id: String = "",
        @SerializedName("layer_id")
        var layer_id: String = "G2"
) : BaseCustomViewModel(), Parcelable
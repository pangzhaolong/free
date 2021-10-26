package com.donews.main.entitys.resps

import com.donews.base.model.BaseLiveDataModel
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.common.contract.BaseCustomViewModel
import com.google.gson.annotations.SerializedName


/**
 *
 * 退出拦截弹出框的商品推荐数据
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/23 13:30
 */

data class ExitDialogRecommendGoodsResp(
    @SerializedName("list")
    var list: List<ExitDialogRecommendGoods> = listOf()
) : BaseCustomViewModel()

data class ExitDialogRecommendGoods(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("main_pic")
    var mainPic: String = "",
    @SerializedName("original_price")
    var originalPrice: Float = 0f,
    @SerializedName("display_price")
    var displayPrice: Float = 0f,
    @SerializedName("total_people")
    var totalPeople: Int = 0,
    @SerializedName("id")
    var id: String = "",
    @SerializedName("goods_id")
    var goodsId: String = "",
    @SerializedName("winning_rates")
    var winningRates: Float = 0f
) : BaseCustomViewModel()
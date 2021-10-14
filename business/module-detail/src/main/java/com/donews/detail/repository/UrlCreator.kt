package com.donews.detail.repository

import com.dn.events.BuildConfig

/**
 * Api url 接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/13 10:02
 */
class UrlCreator {

    /** 商品详情api */
    fun getGoodsDetailApi(): String {
        return BuildConfig.BASE_URL + "v1/get-goods-details"
    }

}
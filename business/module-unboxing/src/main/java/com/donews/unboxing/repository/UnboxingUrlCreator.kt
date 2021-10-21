package com.donews.unboxing.repository

import com.dn.events.BuildConfig

/**
 * 晒单页url
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 19:27
 */
class UnboxingUrlCreator {

    /** 商品详情api */
    fun getUnboxingDataUrl(): String {
        return BuildConfig.BASE_URL + "v1/get-goods-details"
    }
}
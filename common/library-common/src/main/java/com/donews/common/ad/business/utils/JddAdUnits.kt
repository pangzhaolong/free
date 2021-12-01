package com.donews.common.ad.business.utils

import com.donews.common.ad.business.callback.JddAdOpenConfig

/**
 * 广告工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/1 18:10
 */
object JddAdUnits {

    /** 广告是否打开 */
    fun isOpenAd(): Boolean {
        return JddAdOpenConfig.isOpenAd()
    }

}
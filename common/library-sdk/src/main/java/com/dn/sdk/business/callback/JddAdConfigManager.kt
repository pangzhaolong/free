package com.dn.sdk.business.callback

import com.dn.sdk.business.bean.JddAdConfigBean
import com.donews.network.BuildConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException

/**
 * 广告策略配置管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:36
 */
object JddAdConfigManager {

    var jddAdConfigBean = JddAdConfigBean()

    fun init() {
        EasyHttp.get(BuildConfig.AD_CONFIG)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<JddAdConfigBean>() {
                override fun onError(e: ApiException?) {

                }

                override fun onSuccess(t: JddAdConfigBean?) {
                    t?.run {
                        jddAdConfigBean = this
                    }
                }
            })

    }
}
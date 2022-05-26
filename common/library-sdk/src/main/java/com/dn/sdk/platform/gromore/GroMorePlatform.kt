package com.dn.sdk.platform.gromore

import com.dn.sdk.bean.BaseAdIdConfigBean
import com.dn.sdk.loader.IAdLoader
import com.dn.sdk.platform.IPlatform

/**
 * 多牛平台
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:00
 */
class GroMorePlatform : IPlatform {

    private val loader = GroMoreAdLoader()

    override fun getLoader(): IAdLoader {
        return loader
    }

    override fun getAdIdByKey(key: String): String {
        return ""
    }
}
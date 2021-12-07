package com.dn.sdk.platform.closead

import com.dn.sdk.loader.IAdLoader
import com.dn.sdk.platform.IPlatform

/**
 * 无广告平台
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/27 10:34
 */
class NoAdPlatform : IPlatform {

    private val loader = NoAdLoader()

    override fun getLoader(): IAdLoader {
        return loader
    }

    override fun getAdIdByKey(key: String): String {
        return ""
    }
}
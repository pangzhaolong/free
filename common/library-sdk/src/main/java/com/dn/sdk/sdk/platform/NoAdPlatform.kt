package com.dn.sdk.sdk.platform

import com.dn.sdk.sdk.interfaces.loader.IRealLoader
import com.dn.sdk.sdk.loader.NoAdRealLoader

/**
 * 无广告平台
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/27 10:34
 */
class NoAdPlatform : IPlatform {

    private val loader = NoAdRealLoader()

    override fun getLoader(): IRealLoader {
        return loader
    }

    override fun getAdIdByKey(key: String): String {
        return ""
    }
}
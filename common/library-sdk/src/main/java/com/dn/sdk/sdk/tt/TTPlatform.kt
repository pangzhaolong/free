package com.dn.sdk.sdk.tt

import com.dn.sdk.sdk.bean.BaseAdIdConfigBean
import com.dn.sdk.sdk.interfaces.loader.IRealLoader
import com.dn.sdk.sdk.platform.IPlatform

/**
 * cdj 广告平台 基础类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 16:58
 */
class TTPlatform(private val adIdConfigBean: BaseAdIdConfigBean) : IPlatform {

    private val loader = PolyTTRealLoader()

    override fun getLoader(): IRealLoader {
        return loader
    }

    override fun getAdIdByKey(key: String): String {
        return adIdConfigBean.getAdIdByKey(key)
    }
}
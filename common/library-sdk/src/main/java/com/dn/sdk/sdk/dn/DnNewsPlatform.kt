package com.dn.sdk.sdk.dn

import com.dn.sdk.sdk.bean.BaseAdIdConfigBean
import com.dn.sdk.sdk.interfaces.loader.IRealLoader
import com.dn.sdk.sdk.platform.IPlatform

/**
 * 多牛平台
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:00
 */
class DnNewsPlatform(private val adIdConfigBean: BaseAdIdConfigBean) : IPlatform {

    private val loader = DnNewsRealLoader()

    override fun getLoader(): IRealLoader {
        return loader
    }

    override fun getAdIdByKey(key: String): String {
        return adIdConfigBean.getAdIdByKey(key)
    }
}
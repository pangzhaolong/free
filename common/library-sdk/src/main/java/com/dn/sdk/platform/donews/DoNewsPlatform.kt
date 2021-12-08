package com.dn.sdk.platform.donews

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
class DoNewsPlatform(private val adIdConfigBean: BaseAdIdConfigBean) : IPlatform {

    private val loader = DoNewsAdLoader()

    override fun getLoader(): IAdLoader {
        return loader
    }

    override fun getAdIdByKey(key: String): String {
        return adIdConfigBean.getAdIdByKey(key)
    }
}
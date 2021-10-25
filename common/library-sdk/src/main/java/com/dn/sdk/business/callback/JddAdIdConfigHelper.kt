package com.dn.sdk.business.callback

import com.dn.sdk.business.bean.JddBaseAdIdConfig
import com.dn.sdk.sdk.dn.DnNewsPlatform
import com.dn.sdk.sdk.platform.BaseAdIdConfigHelper
import com.dn.sdk.sdk.platform.IPlatform

/**
 * 奖多多 广告id 辅助类，使用单列
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:23
 */
object JddAdIdConfigHelper : BaseAdIdConfigHelper<JddBaseAdIdConfig>() {
    override fun getDefaultConfig(): JddBaseAdIdConfig {
        return JddBaseAdIdConfig()
    }

    override fun getPlatform(): IPlatform {
        return DnNewsPlatform(getDefaultConfig())
    }
}
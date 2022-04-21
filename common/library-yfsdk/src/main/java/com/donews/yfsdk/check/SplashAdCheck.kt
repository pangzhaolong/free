package com.donews.yfsdk.check

import com.dn.sdk.AdCustomError
import com.donews.yfsdk.manager.AdConfigManager

/**
 * 插屏广告统计监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/2 14:58
 */
object SplashAdCheck {

    @Synchronized
    fun isEnable(): AdCustomError {
        if (!AdConfigManager.mNormalAdBean.enable) {
            return AdCustomError.CloseAdAll
        }
        if (!AdConfigManager.mNormalAdBean.splash.enable) {
            return AdCustomError.CloseAdOne
        }

        return AdCustomError.OK
    }
}
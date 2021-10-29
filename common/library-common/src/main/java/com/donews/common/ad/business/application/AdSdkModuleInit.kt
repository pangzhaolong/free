package com.donews.common.ad.business.application

import com.dn.sdk.BuildConfig
import com.donews.common.ad.business.callback.JddAdConfigManager
import com.donews.common.ad.business.loader.AdManager
import com.dn.sdk.sdk.AdSdkManager
import com.donews.base.base.BaseApplication
import com.donews.common.IModuleInit
import com.donews.utilslibrary.utils.DeviceUtils
import com.orhanobut.logger.Logger

/**
 * 广告Sdk初始化
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 19:35
 */
class AdSdkModuleInit : IModuleInit {
    override fun onInitAhead(application: BaseApplication?): Boolean {
        application?.let {
            JddAdConfigManager.init()
            AdManager.initAdIdConfig(BuildConfig.AD_ID_CONFIG)
            AdManager.initSDK(it, BuildConfig.DEBUG, BuildConfig.APP_ID_GROMORE)
            AdSdkManager.channel = DeviceUtils.getChannelName()
        }
        Logger.d("初始广告")
        return false
    }

    override fun onInitLow(application: BaseApplication?): Boolean {
        return false
    }
}
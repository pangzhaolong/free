package com.donews.common.ad.business.application

import com.dn.sdk.BuildConfig
import com.donews.base.base.BaseApplication
import com.donews.common.IModuleInit
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.donews.common.ad.business.manager.JddAdManager
import com.donews.utilslibrary.utils.DeviceUtils
import com.donews.utilslibrary.utils.Utils

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
            AdManager.initSDK(it, DeviceUtils.getChannelName(), BuildConfig.DEBUG)
            JddAdConfigManager.init()
            JddAdManager.init()
        }
        return false
    }

    override fun onInitLow(application: BaseApplication?): Boolean {
        return false
    }
}
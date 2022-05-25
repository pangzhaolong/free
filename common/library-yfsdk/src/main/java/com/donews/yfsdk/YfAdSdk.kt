package com.donews.yfsdk

import android.app.Application
import com.donews.utilslibrary.utils.DeviceUtils
import com.donews.yfsdk.bean.AdConfigBean
import com.donews.yfsdk.loader.AdManager
import com.donews.yfsdk.manager.AdConfigManager
import com.donews.yfsdk.monitor.InterstitialFullAdCheck
import com.donews.yfsdk.utils.AppUtil

object YfAdSdk {
    fun init(application: Application, adConfigBean: AdConfigBean) {
        application?.let {
            AdManager.initSDK(it, DeviceUtils.getChannelName(), BuildConfig.DEBUG)
            AdConfigManager.init(adConfigBean)
            AppUtil.saveAppInstallTime()
            InterstitialFullAdCheck.reset()
        }
    }
}
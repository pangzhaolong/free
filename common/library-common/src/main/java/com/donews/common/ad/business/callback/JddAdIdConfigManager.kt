package com.donews.common.ad.business.callback

import com.dn.sdk.sdk.dn.DnNewsPlatform
import com.dn.sdk.sdk.platform.BaseAdIdConfigManager
import com.dn.sdk.sdk.platform.IPlatform
import com.dn.sdk.sdk.platform.NoAdPlatform
import com.donews.common.ad.business.bean.NewAdIdConfigBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.orhanobut.logger.Logger

/**
 * 奖多多 广告id 辅助类，使用单列
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:23
 */
object JddAdIdConfigManager : BaseAdIdConfigManager<NewAdIdConfigBean>() {

    private var noAdPlatform: NoAdPlatform = NoAdPlatform()
    private var dnAdPlatform: DnNewsPlatform? = null


    override fun initAdIdConfig(configPath: String) {
        EasyHttp.get(configPath)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<NewAdIdConfigBean>() {
                override fun onError(e: ApiException) {
                    mInit = true
                    mConfigBean = getDefaultConfig()
                    callInitListener()
                }

                override fun onSuccess(t: NewAdIdConfigBean?) {
                    Logger.d(t)
                    mConfigBean = t ?: getDefaultConfig()
                    mInit = true
                    callInitListener()
                }
            })
    }

    override fun getDefaultConfig(): NewAdIdConfigBean {
        return NewAdIdConfigBean()
    }

    override fun getPlatform(): IPlatform {
        val configBean: NewAdIdConfigBean = getConfig()
        if (dnAdPlatform == null) {
            dnAdPlatform = DnNewsPlatform(configBean)
        }

        val openAd = JddAdOpenConfig.isOpenAd()
        return if (openAd) {
            dnAdPlatform!!
        } else {
            noAdPlatform
        }
    }
}
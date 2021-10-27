package com.dn.sdk.business.callback

import com.dn.sdk.business.bean.JddAdIdConfigBean
import com.dn.sdk.sdk.dn.DnNewsPlatform
import com.dn.sdk.sdk.platform.BaseAdIdConfigManager
import com.dn.sdk.sdk.platform.IPlatform
import com.dn.sdk.sdk.platform.NoAdPlatform
import com.dn.sdk.sdk.tt.TTPlatform
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.orhanobut.logger.Logger
import java.util.*

/**
 * 奖多多 广告id 辅助类，使用单列
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 17:23
 */
object JddAdIdConfigManager : BaseAdIdConfigManager<JddAdIdConfigBean>() {

    private var noAdPlatform: NoAdPlatform = NoAdPlatform()
    private var dnAdPlatform: DnNewsPlatform? = null
    private var csdPlatform: TTPlatform? = null


    override fun initAdIdConfig(configPath: String) {
        EasyHttp.get(configPath)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<JddAdIdConfigBean>() {
                override fun onError(e: ApiException) {
                    mInit = true
                    mConfigBean = getDefaultConfig()
                    callInitListener()
                }

                override fun onSuccess(t: JddAdIdConfigBean?) {
                    mConfigBean = t ?: getDefaultConfig()
                    mInit = true
                    callInitListener()
                }
            })
    }

    override fun getDefaultConfig(): JddAdIdConfigBean {
        return JddAdIdConfigBean()
    }

    override fun getPlatform(): IPlatform {
        val configBean: JddAdIdConfigBean = getConfig()
        if (dnAdPlatform == null) {
            dnAdPlatform = DnNewsPlatform(configBean.dnAdIdConfigBean)
        }
        if (csdPlatform == null) {
            csdPlatform = TTPlatform(configBean.csjAdIdConfigBean)
        }

        if (!configBean.openAd) {
            return noAdPlatform
        }

        if (configBean.bjcsj == 0) {
            return dnAdPlatform!!
        }

        if (configBean.bjdn == 0) {
            return csdPlatform!!
        }

        val total: Int = configBean.bjdn + configBean.bjcsj
        val random = Random()
        val index = random.nextInt(total)
        return if (index < configBean.bjdn) {
            dnAdPlatform!!
        } else {
            csdPlatform!!
        }
    }
}
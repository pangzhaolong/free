package com.donews.common.ad.business.callback

import com.donews.common.BuildConfig
import com.donews.common.ad.business.bean.AdOpenConfigBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.withConfigParams
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

/**
 * 奖多多开关配置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/1 17:27
 */
object JddAdOpenConfig {
    private val mmkv = MMKV.defaultMMKV()!!

    private const val KEY_JDD_AD_OPEN_CONFIG = "KEY_JDD_AD_OPEN_CONFIG"

    private var adOpenConfigBean: AdOpenConfigBean = AdOpenConfigBean()

    init {
        val saveBean = mmkv.decodeParcelable(KEY_JDD_AD_OPEN_CONFIG, AdOpenConfigBean::class.javaObjectType)
        saveBean?.let {
            adOpenConfigBean = it
        }
    }

    fun init() {
        EasyHttp.get(BuildConfig.AD_OPEN_CONFIG.withConfigParams())
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<AdOpenConfigBean>() {

                override fun onSuccess(t: AdOpenConfigBean?) {
                    Logger.d(t)
                    t?.let {
                        adOpenConfigBean = it
                        mmkv.encode(KEY_JDD_AD_OPEN_CONFIG, it)
                    }
                }

                override fun onError(e: ApiException?) {

                }
            })
    }

    fun isOpenAd(): Boolean {
        return adOpenConfigBean.openAd
    }
}
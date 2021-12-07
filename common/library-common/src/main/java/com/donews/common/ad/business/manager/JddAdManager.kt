package com.donews.common.ad.business.manager

import com.dn.sdk.manager.config.BaseAdConfigManager
import com.dn.sdk.platform.IPlatform
import com.dn.sdk.platform.closead.NoAdPlatform
import com.dn.sdk.platform.donews.DoNewsPlatform
import com.donews.common.BuildConfig
import com.donews.common.ad.business.bean.JddAdOpenConfigBean
import com.donews.common.ad.business.bean.JddAdIdConfigBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.withConfigParams
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

/**
 * 奖多多 广告 管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 15:52
 */
object JddAdManager : BaseAdConfigManager() {

    private val mmkv = MMKV.defaultMMKV()!!

    private const val KEY_JDD_AD_OPEN_CONFIG = "KEY_JDD_AD_OPEN_CONFIG"
    private const val KEY_JDD_AD_ID_CONFIG = "KEY_JDD_AD_ID_CONFIG"

    private var mAdConfigBean: JddAdOpenConfigBean = getDefaultAdConfig()
    private var mAdIdConfigBean: JddAdIdConfigBean = getDefaultAdIdConfig()

    private val noAdPlatform = NoAdPlatform()
    private var doNewsPlatform: DoNewsPlatform? = null
    override fun init() {
        EasyHttp.get(BuildConfig.AD_OPEN_CONFIG.withConfigParams())
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<JddAdOpenConfigBean>() {
                override fun onSuccess(t: JddAdOpenConfigBean?) {
                    if (t != null) {
                        mmkv.encode(KEY_JDD_AD_OPEN_CONFIG, t)
                    }
                    mAdConfigBean = t ?: getDefaultAdConfig()

                    Logger.d(t)
                    if (mAdConfigBean.openAd) {
                        initAdIdConfig()
                    } else {
                        setInitSuccess()
                    }
                }

                override fun onError(e: ApiException?) {
                    mAdConfigBean = getDefaultAdConfig()
                    if (mAdConfigBean.openAd) {
                        initAdIdConfig()
                    } else {
                        setInitSuccess()
                        callListener()
                    }
                }
            })
    }


    fun initAdIdConfig() {
        EasyHttp.get(BuildConfig.AD_ID_CONFIG.withConfigParams())
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<JddAdIdConfigBean>() {

                override fun onSuccess(t: JddAdIdConfigBean?) {
                    t?.let {
                        mmkv.encode(KEY_JDD_AD_ID_CONFIG, it)
                    }
                    mAdIdConfigBean = t ?: getDefaultAdIdConfig()

                    setInitSuccess()
                    callListener()
                }

                override fun onError(e: ApiException) {
                    mAdIdConfigBean = getDefaultAdIdConfig()
                    setInitSuccess()
                    callListener()
                }
            })
    }

    fun getDefaultAdConfig(): JddAdOpenConfigBean {
        val last = mmkv.decodeParcelable(KEY_JDD_AD_OPEN_CONFIG, JddAdOpenConfigBean::class.java)
        return last ?: JddAdOpenConfigBean()
    }

    fun getDefaultAdIdConfig(): JddAdIdConfigBean {
        val last = mmkv.decodeParcelable(KEY_JDD_AD_OPEN_CONFIG, JddAdIdConfigBean::class.java)
        return last ?: JddAdIdConfigBean()
    }

    override fun getPlatform(): IPlatform {
        if (!mAdConfigBean.openAd) {
            return noAdPlatform
        }

        if (doNewsPlatform == null) {
            doNewsPlatform = DoNewsPlatform(mAdIdConfigBean)
        }
        return doNewsPlatform!!
    }

    fun isOpenAd(): Boolean {
        return mAdConfigBean.openAd
    }
}
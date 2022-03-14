package com.donews.common.ad.business.manager

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.donews.common.ad.business.bean.JddAdConfigBean
import com.donews.network.BuildConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.LogUtil
import com.donews.utilslibrary.utils.withConfigParams
import com.tencent.mmkv.MMKV

/**
 * 广告策略配置管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:36
 */
object JddAdConfigManager {

    private const val UPDATE_CONFIG_MSG = 11004

    private val mmkv = MMKV.defaultMMKV()!!

    private const val KEY_JDD_AD_CONFIG = "key_jdd_ad_config";

    var jddAdConfigBean = JddAdConfigBean()

    private var init: Boolean = false

    private val mListener = arrayListOf<() -> Unit>()

    init {
        val saveBean = mmkv.decodeParcelable(KEY_JDD_AD_CONFIG, JddAdConfigBean::class.javaObjectType)
        saveBean?.let {
            jddAdConfigBean = it
        }
    }

    val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_CONFIG_MSG -> update()
            }
        }
    }

    fun init() {
        handler.sendEmptyMessage(UPDATE_CONFIG_MSG)
    }

    fun update() {
        LogUtil.e("JddAdConfigManager update")
        EasyHttp.get(BuildConfig.AD_CONFIG.withConfigParams(false))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<JddAdConfigBean>() {
                    override fun onError(e: ApiException?) {
                        init = true
                        callListener()
                        handler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000L)
                    }

                    override fun onSuccess(t: JddAdConfigBean?) {
                        init = true
                        t?.run {
                            jddAdConfigBean = this
                            mmkv.encode(KEY_JDD_AD_CONFIG, jddAdConfigBean)
                            handler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, t.refreshInterval * 1000L)
                        }
                        callListener()
                    }
                })
    }

    private fun callListener() {
        val iterator = mListener.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            next.invoke()
            iterator.remove()
        }
    }

    fun addListener(success: () -> Unit) {
        if (init) {
            success.invoke()
        } else {
            mListener.add(success)
        }
    }
}
package com.dn.sdk.sdk.platform

import com.dn.sdk.sdk.interfaces.IAdIdConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import java.util.*

/**
 *  平台初始化监听实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 16:22
 */
abstract class BaseAdIdConfigHelper<T> : IAdIdConfig {

    private var mInit = false
    private val mCallbacks: MutableList<IAdIdConfigCallback> = ArrayList()

    var mConfigBean: T? = null

    @Synchronized
    open fun addInitListener(listener: IAdIdConfigCallback) {
        if (isInitConfig()) {
            listener.initSuccess()
        } else {
            mCallbacks.add(listener)
        }
    }

    @Synchronized
    open fun callInitListener() {
        if (!isInitConfig()) {
            return
        }
        val iterable = mCallbacks.iterator()
        while (iterable.hasNext()) {
            val listener = iterable.next()
            listener.initSuccess()
            iterable.remove()
        }
    }

    override fun initAdIdConfig(configPath: String) {
        EasyHttp.get(configPath)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<T>() {
                override fun onError(e: ApiException) {
                    mInit = true
                    mConfigBean = getDefaultConfig()
                    callInitListener()

                }

                override fun onSuccess(t: T) {
                    mConfigBean = t ?: getDefaultConfig()
                    mInit = true
                    callInitListener()
                }
            })
    }

    fun isInitConfig(): Boolean {
        return mInit
    }

    /** 默认的配置 */
    abstract fun getDefaultConfig(): T

    /** 根据策略返回平台信息 */
    abstract fun getPlatform(): IPlatform
}
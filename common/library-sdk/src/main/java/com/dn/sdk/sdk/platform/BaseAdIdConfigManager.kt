package com.dn.sdk.sdk.platform

import com.dn.sdk.sdk.interfaces.idconfig.IAdIdConfigManager
import java.util.*

/**
 *  基础广告id配置管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 16:22
 */
abstract class BaseAdIdConfigManager<T> : IAdIdConfigManager<T> {

    var mInit = false
    private val mCallbacks: MutableList<IAdIdConfigCallback> = ArrayList()

    var mConfigBean: T? = null

    @Synchronized
    override fun addInitListener(listener: IAdIdConfigCallback) {
        if (isInitConfig()) {
            listener.initSuccess()
        } else {
            mCallbacks.add(listener)
        }
    }


    override fun isInitConfig(): Boolean {
        return mInit
    }

    override fun getConfig(): T {
        if (mConfigBean == null) {
            mConfigBean = getDefaultConfig()
        }
        return mConfigBean!!
    }

    fun callInitListener() {
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
}
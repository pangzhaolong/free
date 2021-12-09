package com.dn.sdk.manager.config

import java.util.ArrayList

/**
 * 基础广告配置管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 13:31
 */
abstract class BaseAdConfigManager : IAdConfigManager {

    private var mInit: Boolean = false

    private val mListener: MutableList<IAdConfigInitListener> = ArrayList()

    override fun addInitListener(listener: IAdConfigInitListener) {
        if (isInitSuccess()) {
            listener.initSuccess()
        } else {
            mListener.add(listener)
        }
    }

    override fun isInitSuccess(): Boolean {
        return mInit
    }

    fun setInitSuccess() {
        mInit = true
    }

    fun resetInit(){
        mInit = false
    }

    fun callListener() {
        if (!isInitSuccess()) {
            return
        }
        val iterable = mListener.iterator()
        while (iterable.hasNext()) {
            val listener = iterable.next()
            listener.initSuccess()
            iterable.remove()
        }
    }
}
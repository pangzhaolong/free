package com.dn.sdk.listener.impl

import com.dn.sdk.listener.IAdNativeListener

/**
 *  信息流 监听 null实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 17:56
 */
open class SimpleNativeListener : IAdNativeListener {
    override fun onAdStatus(code: Int, any: Any?) {

    }

    override fun onAdExposure() {

    }

    override fun onAdClicked() {

    }

    override fun onAdError(var1: String?) {

    }
}
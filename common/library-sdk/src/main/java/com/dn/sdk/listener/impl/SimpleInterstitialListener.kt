package com.dn.sdk.listener.impl

import com.dn.sdk.listener.IAdInterstitialListener

/**
 * IAdInterstitialListener 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:58
 */
open class SimpleInterstitialListener : IAdInterstitialListener {
    override fun onAdStatus(code: Int, any: Any?) {

    }

    override fun onAdLoad() {

    }

    override fun onAdShow() {

    }

    override fun onAdExposure() {

    }

    override fun onAdClicked() {

    }

    override fun onAdClosed() {

    }

    override fun onAdError(code: Int, errorMsg: String?) {

    }
}
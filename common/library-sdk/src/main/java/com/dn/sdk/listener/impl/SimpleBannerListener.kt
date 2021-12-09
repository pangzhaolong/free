package com.dn.sdk.listener.impl

import com.dn.sdk.listener.IAdBannerListener

/**
 *  IAdBannerListener 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:57
 */
open class SimpleBannerListener : IAdBannerListener {
    override fun onAdStatus(code: Int, any: Any?) {

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
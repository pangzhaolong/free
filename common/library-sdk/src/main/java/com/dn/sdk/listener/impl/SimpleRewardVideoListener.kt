package com.dn.sdk.listener.impl

import com.dn.sdk.listener.IAdRewardVideoListener

/**
 * IAdRewardVideoListener 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:55
 */
open class SimpleRewardVideoListener : IAdRewardVideoListener {
    override fun onAdStatus(code: Int, any: Any?) {

    }

    override fun onAdLoad() {

    }

    override fun onAdShow() {

    }

    override fun onAdVideoClick() {

    }

    override fun onRewardVerify(result: Boolean) {

    }

    override fun onAdClose() {

    }

    override fun onVideoCached() {

    }

    override fun onVideoComplete() {

    }

    override fun onAdError(code: Int, errorMsg: String?) {

    }

    override fun onAdStartLoad() {

    }
}
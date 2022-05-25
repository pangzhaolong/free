package com.dn.sdk.listener.interstitialfull

/**
 * IAdInterstitialListener 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 9:58
 */
open class SimpleInterstitialFullListener : IAdInterstitialFullScreenListener {
    override fun onAdStatus(code: Int, any: Any?) {

    }

    override fun onAdLoad() {

    }

    override fun onAdCached() {
    }

    override fun onAdError(errorCode: Int, errprMsg: String) {
    }

    override fun onAdShow() {

    }

    override fun onAdClicked() {

    }

    override fun onAdComplete() {
    }

    override fun onAdClose() {
    }

    override fun onSkippedVideo() {
    }

    override fun onRewardVerify(reward: Boolean) {
    }

    override fun onAdShowFail(errCode: Int, errMsg: String) {
    }

    override fun onAdVideoError(errCode: Int, errMsg: String) {
    }

    override fun onAdStartLoad() {

    }
}
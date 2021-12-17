package com.dn.sdk.listener.proxy

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.IAdRewardVideoListener
import com.dn.sdk.utils.AdLoggerUtils

/**
 * 激励视频加载日志
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/7 17:45
 */
class LoggerRewardVideoListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdRewardVideoListener?
) : IAdRewardVideoListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdStartLoad()"))
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdStatus($code,$any)"))
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdLoad()"))
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdShow()"))
        listener?.onAdShow()
        countTrack.onAdShow()
    }

    override fun onAdVideoClick() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdVideoClick()"))
        listener?.onAdVideoClick()
        countTrack.onAdClick()
    }

    override fun onRewardVerify(result: Boolean) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onRewardVerify($result)"))
        listener?.onRewardVerify(result)
        countTrack.onRewardVerify(result)
    }

    override fun onAdClose() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdClose()"))
        listener?.onAdClose()
        countTrack.onAdClose()
    }

    override fun onVideoCached() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onVideoCached()"))
        listener?.onVideoCached()
    }

    override fun onVideoComplete() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onVideoComplete()"))
        listener?.onVideoComplete()
        countTrack.onVideoComplete()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }
}
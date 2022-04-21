package com.dn.sdk.listener.rewardvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener
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
    }

    override fun onAdVideoClick() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdVideoClick()"))
        listener?.onAdVideoClick()
    }

    override fun onRewardVerify(result: Boolean) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onRewardVerify($result)"))
        listener?.onRewardVerify(result)
    }

    override fun onAdClose() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdClose()"))
        listener?.onAdClose()
    }

    override fun onVideoCached() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onVideoCached()"))
        listener?.onVideoCached()
    }

    override fun onVideoComplete() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onVideoComplete()"))
        listener?.onVideoComplete()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo onAdError($code,$errorMsg)"))
        listener?.onAdError(code, errorMsg)
    }

    override fun addReportEcpmParamsWhenReward(params: EcpmParam) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo addReportEcpmParams($params)"))
        listener?.addReportEcpmParamsWhenReward(params)
    }

    override fun reportEcpmSuccessWhenReward(response: EcpmResponse) {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo reportEcpmSuccessWhenReward($response)"))
        listener?.reportEcpmSuccessWhenReward(response)
    }

    override fun reportEcpmFailWhenReward() {
        AdLoggerUtils.d(AdLoggerUtils.createMsg(adRequest, "RewardVideo reportEcpmFailWhenReward()"))
        listener?.reportEcpmFailWhenReward()
    }
}
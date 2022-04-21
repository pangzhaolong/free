package com.dn.sdk.listener.rewardvideo

import com.dn.sdk.bean.AdRequest
import com.dn.sdk.bean.EcpmParam
import com.dn.sdk.bean.EcpmResponse
import com.dn.sdk.count.CountTrackImpl
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener

/**
 * 激励视频埋点事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/18 16:52
 */
class TrackRewardVideoListenerProxy(
    private val adRequest: AdRequest,
    private val listener: IAdRewardVideoListener?
) : IAdRewardVideoListener {

    private val countTrack = CountTrackImpl(adRequest)

    override fun onAdStartLoad() {
        countTrack.onAdStartLoad()
        listener?.onAdStartLoad()
    }

    override fun onAdStatus(code: Int, any: Any?) {
        listener?.onAdStatus(code, any)
    }

    override fun onAdLoad() {
        listener?.onAdLoad()
    }

    override fun onAdShow() {
        countTrack.onAdShow()
        listener?.onAdShow()
    }

    override fun onAdVideoClick() {
        countTrack.onAdClick()
        listener?.onAdVideoClick()
    }

    override fun onRewardVerify(result: Boolean) {
        countTrack.onRewardVerify(result)
        listener?.onRewardVerify(result)
    }

    override fun onAdClose() {
        countTrack.onAdClose()
        listener?.onAdClose()
    }

    override fun onVideoCached() {
        listener?.onVideoCached()
    }

    override fun onVideoComplete() {
        countTrack.onVideoComplete()
        listener?.onVideoComplete()
    }

    override fun onAdError(code: Int, errorMsg: String?) {
        listener?.onAdError(code, errorMsg)
    }

    override fun addReportEcpmParamsWhenReward(params: EcpmParam) {
        listener?.addReportEcpmParamsWhenReward(params)
    }

    override fun reportEcpmSuccessWhenReward(response: EcpmResponse) {
        listener?.reportEcpmSuccessWhenReward(response)
    }

    override fun reportEcpmFailWhenReward() {
        listener?.reportEcpmFailWhenReward()
    }
}